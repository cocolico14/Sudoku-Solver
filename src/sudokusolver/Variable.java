/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package sudokusolver;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author soheilchangizi
 */
public class Variable {
    
    private int value;
    private int region;
    private ArrayList<Integer> domain;

    public Variable(int value) {
        this.value = value;
        this.domain = new ArrayList<>();
    }

    public ArrayList<Integer> getDomain() {
        return domain;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setDomain(ArrayList<Integer> domain) {
        this.domain = domain;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.value;
        hash = 31 * hash + this.region;
        hash = 31 * hash + Objects.hashCode(this.domain);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Variable other = (Variable) obj;
        if (this.value != other.value) {
            return false;
        }
        if (this.region != other.region) {
            return false;
        }
        if (!Objects.equals(this.domain, other.domain)) {
            return false;
        }
        return true;
    }
    
    
    
}
