package com.ibs.fixmtoacris.model;
import java.io.Serializable;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FixmData  implements Serializable {
	private static final long serialVersionUID = 1L;
	private String fixmXml;
    public String getFixmXml() {
        return fixmXml;
    }
    public void setFixmXml(String fixmXml) {
        this.fixmXml = fixmXml;
    }	

	
}

