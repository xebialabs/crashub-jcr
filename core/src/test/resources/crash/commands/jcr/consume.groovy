import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.Pipe;
import javax.jcr.Node;

public class consume {
  @Command
  @Usage("collects and log a set of nodes")
  public Pipe<Node, String> main() {
    return new Pipe<Node, String>() {
      @Override
      void provide(Node element) {
        Node node = (Node)element;
        out.println(node.path);
      }
    }
  }
}