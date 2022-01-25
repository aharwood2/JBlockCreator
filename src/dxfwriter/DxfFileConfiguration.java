package dxfwriter;

import java.util.List;

public class DxfFileConfiguration
{
    private String timestamp;
    private List<String> layers;

    public DxfFileConfiguration(String timeStamp, List<String> outputChecks)
    {
        this.timestamp = timeStamp;
        this.layers = outputChecks;
    }

    public String getTimeStamp()
    {
        return timestamp;
    }

    public List<String> getLayers()
    {
        return layers;
    }
}
