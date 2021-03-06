/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package husi.recuperapp.caminatas;


/**
 * Calculates and displays the distance walked.  
 * @author Levente Bagi
 */
public class DistanceNotifier implements StepListener{

    public interface Listener {
        void valueChanged(float value);
        void passValue();
    }
    private Listener mListener;
    
    float mDistance = 0;
    
    PedometerSettings mSettings;
    float mStepLength;

    public DistanceNotifier(Listener listener, PedometerSettings settings) {
        mListener = listener;
        mSettings = settings;
        reloadSettings();
    }
    public void setDistance(float distance) {
        mDistance = distance;
        notifyListener();
    }
    
    public void reloadSettings() {
        mStepLength = mSettings.getStepLength();
        notifyListener();
    }
    
    public void onStep() {
            mDistance += (float)(// Distancia en metros
                mStepLength // centimetros
                / 100.0); // centimetros/metros
        notifyListener();
    }
    
    private void notifyListener() {
        mListener.valueChanged(mDistance);
    }
    
    public void passValue() {
        // Callback of StepListener - Not implemented
    }
    

}

