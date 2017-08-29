public class mPoint {
	public double x = 0;
	public double y = 0;

	public mPoint(Double _x, Double _y) {
		x = _x;
		y = _y;
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass() != mPoint.class)
			return false;
		mPoint p = (mPoint) o;
		if (x == p.x && y == p.y)
			return true;
		return false;
	}

	public void move(double dx, double dy) {
		x = dx;
		y = dy;
	}

	public String toString() {
		int r = 10;
		return "(" + Double.toString(((int) (x * r)) * 1.0 / r) + ";"
				+ Double.toString(((int) (y * r)) * 1.0 / r) + ")";
	}
	
	@Override
	public mPoint clone(){
		return new mPoint(x,y);
	}
}
