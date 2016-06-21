package com.jgonfer.pwned.model.realm;

import io.realm.RealmObject;

/**
 * Created by jgonfer on 14/6/16.
 */
public class RealmString extends RealmObject {
    public String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}