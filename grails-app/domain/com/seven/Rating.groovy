package com.seven

class Rating {
	String label
	int order

	static constraints = {
		order(blank:false,nullable:false,maxSize:50)
	}
}
