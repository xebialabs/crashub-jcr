import javax.jcr.Node

import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.cli.Man
import org.crsh.jcr.command.Path
import org.crsh.cli.Argument
import org.crsh.command.Pipe;

public class mv extends org.crsh.jcr.command.JCRCommand {

  @Command
  @Usage("move a node")
  @Man("""\
The mv command can move a node to a target location in the JCR tree. It can be used also to
rename a node. The mv command is a <Node,Node> command consuming a stream of node to move
them and producing nodes that were moved.

[/registry]% mv Registry Registry2""")


  public Pipe<Node, Node> main(
    @Argument @Usage("the source paths") @Man("The path of the source nodes to move, absolute or relative") List<Path> sources,
    @Argument @Usage("the destination path") @Man("The destination path absolute or relative") Path destination) {
    assertConnected()

    // Resolve JCR session
    def session = session;
    return new Pipe<Node, Node>() {
      @Override
      void open() {
        sources.each { src ->
          def srcNode = findNodeByPath(src);
          def dstPath = absolutePath(destination);
          srcNode.session.workspace.move(srcNode.path, dstPath.value);
          def targetNode = findNodeByPath(dstPath);
          context.provide(targetNode);
        }
      }
      @Override
      void provide(Node src) {
        def dstPath = absolutePath(destination);
        def dstParent = findNodeByPath(dstPath);
        def targetPath = dstParent.path + "/" + src.name;
        session.workspace.move(src.path, targetPath);
        context.provide(src);
      }
    }
  }
}