//Set AND MAP JAVASCRIPT

Date.prototype.equals = function(d) {
	if (d instanceof Date && this.valueOf() === d.valueOf())
		return true;
	else
		return false;
}
Date.prototype.hashCode = function() {
	return this.valueOf();
}
String.prototype.hashCode = function() {
	var h = 0, off = 0;
	var len = this.length;
	for (var i = 0; i < len; i++) {
		h = 31 * h + this.charCodeAt(off++);
	}
	return h;

}
String.prototype.equals = function(anotherString) {
	if (this === anotherString) {
		return true;
	}
	if (anotherString instanceof String || typeof anotherString == "string") {
		// var anotherStringObj = new String(anotherString) ;
		if (anotherString.length != this.length)
			return false;
		for (var i = 0; i < this.length; i++) {
			if (this.charAt(i) != anotherString.charAt(i))
				return false;
		}
		return true;
	}
	return false;
}
Number.prototype.hashCode = function() {
	return this.valueOf();// not good!
}
Number.prototype.equals = function(anotherNumber) {
	if (this === anotherNumber) {
		return true;
	}
	if (isNaN(this) || isNaN(anotherNumber))
		throw new Error("equals is not supported for NaN");
	if (anotherNumber instanceof Number || typeof anotherNumber === "number") {
		var anotherNumberObj = new Number(anotherNumber);
		if (this.valueOf() === anotherNumberObj.valueOf())
			return true;
	}
	return false;
}
Boolean.prototype.hashCode = function() {
	return this ? 1231 : 1237;
}
Boolean.prototype.equals = function(anotherBoolean) {
	if (this === anotherBoolean)
		return true;

	if (anotherBoolean instanceof Boolean
			|| typeof anotherBoolean === "boolean") {
		if (this.valueOf() === anotherBoolean.valueOf())
			return true;
	}
	return false;
}
Array.prototype.hashCode = function() {
	var hash = 0;
	for (var i = 0; i < this.length; i++) {
		switch (typeof this[i]) {
		case "string":
			hash += this[i].hashCode();
			break;
		case "number":
			hash += new Number(this[i]).hashCode();
			break;
		case "boolean":
			hash += this[i].hashCode();
			break;
		case "object":
			hash += this[i].hashCode();
		}
	}
	return hash;// not good!
}
Array.prototype.equals = function(anotherArray) {
	if (this === anotherArray) {
		return true;
	}
	// define find method
	var findStr = function(arr) {
		var count = 0;
		var result = [];
		if (arr instanceof Array) {
			for (var i = 0; i < arr.length; i++) {
				if (typeof arr[i] === "string" || arr[i] instanceof String) {
					result[count] = arr[i];
					count++;
				}
			}
			return result;
		}
		esle
		return null;
	}
	var findNum = function(arr) {
		var count = 0;
		var result = [];
		if (arr instanceof Array) {
			for (var i = 0; i < arr.length; i++) {
				if ((typeof arr[i] === "number" || arr[i] instanceof Number)
						&& !isNaN(arr[i])) {
					result[count] = arr[i];
					count++;
				}
			}
			return result;
		}
		esle
		return null;
	}
	var findBoolean = function(arr) {
		var count = 0;
		var result = [];
		if (arr instanceof Array) {
			for (var i = 0; i < arr.length; i++) {
				if (typeof arr[i] === "boolean" || arr[i] instanceof Boolean) {
					result[count] = arr[i];
					count++;
				}
			}
			return result;
		}
		esle
		return null;
	}
	var findArray = function(arr) {
		var count = 0;
		var result = [];
		if (arr instanceof Array) {
			for (var i = 0; i < arr.length; i++) {
				if (arr[i] instanceof Array) {
					result[count] = arr[i];
					count++;
				}
			}
			return result;
		}
		esle
		return null;
	}
	var findObject = function(arr) {
		var count = 0;
		var result = [];
		if (arr instanceof Array) {
			for (var i = 0; i < arr.length; i++) {
				if (typeof arr[i] === "object"
						&& !(arr[i] instanceof Array)
						&& !(arr[i] instanceof Boolean)
						&& !(arr[i] instanceof Number && !(arr[i] instanceof String))) {
					result[count] = arr[i];
					count++;
				}
			}
			return result;
		}
		esle
		return null;
	}
	// begin to check equality
	if (anotherArray instanceof Array) {

		if (this.length != anotherArray.length)
			return false;
		// string
		var this_StrArray = findStr(this).sort();
		var another_StrArray = findStr(anotherArray).sort();
		if (this_StrArray.length != another_StrArray.length)
			return false;
		for (var i = 0; i < this_StrArray.length; i++)
			if (!this_StrArray[i].equals(another_StrArray[i]))
				return false;
		// number
		var this_NumArray = findNum(this).sort();
		var another_NumArray = findNum(anotherArray).sort();
		if (this_NumArray.length != another_NumArray.length)
			return false;
		for (var i = 0; i < this_NumArray.length; i++)
			if (!this_NumArray[i].equals(another_NumArray[i]))
				return false;
		// boolean
		var this_BooleanArray = findBoolean(this).sort();
		var another_BooleanArray = findBoolean(anotherArray).sort();
		if (this_BooleanArray.length != another_BooleanArray.length)
			return false;
		for (var i = 0; i < this_BooleanArray.length; i++) {
			if (!this_BooleanArray[i].equals(another_BooleanArray[i]))
				return false;
		}
		// object
		var this_Object = findObject(this);
		var another_Object = findObject(anotherArray);
		if (this_Object.length != another_Object.length)
			return false;
		for (var i = 0; i < this_Object.length; i++) {
			for (var j = 0; j < another_Object.length; j++) {
				if (!this_Object[i].equals(another_Object[j]))
					continue;
				else
					// success++;
					break;
			}
			// alert(j);
			if (j >= another_Object.length)
				return false;
		}
		// array
		var this_Array = findArray(this);
		var another_Array = findArray(anotherArray);
		if (this_Array.length != another_Array.length)
			return false;
		// var success=0;
		for (var i = 0; i < this_Array.length; i++) {
			for (var j = 0; j < another_Array.length; j++) {
				if (!this_Array[i].equals(another_Array[j]))
					continue;
				else
					// success++;
					break;
			}
			// alert(j);
			if (j >= another_Array.length)
				return false;
		}
		/*
		 * if(success<this_Array.length) return false;
		 */

		return true;
	}
	return false;
}

var AUITrim = function(v) {
	return v.replace(/(^\s+)|(\s+$)/g, "");
}

var AUIMap = function() {
	var table = [ "" ];
	this.set = function(k, v) {
		if (k === null)
			return putForNullKey(v);
		for (var i = 1; i < table.length; i++) {
			if (table[i].getKey().equals(k))
				return table[i].setValue(v);
		}
		addEntry(k, v, table.length);
		return null;
	}
	// set all of "key:k,value:v" pairs such as
	// "setAll([{key:1,value:"a"},{key:2,value:45},{key:-123,value:"hello"}]); "
	this.setAll = function(arr) {
		for (var i = 0; i < arr.length; i++) {
			var key = arr[i].key;
			var value = arr[i].value;
			this.set(key, value);
		}

	}
	this.get = function(k) {
		if (k === null)
			return getForNullKey();
		for (var i = 1; i < table.length; i++) {
			if (table[i].getKey().equals(k))
				return table[i].getValue();
		}
		return null;
	}
	// remove the entry who's key equals parameter
	this.remove = function(key) {
		if (key === null)
			if (table[0] instanceof Entry) {
				var temp = table[0];
				table[0] = "";
				return temp.value;
			} else
				return null;
		for (var i = 1; i < table.length; i++) {
			if (table[i].getKey().equals(key)) {
				return table.splice(i, 1)[0].getValue();
			}
		}
		return null;
	}
	// return all of entry this map contains with form "[key1:v1];[key2:v2]"
	this.toString = function() {
		var str = "";
		if (table[0] instanceof Entry)
			str += table[0].toString() + ";";
		for (var i = 1; i < table.length; i++)
			str += table[i].toString() + ";";
		return str.substring(0, str.lastIndexOf(";"));
	}
	// check the map is empty or not!
	this.isEmpty = function() {
		if (table[0] instanceof Entry)
			return false;
		else
			return table[1] instanceof Entry ? false : true;

	}
	// empty the map
	this.clear = function() {
		table.length = 1;
		table[0] = "";
	}
	// check the map contains this key or not
	this.containsKey = function(key) {
		if (key === null)
			return table[0] instanceof Entry ? true : false;
		for (var i = 1; i < table.length; i++) {
			if (table[i].getKey().equals(key))
				return true;
		}
		return false;
	}
	// inner method,not invoked by user
	var getForNullKey = function() {
		if (table[0] instanceof Entry)
			return table[0].getValue();
		else
			return null;
	}
	// inner method,not invoked by user
	var putForNullKey = function(value) {
		if (table[0] instanceof Entry)
			return table[0].setValue(value);
		addEntry(null, value, 0);
		return null;
	}
	// inner method,not invoked by user
	var addEntry = function(key, value, bucketIndex) {
		table[bucketIndex] = new Entry(key, value);
	}
	// iterate the map keys collection,pass the "fun" parameter one key every
	// time
	this.each = function(fun) {
		if (typeof fun == "function") {
			var keys = this.keySet();
			for (var i = 0; i < keys.length; i++)
				fun(keys[i]);
		}
	}
	// return the map's length
	this.size = function() {
		if (table[0] instanceof Entry)
			return table.length;
		else
			return table.length - 1;
	}
	// return key collection this map contains
	this.keySet = function() {
		var keys = [];
		if (this.containsKey(null))
			keys[0] = null;
		for (var i = 1; i < table.length; i++)
			keys[i] = table[i].getKey();
		if (keys[0] === null)
			return keys;
		else
			return keys.slice(1);
	}

	// Entry inner class
	var Entry = function(key, value) {
		var key = key;

		var value = value;
		this.getKey = function() {
			return key;
		}
		this.getValue = function() {
			return value;
		}
		this.setValue = function(v) {
			var oldvalue = value;
			value = v;
			return oldvalue;
		}
		this.toString = function() {
			return "[" + key + ":" + value + "]";
		}
	}
}

// AUISet class
var AUISet = function() {
	var map = new AUIMap();
	var obj = {};
	// if this set did not already contain the specified element,return true
	this.add = function(o) {
		return map.set(o, obj) == null;
	}
	// check the set contains this element or not
	this.contains = function(o) {
		return map.containsKey(o);
	}
	// iterate set element,pass parameter function the element containd in the
	// set
	this.each = function(fun) {
		map.each(fun);
	}
	// empty the set
	this.clear = function() {
		map.clear();
	}
	// return set's length
	this.size = function() {
		return map.size();
	}
	// if the set contained the specified element,return true;
	this.remove = function(o) {
		return map.remove(o) === obj;
	}
	// check the set is empty or not
	this.isEmpty = function() {
		return map.isEmpty();
	}
	// return all of element with form "v1,:v2"
	this.toString = function() {
		var str = "";
		var arr = map.keySet();
		for (var i = 0; i < arr.length; i++)
			str += arr[i] + ",";
		return str.substring(0, str.lastIndexOf(","));
	}
}

/*
 * var map=new AUIMap(); map.set(-123,"cccc");
 * map.setAll([{key:1,value:"a"},{key:2,value:45},{key:-123,value:"hello"}]);
 */