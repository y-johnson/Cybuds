package edu.coms309.cybuds;
/* commented out because of error with Mockito import
import static org.mockito.Mockito.when;

import android.widget.Scroller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import edu.coms309.cybuds.ui.AdapterRecyclerView;
import edu.coms309.cybuds.ui.RVInterface;

@RunWith(MockitoJUnitRunner.class)
public class RecyclerViewTest {

    @InjectMocks
    AdapterRecyclerView arv = new AdapterRecyclerView(this, titles);

    @Mock
    RVInterface rvI;

    @Test
    public void testAdd(){
        when(rvI.getItemCount()).thenReturn(arv.titles.size());

        Assert.assertEquals(arv.getItemCount(),6,0);
    }
}
*/