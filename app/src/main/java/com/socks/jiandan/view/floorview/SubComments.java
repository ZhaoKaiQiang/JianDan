/**
 * 
 */
package com.socks.jiandan.view.floorview;

import com.socks.jiandan.model.Commentator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName: 	SubComments
 * @Description:TODO
 * @author 	JohnnyShieh
 * @date	Feb 20, 2014		10:03:22 AM
 */
public class SubComments {

    private List <Commentator> list ;
    
    public SubComments ( List < Commentator > cmts ) {
        if ( cmts != null ){
            list = new ArrayList < Commentator > ( cmts ) ;
        } else {
            list = null ;
        }
    }
    
    public int size () {
        return list == null ? 0 : list.size () ;
    }
    
    public int getFloorNum () {
        return list.get ( list.size() - 1 ).getFloorNum () ;
    }
    
    public Commentator get ( int index ) {
        return list.get ( index ) ;
    }
    
    public Iterator < Commentator > iterator () {
        return list == null ? null : list.iterator () ;
    }
}
