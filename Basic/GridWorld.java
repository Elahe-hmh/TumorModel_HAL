package ElaheRPTSimulation;

import HAL.GridsAndAgents.AgentGrid2D;
import HAL.GridsAndAgents.AgentSQ2Dunstackable;
import HAL.Gui.GridWindow;
import HAL.Rand;
import HAL.Util;

class AgentWorld extends AgentSQ2Dunstackable<GridWorld> {

    public void StepCell(double dieProb, double divProb){
        if(G.rn.Double()<dieProb){
            //cell dies
            Dispose();
            return;
        }
        if(G.rn.Double()<divProb){
            //cell divides. So we need a neighborhood check.
            int options=MapEmptyHood(G.divHood);   //Stores empty positions in the neighborhood we checked, into the beginning of divHood
            if (options>0){
                G.NewAgentSQ(G.divHood[G.rn.Int(options)]);  //So divHood[0:options] is the empty squares in the neighborhood.
            }

        }

    }

}



public class GridWorld extends AgentGrid2D<AgentWorld> {
    Rand rn=new Rand();
    int[]divHood= Util.VonNeumannHood(false);   //neighborhood check for the division. (1,0)(0,1)(-1,0)(0,-1)


    public GridWorld(int x, int y ) {
        super(x, y, AgentWorld.class);
    }
    public void StepCells(double dieProb, double divProb){
        for(AgentWorld cell:this){
            cell.StepCell(dieProb,divProb);
        }
    }
    public void DrawModel(GridWindow win){
        for (int i=0;i<length; i++){
            int color =Util.BLACK;
            if(GetAgent(i)!=null){
                color=Util.BLUE;
            }
            win.SetPix(i,color);
        }
    }
    public static void main(String[]args){
        //model parameters
        int x=400;  //# of pixels in simulation
        int y=400;
        int timesteps=1000;
        double dieProb=0.1;
        double divProb=0.3;
        GridWindow win=new GridWindow(x, y,2);
        GridWorld model=new GridWorld(x, y);

        //initializing the model
        model.NewAgentSQ(model.xDim/2, model.yDim/2);

        for (int i=0; i<timesteps; i++) {
            win.TickPause(50);
            //model step
            model.StepCells(dieProb,divProb);
            //draw
            model.DrawModel(win);

        }

    }
}
