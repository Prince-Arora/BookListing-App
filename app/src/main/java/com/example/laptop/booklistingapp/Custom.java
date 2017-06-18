package com.example.laptop.booklistingapp;
public class Custom {

    private String Title="";
    private String Author="";
    private String Publisher="";
    String descc="";
    public Custom(String a,String b,String c,String d)
    {
        Title=a;
        Author=b;
        Publisher=c;
        descc=d;

    }
    public String getTitle()
    {
        return Title;
    }
    public String getAuthor()
    {
        return Author;
    }
    public String getPublisher(){ return Publisher;}
    public String getDescc(){return descc; }

}
