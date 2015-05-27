
/**
 * Processes have randomly and evenly distributed sizes of 5, 11, 17 and 31 MB.
 * With durations of 1,2,3,4,5 seconds.
 *
 * @author
 *
 */

public class Process
{
    String name;
    int size;
    int duration;

    Process(String name, int size, int duration)
    {
        this.name = name;
        this.size = size;
        this.duration = duration;
    }
}
