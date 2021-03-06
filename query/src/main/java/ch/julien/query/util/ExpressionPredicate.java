package ch.julien.query.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import ch.julien.common.delegate.Predicate;


/**
 * Wrapper for {@link Predicate} implementations that allows to easily build
 * conjunctions with other predicates.
 */
public class ExpressionPredicate<T> implements Predicate<T> {
	
	/** Compiled version of this expression predicate */
	private Predicate<T> compiledPredicate = null;
	private List<Object> expressionTokens = new ArrayList();
	
	public ExpressionPredicate(Predicate<T> predicate) {
		this.expressionTokens.add(predicate);
	}
	
	public ExpressionPredicate<T> and(Predicate<T> predicate) {
		addTokens(BinaryOperator.AND, predicate);
		return this;
	}
	
	public ExpressionPredicate<T> or(Predicate<T> predicate) {
		addTokens(BinaryOperator.OR, predicate);
		return this;
	}
	
	@Override
	public boolean invoke(T arg) {
		if (this.compiledPredicate == null) {
			this.compiledPredicate = compilePredicate();
		}
		return this.compiledPredicate.invoke(arg);
	}
	
	private void addTokens(BinaryOperator op, Predicate<T> predicate) {
		this.compiledPredicate = null;
		this.expressionTokens.add(op);
		this.expressionTokens.add(predicate);
	}

	private Predicate<T> compilePredicate() {
		Stack<Object> stack = new Stack<Object>();
		stack.addAll(getReversePolishNotation(this.expressionTokens));
		return resolveReversePolishNotation(stack);
	}
	
	private static interface Operator {
		<T> void apply(Stack<Object> stack);
	}
	
	private static enum BinaryOperator implements Operator {
		AND		(10) {
			@SuppressWarnings("unchecked")
			@Override public <T> void apply(Stack<Object> stack) {
				Predicate<T> p1 = popPredicate(stack);
				Predicate<T> p2 = popPredicate(stack);
				stack.push(Predicates.and(p1, p2));
			}
		},
		OR		(20) {
			@SuppressWarnings("unchecked")
			@Override public <T> void apply(Stack<Object> stack) {
				Predicate<T> p1 = popPredicate(stack);
				Predicate<T> p2 = popPredicate(stack);
				stack.push(Predicates.or(p1, p2));
			}
		},
		;
		final int precedence;
		BinaryOperator(int precedence) {
			this.precedence = precedence;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <T> Predicate<T> popPredicate(Stack<Object> stack) {
		if ( ! (stack.peek() instanceof Predicate)) {
			resolveReversePolishNotation(stack);
		}
		return (Predicate<T>) stack.pop();
	}
	
	@SuppressWarnings("unchecked")
	private static <T> Predicate<T> resolveReversePolishNotation(Stack<Object> stack) {
		if (stack.size() == 1) {
			return (Predicate<T>) stack.peek();
		}
		BinaryOperator operator = (BinaryOperator) stack.pop();
		operator.apply(stack);
		return (Predicate<T>) stack.peek();
	}

	private static List<Object> getReversePolishNotation(List<Object> input) {
		// shunting yard algorithm (simplified)
		Iterator<?> iterator = input.iterator();
		List<Object> reversePolishNotation = new ArrayList();
		Stack<BinaryOperator> stack = new Stack<BinaryOperator>();
		while (iterator.hasNext()) {
			Object next = (Object) iterator.next();
			if (next instanceof BinaryOperator) {
				BinaryOperator nextOperator = (BinaryOperator) next;
				while ( ! stack.empty() && nextOperator.precedence >= stack.peek().precedence) {
					reversePolishNotation.add(stack.pop());
				}
				stack.push(nextOperator);
			} else if (next instanceof Predicate) {
				reversePolishNotation.add(next);
			} else {
				throw new RuntimeException();
			}
		}
		while ( ! stack.isEmpty()) {
			reversePolishNotation.add(stack.pop());
		}
		return reversePolishNotation;
	}
	
}
