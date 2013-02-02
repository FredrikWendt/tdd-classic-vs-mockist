package se.wendt.tdd.mockist;

public class BeanType {

	private String name;

	public BeanType(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name of " + BeanType.class.getSimpleName() + " can't be null");
		}
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BeanType) {
			BeanType other = (BeanType) obj;
			return name.equals(other.name);
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return name;
	}

}
