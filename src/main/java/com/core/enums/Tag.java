/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.enums;

/**
 *
 * @author gonzalo
 */
public enum Tag {
    
    Header1("<h1>$</h1>"),
    Header2("<h2>$</h2>"),
    Header3("<h3>$</h3>"),
    Header4("<h4>$</h4>"),
    Paragraph("<p>$</p>"),
    Code("<pre><code>$</code></pre>"),
    Image("<img src='$' />"),
    Link("<a href='$'></a>")
    ;
    
    private final String value;
    
    private Tag(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
    
    public String Name(){
        return this.name();
    }
    
}
