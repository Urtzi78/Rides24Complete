package iterator;

import java.util.List;

public class CitiesIterator implements ExtendedIterator<String> {
	int i=0;
	List<String> cities;
	
	public CitiesIterator(List<String> cities) {
		this.cities=cities;
	}
	@Override
	public boolean hasNext() {
		return i < cities.size();
	}

	@Override
	public String next() {
		String c =cities.get(i);
		i = i + 1;
		return c;
	}

	@Override
	public String previous() {
		i = i - 1;
		String c =cities.get(i);
		return c;
	}

	@Override
	public boolean hasPrevious() {
		return i>=1;
	}

	@Override
	public void goFirst() {
		i=0;
	}

	@Override
	public void goLast() {
		i=cities.size();
	}

}
