package comparable;

public class Company implements Comparable <Company> {
	String name;
	Double mrkCap;
	Double peRatio;
	String industry;
	Double score;
	String geography;
	Company comparable; 
	
	public String getName(){
		return name;
	}
	public Double getMrkCap(){
		return mrkCap;
	}
	public Double getPeRatio(){
		return peRatio;
	}
	public String getIndustry(){
		return industry;
	}
	public String getGeo(){
		return geography;
	}
	public Company getComp(){
		return comparable;
	}
	@Override
	public int compareTo (Company other) {
		if(this.score <= other.score) {
			return -1;
		}else if (this.score > other.score){
			return 1;
		}else{
			return 0;
		}
	}

//just to save
}