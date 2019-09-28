/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.DB;

/**
 *
 * @author user
 */
public class StatusElement {
    private boolean isNew = false;
    private boolean isRecent = false;

    public StatusElement() {
    }

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isIsRecent() {
        return isRecent;
    }

    public void setIsRecent(boolean isRecent) {
        this.isRecent = isRecent;
    }

    @Override
    public String toString() {
        return "StatusElement{" + "isNew=" + isNew + ", isRecent=" + isRecent + '}';
    }
}





























