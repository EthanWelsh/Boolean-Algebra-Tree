import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;

//a graphical component for displaying the contents of a binary tree.
//
//sample usage: for us this will be in the constriuctor for an Expression.
// Node root = build_tree() //based on stringexpression = "(A v B)"
// TreeDisplay display = new TreeDisplay(stringexpression);
// display.setRoot(root);
// 

class TreeDisplay extends JComponent
{
  //number of pixels between text and edge
  private static final int ARC_PAD = 2;
  
  //the tree being displayed
  private Node root = null;
  
  
  //creates a frame with a new TreeDisplay component.
  //(constructor returns the TreeDisplay component--not the frame).
  public TreeDisplay(String title)
  {
    //create surrounding frame
    JFrame frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //add the TreeDisplay component to the frame
    frame.getContentPane().add(this);
    
    //show frame
    frame.pack();
    frame.setVisible(true);
  }
  
  //tells the frame the default size of the tree
  public Dimension getPreferredSize()
  {
    return new Dimension(400, 300);
  }
  
  
  //called whenever the TreeDisplay must be drawn on the screen
  public void paint(Graphics g)
  {
    Graphics2D g2 = (Graphics2D)g;
    Dimension d = getSize();
    
    //draw white background
    g2.setPaint(Color.white);
    g2.fill(new Rectangle2D.Double(0, 0, d.width, d.height));
    
    int depth = h();
    
    if (root == null)
      //no tree to draw
      return;
    
    //hack to avoid division by zero, if only one level in tree
    if (depth == 1)
      depth = 2;
    
    //compute the size of the text
    FontMetrics font = g2.getFontMetrics();
    Node leftmost = root;
    while (leftmost.left != null)
      leftmost = leftmost.left;
    Node rightmost = root;
    while (rightmost.right != null)
      rightmost = rightmost.right;
    int leftPad = font.stringWidth(leftmost.symbol + "") / 2;
    int rightPad = font.stringWidth(rightmost.symbol + "") / 2;
    int textHeight = font.getHeight();
    
    //draw the actual tree
    drawTree(g2, root, leftPad + ARC_PAD,
             d.width - rightPad - ARC_PAD,
             textHeight / 2 + ARC_PAD,
             (d.height - textHeight - 2 * ARC_PAD) / (depth - 1));
    
   
  }
  
  //draws the tree, starting from the given node, in the region with x values ranging
  //from minX to maxX, with y value beginning at y, and next level at y + yIncr.
  private void drawTree(Graphics2D g2, Node t, int minX, int maxX, int y, int yIncr)
  {
    //skip if empty
    if (t == null)
      return;
    
    //compute useful coordinates
    int x = (minX + maxX) / 2;
    int nextY = y + yIncr;
    
    //draw black lines
    g2.setPaint(Color.black);
    if (t.left != null)
    {
      int nextX = (minX + x) / 2;
      g2.draw(new Line2D.Double(x, y, nextX, nextY));
    }
    if (t.right != null)
    {
      int nextX = (x + maxX) / 2;
      g2.draw(new Line2D.Double(x, y, nextX, nextY));
    }
    
    //measure text
    FontMetrics font = g2.getFontMetrics();
    String text = t.symbol + "";
    int textHeight = font.getHeight();
    int textWidth = font.stringWidth(text);
    
    //draw the box around the node
    Rectangle2D.Double box = new Rectangle2D.Double(x - textWidth / 2 - ARC_PAD, y - textHeight / 2 - ARC_PAD,
                                                    textWidth + 2 * ARC_PAD, textHeight + 2 * ARC_PAD);
    Color c = new Color(187, 224, 227);
    g2.setPaint(c);
    g2.fill(box);
    //draw black border
    g2.setPaint(Color.black);
    g2.draw(box);
    
    //draw text
    g2.drawString(text, x - textWidth / 2, y + textHeight / 2);
    
    //draw children
    drawTree(g2, t.left, minX, x, nextY, yIncr);
    drawTree(g2, t.right, x, maxX, nextY, yIncr);
  }
  
  //tells the component to switch to displaying the given tree
  public void setRoot(Node root)
  {
    this.root = root;
    
    //signal that the display needs to be redrawn
    repaint();
  }
  
  private int h()
  {
    Stack<Node> nodeStack = new Stack<Node>();
    Stack<Integer> leftStack = new Stack<Integer>();
    Stack<Integer> rightStack = new Stack<Integer>();
    
    nodeStack.push(root);
    leftStack.push(-1);
    rightStack.push(-1);
    
    while (true)
    {
      Node t = nodeStack.peek();
      int left = leftStack.peek();
      int right = rightStack.peek();
      
      if (t == null)
      {
        nodeStack.pop();
        leftStack.pop();
        rightStack.pop();
        int value = 0;
        if (nodeStack.isEmpty())
          return value;
        else if (leftStack.peek() == -1)
        {
          leftStack.pop();
          leftStack.push(value);
        }
        else
        {
          rightStack.pop();
          rightStack.push(value);
        }
      }
      else if (left == -1)
      {
        nodeStack.push(t.left);
        leftStack.push(-1);
        rightStack.push(-1);
      }
      else if (right == -1)
      {
        nodeStack.push(t.right);
        leftStack.push(-1);
        rightStack.push(-1);
      }
      else
      {
        nodeStack.pop();
        leftStack.pop();
        rightStack.pop();
        int value = 1 + Math.max(left, right);
        if (nodeStack.isEmpty())
          return value;
        else if (leftStack.peek() == -1)
        {
          leftStack.pop();
          leftStack.push(value);
        }
        else
        {
          rightStack.pop();
          rightStack.push(value);
        }
      }
    }
  }
}