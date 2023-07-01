package stijn.dev.datasource.importing.xml.interfaces;

import nu.xom.*;

public interface IElementReader {
    default String readElement(Element element){
        if(element!=null){
            return element.getValue();
        } else return "";
    }
}
