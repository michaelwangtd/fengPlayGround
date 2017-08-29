import java.util.HashSet;


public class Info {

	String relname;
	double score;
	HashSet<String> index;

	public String getRelname() {
		return relname;
	}
	public void setRelname(String relname) {
		this.relname = relname;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}

	public HashSet<String> getIndex() {
		return index;
	}
	public void setIndex(HashSet<String> index) {
		this.index = index;
	}
	@Override
	public String toString() {
		return "Info [relname=" + relname + ", score=" + score + ", index="
				+ index + "]";
	}

}
