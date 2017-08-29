public class Line {
	public Circle A = null, B = null;
	private final Double eps = 0.001;

	public Line(Circle a, Circle b) throws Exception {
		if (a.equals(b)) {
			throw new Exception("can't creat this line");
		}
		A = a;
		B = b;
	}

	public Circle getFirst() {
		return A;
	}

	public Circle getSecond() {
		return B;
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass() != Line.class)
			return false;
		Line l = (Line) o;
		if ((A.equals(l.A) && B.equals(l.B))
				|| (A.equals(l.B) && B.equals(l.A)))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	/**
	 *
	 * @param l
	 * @return true if lines is crossed
	 */
	public boolean cross(Line l) {
		double Z = (l.B.getPos().y - l.A.getPos().y)
				* (B.getPos().x - A.getPos().x)
				- (l.B.getPos().x - l.A.getPos().x)
				* (B.getPos().y - A.getPos().y);
		double Ca = (l.B.getPos().x - l.A.getPos().x)
				* (A.getPos().y - l.A.getPos().y)
				- (l.B.getPos().y - l.A.getPos().y)
				* (A.getPos().x - l.A.getPos().x);
		double Cb = (B.getPos().x - A.getPos().x)
				* (A.getPos().y - l.A.getPos().y)
				- (B.getPos().y - A.getPos().y)
				* (A.getPos().x - l.A.getPos().x);
		if ((Z == 0) && (Ca == 0) && (Cb == 0))
			return true;

		if (Z == 0)
			return false;

		double Ua = Ca / Z;
		double Ub = Cb / Z;

		if ((0 <= Ua) && (Ua <= 1) && (0 <= Ub) && (Ub <= 1)) {
			if (Ua < eps || Ua > 1 + eps || Ub < eps || Ub > 1 - eps)
				return false;
			return true;
		}
		else
			return false;
	}

	public double length() {
		return Math.sqrt(Math.pow(A.getPos().x - B.getPos().x, 2)
				+ Math.pow(A.getPos().y - B.getPos().y, 2));
	}
}
