public class Circle implements Comparable<Circle> {
	private mPoint pos = null;
	private mPoint bPos = null;
	private mPoint lPos = null;
	public long last;
	public int num;

	int neighbours = 0;

	public Circle(mPoint a, int n) throws Exception {
		if (a == null)
			throw new Exception("can'n create this Buble");
		pos = a;
		num = n;
		last = n;
		// neighbours = new HashSet<Circle>();
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass() != Circle.class)
			return false;
		Circle c = (Circle) o;
		if (c.getPos().equals(pos))
			return true;
		return false;
	}

	// public void addNeighbour(Circle c) {
	// neighbours.add(c);
	// }
	//
	// public boolean isNeighbour(Circle c) {
	// if (neighbours.contains(c))
	// return true;
	// return false;
	// }

	public mPoint getPos() {
		return pos;
	}

	public void move(double dx, double dy) {
		pos.move(dx, dy);
	}

	public void save() {
		bPos = pos.clone();
	}

	public void load() {
		if (lPos != null)
			pos = lPos.clone();
	}

	public void solv() {
		lPos = pos.clone();
		pos = bPos.clone();
	}

	@Override
	public int compareTo(Circle o) {
		// TODO Auto-generated method stub
		if (last < o.last)
			return -1;
		if (last > o.last)
			return 1;
		return 0;
	}
}
