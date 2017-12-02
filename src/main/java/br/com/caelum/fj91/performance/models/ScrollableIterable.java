package br.com.caelum.fj91.performance.models;

import java.util.Iterator;

import org.hibernate.ScrollableResults;


public class ScrollableIterable<T> implements Iterable<T> {
	private ScrollableResults results;
	
	public ScrollableIterable(ScrollableResults results) {
		this.results = results;
	}
	
	@Override
	public Iterator<T> iterator() { return new ScrollableIterator<>(results);}
	
	private class ScrollableIterator<E> implements Iterator<E> {

		private ScrollableResults results;

		public ScrollableIterator(ScrollableResults results) { this.results = results; }

		@Override
		public boolean hasNext() { return results.next(); }

		@Override
		public E next() { return (E) results.get()[0]; }

	}

}
