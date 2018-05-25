
import java.util.*;
import java.util.zip.*;
import java.util.List;
import java.util.regex.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.table.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.*;
import java.lang.ref.*;
import java.lang.management.*;
import java.security.*;
import java.security.spec.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.math.*;
import java.awt.datatransfer.StringSelection;
import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.swing.undo.UndoManager;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.skin.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.Timer;
import java.text.SimpleDateFormat;
import java.text.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.geom.*;
import java.awt.font.GlyphVector;

public class main {

  public static class Drawing extends Concept {

    public static String _fieldOrder = "globalID name calStructure";

    public String globalID = aGlobalID();

    public String name;

    public String calStructure;
  }

  public static CirclesAndLines cal = new CirclesAndLines();

  public static Canvas canvas;

  public static Drawing drawing;

  public static JLabel lblGlobalID;

  public static boolean autoVis;

  public static void main(final String[] args) throws Exception {
    autoRestart();
    useDBOf("#1007609");
    db();
    fixGlobalIDs();
    substance();
    {
      swing(new Runnable() {

        public void run() {
          try {
            fixCAL();
            canvas = cal.show(1000, 600);
            makeMenu();
            addToWindowNorth(canvas, withMargin(jcenteredline(jbutton("New drawing", "newDrawing"), lblGlobalID = jlabel(), jbutton("New circle", "newCircle"))));
            addToWindow(canvas, withMargin(jcenteredline(jbutton("Save drawing", "saveDrawing"), jbutton("Save drawing as...", "saveDrawingAs"), jbutton("PUBLISH", "publish"))));
            addDrawingsList();
            centerFrame(canvas);
            hideConsole();
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "fixCAL();\n    canvas = cal.show(1000, 600);\n    makeMenu();\n    addToWindowNo...";
        }
      });
    }
    bot("Circles Editor.");
    sleepIfMain();
  }

  public static void newCircle() {
    final JTextField text = jtextfield();
    showFormTitled("New Circle", "Text", text, runnableThread(new Runnable() {

      public void run() {
        try {
          {
            JWindow _loading_window = showLoadingAnimation();
            try {
              String theText = getTextTrim(text);
              newCircle(theText);
            } finally {
              disposeWindow(_loading_window);
            }
          }
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "{ JWindow _loading_window = showLoadingAnimation(); try {\n    String theText ...";
      }
    }));
  }

  public static void newCircle(String theText) {
    double x = random(0.2, 0.8), y = random(0.2, 0.8);
    if (autoVis)
      cal.circle_autoVis(theText, x, y);
    else
      cal.circle(theText, x, y);
    canvas.update();
  }

  public static Drawing saveDrawing() {
    return saveDrawing(drawing != null ? drawing.name : "");
  }

  public static Drawing saveDrawing(String name) {
    if (empty(name)) {
      final JTextField tfName = jtextfield(makeUpName());
      showFormTitled("Name drawing", "Name", tfName, new Runnable() {

        public void run() {
          try {
            saveDrawing(or2(getTextTrim(tfName), "Untitled"));
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "saveDrawing(or2(getTextTrim(tfName), \"Untitled\"));";
        }
      });
      return null;
    }
    cal.title = name;
    if (drawing == null)
      drawing = cnew(Drawing.class);
    cal.globalID = drawing.globalID;
    String s = cal_structure(cal);
    cset(drawing, "calStructure", s, "name", name);
    setDrawing(drawing);
    return drawing;
  }

  public static void newDrawing() {
    setCAL(new CirclesAndLines());
    setDrawing(null);
    newCircle();
  }

  public static void makeMenu() {
    addMenu(canvas, "Menu", "Upload all drawings!", "uploadAll2");
  }

  public static String drawingName(Drawing d) {
    return d == null ? "" : "[" + d.id + "] " + (nempty(d.name) ? quote(d.name) + " " : "");
  }

  public static List makeDrawingsTable() {
    List l = new ArrayList();
    for (final Drawing d : reversed(list(Drawing.class))) {
      CirclesAndLines cal = cal_unstructure(d.calStructure);
      l.add(litorderedmap("Drawing", drawingName(d), "Circles + Relations", join("|", concatLists(collect(cal.circles, "text"), collectTreeSet(cal.lines, "text")))));
    }
    return l;
  }

  public static Drawing drawingFromTable(JTable table, int row) {
    return getConcept(Drawing.class, parseFirstLong((String) getTableCell(table, row, 0)));
  }

  public static void addDrawingsList() {
    final JTable table = sexyTableWithoutDrag();
    Object load = new VF1<Integer>() {

      public void get(Integer row) {
        try {
          loadDrawing(drawingFromTable(table, row));
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "loadDrawing(drawingFromTable(table, row))";
      }
    };
    onDoubleClick(table, load);
    tablePopupMenu(table, new VF2<JPopupMenu, Integer>() {

      public void get(JPopupMenu menu, final Integer row) {
        try {
          final Drawing d = drawingFromTable(table, row);
          addMenuItem(menu, "ID: " + d.globalID, new Runnable() {

            public void run() {
              try {
                copyTextToClipboard(d.globalID);
              } catch (Exception __e) {
                throw rethrow(__e);
              }
            }

            public String toString() {
              return "copyTextToClipboard(d.globalID)";
            }
          });
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "final Drawing d = drawingFromTable(table, row);\n    addMenuItem(menu, \"ID: \" ...";
      }
    });
    tablePopupMenuItem(table, "Rename...", new VF1<Integer>() {

      public void get(Integer row) {
        try {
          final Drawing d = drawingFromTable(table, row);
          final JTextField tf = jtextfield(d.name);
          showFormTitled("Rename Drawing", "Old name", jlabel(d.name), "New name", tf, new Runnable() {

            public void run() {
              try {
                cset(d, "name", getTextTrim(tf));
                setDrawing(d);
              } catch (Exception __e) {
                throw rethrow(__e);
              }
            }

            public String toString() {
              return "cset(d, \"name\" , getTextTrim(tf));\n        setDrawing(d);";
            }
          });
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "final Drawing d = drawingFromTable(table, row);\n    final JTextField tf = jte...";
      }
    });
    tablePopupMenuItem(table, "Show structure", new VF1<Integer>() {

      public void get(Integer row) {
        try {
          final Drawing d = drawingFromTable(table, row);
          showWrappedText("Structure of drawing", cal_simplifiedStructure(d.calStructure));
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "final Drawing d = drawingFromTable(table, row);\n    showWrappedText(\"Structur...";
      }
    });
    tablePopupMenuItem(table, "Copy structure to clipboard", new VF1<Integer>() {

      public void get(Integer row) {
        try {
          final Drawing d = drawingFromTable(table, row);
          copyTextToClipboard(cal_simplifiedStructure(d.calStructure));
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "final Drawing d = drawingFromTable(table, row);\n    copyTextToClipboard(cal_s...";
      }
    });
    tablePopupMenuItem(table, "Delete", new VF1<Integer>() {

      public void get(Integer row) {
        try {
          final Drawing d = drawingFromTable(table, row);
          removeConcept(d);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "final Drawing d = drawingFromTable(table, row);\n    removeConcept(d);";
      }
    });
    addToWindowSplitRight_aggressive(canvas, tableWithSearcher(table), 0.7f);
    awtCalcOnConceptsChange(table, new Runnable() {

      public void run() {
        try {
          dataToTable(table, makeDrawingsTable());
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "dataToTable(table, makeDrawingsTable())";
      }
    });
  }

  public static void fixCAL() {
    cal.imageForUserMadeNodes = whiteImage(10, 10);
  }

  public static void setCAL(CirclesAndLines cal) {
    main.cal = cal;
    fixCAL();
    Canvas c = canvas;
    canvas = cal.makeCanvas();
    awtReplaceComponent(c, canvas);
  }

  public static void setDrawing(Drawing d) {
    drawing = d;
    setText(lblGlobalID, drawing != null ? "Drawing ID: " + drawing.globalID : "");
    setFrameTitle(canvas, trim(drawingName(d)) + " - " + programName());
  }

  public static void loadDrawing(Drawing d) {
    setDrawing(d);
    CirclesAndLines cal = (CirclesAndLines) (unstructure(d.calStructure));
    setCAL(cal);
  }

  public static void publish() {
    final Drawing d = saveDrawing();
    if (d == null)
      return;
    {
      Thread _t_0 = new Thread() {

        public void run() {
          try {
            {
              JWindow _loading_window = showLoadingAnimation();
              try {
                String fullName = "Drawing " + d.globalID + " " + renderDate(now()) + " - " + or2(d.name, "Untitled");
                String url = uploadToImageServer(canvas.getImage(), fullName);
                infoBox("Image uploaded to server! " + url);
                String id = createSuperHighSnippet(structure(d), fullName, 56, null, null);
                infoBox("Drawing uploaded. " + fsI(id));
              } finally {
                disposeWindow(_loading_window);
              }
            }
          } catch (Throwable __e) {
            printStackTrace2(__e);
          }
        }
      };
      startThread(_t_0);
    }
  }

  public static String makeUpName() {
    Circle c = first(cal.circles);
    return c == null ? "" : c.text;
  }

  public static void saveDrawingAs() {
    drawing = cnew(Drawing.class);
    saveDrawing();
  }

  public static void uploadAll() {
    fixGlobalIDs();
    run("#1009985");
  }

  public static void uploadAll2() {
    runInNewThread_awt("#1010485");
  }

  public static String answer(String s) {
    final Matches m = new Matches();
    if (match("new diagram with node *", s, m)) {
      {
        swing(new Runnable() {

          public void run() {
            try {
              activateFrame(canvas);
              newDrawing();
              if (nempty(m.unq(0)))
                newCircle(m.unq(0));
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "activateFrame(canvas);\n      newDrawing();\n      if (nempty(m.unq(0))) newCir...";
          }
        });
      }
      return "OK";
    }
    return null;
  }

  public static void fixGlobalIDs() {
    for (Drawing d : list(Drawing.class)) try {
      if (!d.calStructure.contains(d.globalID)) {
        print("Fixing global ID: " + d.globalID);
        CirclesAndLines cal = cal_unstructure(d.calStructure);
        cal.globalID = d.globalID;
        cset(d, "calStructure", cal_structure(cal));
      }
    } catch (Throwable __e) {
      printStackTrace2(__e);
    }
  }

  public static String aGlobalID() {
    return randomID(16);
  }

  public static LinkedHashMap litorderedmap(Object... x) {
    LinkedHashMap map = new LinkedHashMap();
    litmap_impl(map, x);
    return map;
  }

  public static long parseFirstLong(String s) {
    return parseLong(jextract("<int>", s));
  }

  public static String cal_simplifiedStructure(CirclesAndLines cal) {
    return cal_simplifiedStructure(structure(cal));
  }

  public static String cal_simplifiedStructure(String structure) {
    CirclesAndLines cal = (CirclesAndLines) (unstructure(structure));
    for (Circle c : cal.circles) {
      c.x = roundToOneHundredth(c.x);
      c.y = roundToOneHundredth(c.y);
    }
    return cal_structure_impl(cal);
  }

  public static BufferedImage whiteImage(int w, int h) {
    return newBufferedImage(w, h, Color.white);
  }

  public static boolean activateFrame(final Component c) {
    return swing(new F0<Boolean>() {

      public Boolean get() {
        try {
          Frame f = getAWTFrame(c);
          if (f == null)
            return false;
          if (!f.isVisible())
            f.setVisible(true);
          if (f.getState() == Frame.ICONIFIED)
            f.setState(Frame.NORMAL);
          if (isWindows()) {
            boolean fullscreen = f.getExtendedState() == Frame.MAXIMIZED_BOTH;
            f.setExtendedState(JFrame.ICONIFIED);
            f.setExtendedState(fullscreen ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
          }
          f.toFront();
          return true;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "Frame f = getAWTFrame(c);\r\n    if (f == null) false;\r\n    if (!f.isVisible())...";
      }
    });
  }

  public static Class run(String progID, String... args) {
    Class main = hotwire(progID);
    callMain(main, args);
    return main;
  }

  public static boolean empty(Collection c) {
    return c == null || c.isEmpty();
  }

  public static boolean empty(String s) {
    return s == null || s.length() == 0;
  }

  public static boolean empty(Map map) {
    return map == null || map.isEmpty();
  }

  public static boolean empty(Object[] o) {
    return o == null || o.length == 0;
  }

  public static boolean empty(Object o) {
    if (o instanceof Collection)
      return empty((Collection) o);
    if (o instanceof String)
      return empty((String) o);
    if (o instanceof Map)
      return empty((Map) o);
    if (o instanceof Object[])
      return empty((Object[]) o);
    if (o instanceof byte[])
      return empty((byte[]) o);
    if (o == null)
      return true;
    throw fail("unknown type for 'empty': " + getType(o));
  }

  public static boolean empty(float[] a) {
    return a == null || a.length == 0;
  }

  public static boolean empty(int[] a) {
    return a == null || a.length == 0;
  }

  public static boolean empty(long[] a) {
    return a == null || a.length == 0;
  }

  public static boolean empty(byte[] a) {
    return a == null || a.length == 0;
  }

  public static String quote(Object o) {
    if (o == null)
      return "null";
    return quote(str(o));
  }

  public static String quote(String s) {
    if (s == null)
      return "null";
    StringBuilder out = new StringBuilder((int) (l(s) * 1.5 + 2));
    quote_impl(s, out);
    return out.toString();
  }

  public static void quote_impl(String s, StringBuilder out) {
    out.append('"');
    int l = s.length();
    for (int i = 0; i < l; i++) {
      char c = s.charAt(i);
      if (c == '\\' || c == '"')
        out.append('\\').append(c);
      else if (c == '\r')
        out.append("\\r");
      else if (c == '\n')
        out.append("\\n");
      else
        out.append(c);
    }
    out.append('"');
  }

  public static String trim(String s) {
    return s == null ? null : s.trim();
  }

  public static String trim(StringBuilder buf) {
    return buf.toString().trim();
  }

  public static String trim(StringBuffer buf) {
    return buf.toString().trim();
  }

  public static String cal_structure(CirclesAndLines cal) {
    return cal_structure_impl(restructure(cal));
  }

  public static String cal_structure_impl(CirclesAndLines cal) {
    if (cal.arrowClass == Arrow.class)
      cal.arrowClass = null;
    if (cal.circleClass == Circle.class)
      cal.circleClass = null;
    for (Circle c : cal.circles) cal_structure_simplifyElement(c);
    for (Line l : cal.lines) cal_structure_simplifyElement(l);
    return structure(cal);
  }

  public static void cal_structure_simplifyElement(Base b) {
    if (eq(b.traits, ll(b.text)))
      b.traits = null;
  }

  public static String programName() {
    return getProgramName();
  }

  public static TreeSet collectTreeSet(Collection c, String field) {
    TreeSet set = new TreeSet();
    for (Object a : c) {
      Object val = getOpt(a, field);
      if (val != null)
        set.add(val);
    }
    return set;
  }

  public static String join(String glue, Iterable<String> strings) {
    if (strings == null)
      return "";
    StringBuilder buf = new StringBuilder();
    Iterator<String> i = strings.iterator();
    if (i.hasNext()) {
      buf.append(i.next());
      while (i.hasNext()) buf.append(glue).append(i.next());
    }
    return buf.toString();
  }

  public static String join(String glue, String... strings) {
    return join(glue, Arrays.asList(strings));
  }

  public static String join(Iterable<String> strings) {
    return join("", strings);
  }

  public static String join(Iterable<String> strings, String glue) {
    return join(glue, strings);
  }

  public static String join(String[] strings) {
    return join("", strings);
  }

  public static String join(String glue, Pair p) {
    return p == null ? "" : str(p.a) + glue + str(p.b);
  }

  public static String fsI(String id) {
    return formatSnippetID(id);
  }

  public static String fsI(long id) {
    return formatSnippetID(id);
  }

  public static void awtCalcOnConceptsChange(JComponent component, int delay, final Object runnable, final boolean runOnFirstTime) {
    awtCalcOnConceptChanges(component, delay, runnable, runOnFirstTime);
  }

  public static void awtCalcOnConceptsChange(JComponent component, int delay, int firstDelay, final Object runnable, final boolean runOnFirstTime) {
    awtCalcOnConceptChanges(component, delay, firstDelay, runnable, runOnFirstTime);
  }

  public static void awtCalcOnConceptsChange(JComponent component, final Object runnable) {
    awtCalcOnConceptChanges(component, 1000, runnable, true);
  }

  public static int withMargin_defaultWidth = 6;

  public static JPanel withMargin(Component c) {
    return withMargin(withMargin_defaultWidth, c);
  }

  public static JPanel withMargin(int w, Component c) {
    return withMargin(w, w, c);
  }

  public static JPanel withMargin(final int w, final int h, final Component c) {
    return swing(new F0<JPanel>() {

      public JPanel get() {
        try {
          JPanel p = new JPanel(new BorderLayout());
          p.setBorder(BorderFactory.createEmptyBorder(h, w, h, w));
          p.add(c);
          return p;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JPanel p = new JPanel(new BorderLayout);\r\n    p.setBorder(BorderFactory.creat...";
      }
    });
  }

  public static JMenu addMenu(JComponent c, String menuName, Object... items) {
    return addMenu(getFrame(c), menuName, items);
  }

  public static JMenu addMenu(final JFrame frame, final String menuName, final Object... items) {
    return (JMenu) swing(new F0<Object>() {

      public Object get() {
        try {
          JMenuBar bar = addMenuBar(frame);
          JMenu menu = getMenuNamed(bar, menuName);
          boolean isNew = menu == null;
          if (isNew)
            menu = new JMenu(menuName);
          else
            menu.removeAll();
          fillJMenu(menu, items);
          if (isNew) {
            bar.add(menu);
            revalidateFrame(frame);
          }
          return menu;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JMenuBar bar = addMenuBar(frame);\r\n    JMenu menu = getMenuNamed(bar, menuNam...";
      }
    });
  }

  public static String uploadToImageServer(BufferedImage img, String name) {
    return uploadToImageServerIfNotThere(img, name);
  }

  public static JButton jbutton(String text, Object action) {
    return newButton(text, action);
  }

  public static JButton jbutton(String text) {
    return newButton(text, null);
  }

  public static JButton jbutton(BufferedImage img, Object action) {
    return setButtonImage(img, jbutton("", action));
  }

  public static JButton jbutton(Action action) {
    return swingNu(JButton.class, action);
  }

  public static JTextField jtextfield() {
    return jTextField();
  }

  public static JTextField jtextfield(String text) {
    return jTextField(text);
  }

  public static JTextField jtextfield(Object o) {
    return jTextField(o);
  }

  public static class tablePopupMenu_Maker {

    public List menuMakers = new ArrayList();
  }

  public static Map<JTable, tablePopupMenu_Maker> tablePopupMenu_map = new WeakHashMap();

  public static ThreadLocal<MouseEvent> tablePopupMenu_mouseEvent = new ThreadLocal();

  public static ThreadLocal<Boolean> tablePopupMenu_first = new ThreadLocal();

  public static void tablePopupMenu(final JTable table, final Object menuMaker) {
    final boolean first = isTrue(getAndClearThreadLocal(tablePopupMenu_first));
    swingNowOrLater(new Runnable() {

      public void run() {
        try {
          tablePopupMenu_Maker maker = tablePopupMenu_map.get(table);
          if (maker == null) {
            tablePopupMenu_map.put(table, maker = new tablePopupMenu_Maker());
            final tablePopupMenu_Maker _maker = maker;
            table.addMouseListener(new MouseAdapter() {

              public void mousePressed(MouseEvent e) {
                displayMenu(e);
              }

              public void mouseReleased(MouseEvent e) {
                displayMenu(e);
              }

              public void displayMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                  JPopupMenu menu = new JPopupMenu();
                  int row = table.rowAtPoint(e.getPoint());
                  if (table.getSelectedRowCount() < 2)
                    table.setRowSelectionInterval(row, row);
                  int emptyCount = menu.getComponentCount();
                  tablePopupMenu_mouseEvent.set(e);
                  for (Object menuMaker : _maker.menuMakers) pcallF(menuMaker, menu, row);
                  if (menu.getComponentCount() != emptyCount)
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
              }
            });
          }
          if (first)
            maker.menuMakers.add(0, menuMaker);
          else
            maker.menuMakers.add(menuMaker);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "tablePopupMenu_Maker maker = tablePopupMenu_map.get(table);\r\n    if (maker ==...";
      }
    });
  }

  public static String or2(String a, String b) {
    return nempty(a) ? a : b;
  }

  public static String or2(String a, String b, String c) {
    return or2(or2(a, b), c);
  }

  public static JSplitPane addToWindowSplitRight_aggressive(Component c, Component toAdd) {
    return addToWindowSplitRight_f(c, toAdd, new F1<JComponent, Object>() {

      public Object get(JComponent c) {
        try {
          return jMinWidth_pure(100, c);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "jMinWidth_pure(100, c)";
      }
    });
  }

  public static JSplitPane addToWindowSplitRight_aggressive(Component c, Component toAdd, float splitRatio) {
    return setSplitPaneLater(addToWindowSplitRight_aggressive(c, toAdd), splitRatio);
  }

  public static void sleepIfMain() {
    if (isMainProgram())
      sleep();
  }

  public static int cset(Concept c, Object... values) {
    try {
      if (c == null)
        return 0;
      int changes = 0;
      values = expandParams(c.getClass(), values);
      warnIfOddCount(values);
      for (int i = 0; i + 1 < l(values); i += 2) {
        String field = (String) values[i];
        Object value = values[i + 1];
        Field f = setOpt_findField(c.getClass(), field);
        if (value instanceof RC)
          value = c._concepts.getConcept((RC) value);
        value = deref(value);
        if (value instanceof String && l((String) value) >= concepts_internStringsLongerThan)
          value = intern((String) value);
        if (f == null) {
          mapPut2(c.fieldValues, field, value instanceof Concept ? c.new Ref((Concept) value) : value);
          c.change();
        } else if (isSubtypeOf(f.getType(), Concept.Ref.class)) {
          ((Concept.Ref) f.get(c)).set((Concept) derefRef(value));
          c.change();
          ++changes;
        } else {
          Object old = f.get(c);
          if (neq(value, old)) {
            f.set(c, value);
            if ((f.getModifiers() & java.lang.reflect.Modifier.TRANSIENT) == 0)
              c.change();
            ++changes;
          }
        }
      }
      return changes;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String createSuperHighSnippet(String text, String title, int type, String user, String pass) {
    String addURL = "http://tinybrain.de:8080/tb-int/add_snippet.php";
    Map query = litmap("type", type, "superhigh", 1, "public", 1, "text", text, "name", title, "_user", user, "_pass", pass);
    String answer = doPost(query, addURL);
    if (!isSnippetID(answer))
      throw fail(answer);
    print("Snippet created. " + formatSnippetID(answer));
    return formatSnippetID(answer);
  }

  public static <A> List<A> concatLists(Collection<A>... lists) {
    List<A> l = new ArrayList();
    for (Collection<A> list : lists) if (list != null)
      l.addAll(list);
    return l;
  }

  public static <A> List<A> concatLists(Collection<? extends Collection<A>> lists) {
    List<A> l = new ArrayList();
    for (Collection<A> list : lists) if (list != null)
      l.addAll(list);
    return l;
  }

  public static void removeConcept(long id) {
    deleteConcept(id);
  }

  public static void removeConcept(Concept c) {
    deleteConcept(c);
  }

  public static void removeConcept(Concept.Ref ref) {
    deleteConcept(ref);
  }

  public static Component addToWindow(final Component c, final Component toAdd) {
    if (toAdd != null) {
      swing(new Runnable() {

        public void run() {
          try {
            JFrame frame = getFrame(c);
            Container cp = frame.getContentPane();
            JPanel newCP = new JPanel();
            newCP.setLayout(new BorderLayout());
            newCP.add(BorderLayout.CENTER, cp);
            newCP.add(BorderLayout.SOUTH, toAdd);
            frame.setContentPane(newCP);
            frame.revalidate();
            frame.repaint();
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "JFrame frame = getFrame(c);\r\n    Container cp = frame.getContentPane();\r\n  \r\n...";
        }
      });
    }
    return c;
  }

  public static void addMenuItem(JPopupMenu menu, String text, Object action) {
    menu.add(jmenuItem(text, action));
  }

  public static void addMenuItem(JPopupMenu menu, JMenuItem menuItem) {
    menu.add(menuItem);
  }

  public static void addMenuItem(JMenu menu, String text, Object action) {
    menu.add(jmenuItem(text, action));
  }

  public static void addMenuItem(Menu menu, String text, Object action) {
    menu.add(menuItem(text, action));
  }

  public static void addMenuItem(JMenu menu, JMenuItem menuItem) {
    menu.add(menuItem);
  }

  public static JComponent tableWithSearcher(final JTable t) {
    final JTextField input = new JTextField();
    onUpdate(input, new Runnable() {

      public List lastFiltered, lastOriginal;

      public void run() {
        String pat = trim(input.getText());
        List<Map<String, Object>> data = rawTableData(t);
        if (eq(lastFiltered, data))
          data = lastOriginal;
        print("Searching " + n(l(data), "entry"));
        List data2 = new ArrayList();
        for (Map<String, Object> map : data) if (anyValueContainsIgnoreCase(map, pat))
          data2.add(map);
        print("Found " + n(l(data2), "entry"));
        lastFiltered = data2;
        lastOriginal = data;
        dataToTable(t, data2);
      }
    });
    return northAndCenter(withLabel("Search:", input), t);
  }

  public static JPanel jcenteredline(final Component... components) {
    return swing(new F0<JPanel>() {

      public JPanel get() {
        try {
          return jFullCenter(hstackWithSpacing(components));
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "jFullCenter(hstackWithSpacing(components))";
      }
    });
  }

  public static JPanel jcenteredline(List<? extends Component> components) {
    return jcenteredline(asArray(Component.class, components));
  }

  public static void hideConsole() {
    final JFrame frame = consoleFrame();
    if (frame != null) {
      autoVMExit();
      swingLater(new Runnable() {

        public void run() {
          try {
            frame.setVisible(false);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "frame.setVisible(false);";
        }
      });
    }
  }

  public static JTextArea showWrappedText(final String title, final String text) {
    return (JTextArea) swingAndWait(new F0<Object>() {

      public Object get() {
        try {
          JTextArea textArea = wrappedTextArea(text);
          textArea.setFont(typeWriterFont());
          makeFrame(title, new JScrollPane(textArea));
          return textArea;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JTextArea textArea = wrappedTextArea(text);\r\n    textArea.setFont(typeWriterF...";
      }
    });
  }

  public static JTextArea showWrappedText(Object text) {
    return showWrappedText(autoFrameTitle(), str(text));
  }

  public static List collect(Collection c, String field) {
    return collectField(c, field);
  }

  public static List collect(String field, Collection c) {
    return collectField(c, field);
  }

  public static List collect(Class c, String field) {
    return collect(list(c), field);
  }

  public static String renderDate(long timestamp) {
    return formatDate(timestamp);
  }

  public static CirclesAndLines cal_unstructure(String s) {
    return (CirclesAndLines) unstructure(s);
  }

  public static Object getTableCell(JTable tbl, int row, int col) {
    if (row >= 0 && row < tbl.getModel().getRowCount())
      return tbl.getModel().getValueAt(row, col);
    return null;
  }

  public static Android3 bot(String greeting) {
    return makeAndroid3(greeting);
  }

  public static Android3 bot(Android3 a) {
    return makeBot(a);
  }

  public static Android3 bot(String greeting, Object responder) {
    return makeBot(greeting, responder);
  }

  public static Android3 bot() {
    return makeBot();
  }

  public static <A> A setFrameTitle(A c, String title) {
    Frame f = getAWTFrame(c);
    if (f == null)
      showFrame(title, c);
    else
      f.setTitle(title);
    return c;
  }

  public static <A extends Component> A setFrameTitle(String title, A c) {
    return setFrameTitle(c, title);
  }

  public static JFrame setFrameTitle(String title) {
    Object f = getOpt(mc(), "frame");
    if (f instanceof JFrame)
      return setFrameTitle((JFrame) f, title);
    return null;
  }

  public static JTable dataToTable(Object data) {
    return dataToTable(showTable(), data);
  }

  public static JTable dataToTable(Object data, String title) {
    return dataToTable(showTable(title), data);
  }

  public static JTable dataToTable(JTable table, Object data) {
    return dataToTable(table, data, false);
  }

  public static JTable dataToTable(JTable table, Object data, boolean now) {
    List<List> rows = new ArrayList();
    List<String> cols = new ArrayList();
    if (data instanceof List) {
      for (Object x : (List) data) try {
        rows.add(dataToTable_makeRow(x, cols));
      } catch (Throwable __e) {
        printStackTrace2(__e);
      }
    } else if (data instanceof Map) {
      Map map = (Map) (data);
      for (Object key : map.keySet()) {
        Object value = map.get(key);
        rows.add(litlist(structureOrText(key), structureOrText(value)));
      }
    } else
      print("Unknown data type: " + data);
    if (now)
      setTableModel(table, fillTableWithData_makeModel(rows, toStringArray(cols)));
    else
      fillTableWithData(table, rows, cols);
    return table;
  }

  public static JWindow infoBox(String text) {
    return infoMessage(text);
  }

  public static JWindow infoBox(String text, double seconds) {
    return infoMessage(text, seconds);
  }

  public static JWindow infoBox(Throwable e) {
    return infoMessage(e);
  }

  public static JLabel jlabel(final String text) {
    return swingConstruct(BetterLabel.class, text);
  }

  public static JLabel jlabel() {
    return jlabel(" ");
  }

  public static void onDoubleClick(final JList list, final Object runnable) {
    list.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
          int idx = list.locationToIndex(evt.getPoint());
          Object item = list.getModel().getElementAt(idx);
          list.setSelectedIndex(idx);
          callF(runnable, item);
        }
      }
    });
  }

  public static void onDoubleClick(final JTable table, final Object runnable) {
    table.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
          int idx = table.rowAtPoint(evt.getPoint());
          table.setRowSelectionInterval(idx, idx);
          callF(runnable, idx);
        }
      }
    });
  }

  public static void onDoubleClick(JComponent c, final Object runnable) {
    c.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent evt) {
        if (evt.getButton() == 1 && evt.getClickCount() == 2)
          callF(runnable, evt.getPoint());
      }
    });
  }

  public static Object unstructure(String text) {
    return unstructure(text, false);
  }

  public static Object unstructure(String text, final boolean allDynamic) {
    return unstructure(text, allDynamic, null);
  }

  public static int structure_internStringsLongerThan = 50;

  public static int unstructure_unquoteBufSize = 100;

  public static int unstructure_tokrefs;

  public abstract static class unstructure_Receiver {

    public abstract void set(Object o);
  }

  public static Object unstructure(String text, boolean allDynamic, Object classFinder) {
    if (text == null)
      return null;
    return unstructure_tok(javaTokC_noMLS_iterator(text), allDynamic, classFinder);
  }

  public static Object unstructure_reader(BufferedReader reader) {
    return unstructure_tok(javaTokC_noMLS_onReader(reader), false, null);
  }

  public static Object unstructure_tok(final Producer<String> tok, final boolean allDynamic, final Object classFinder) {
    final boolean debug = unstructure_debug;
    final class X {

      public int i = -1;

      public HashMap<Integer, Object> refs = new HashMap();

      public HashMap<Integer, Object> tokrefs = new HashMap();

      public HashSet<String> concepts = new HashSet();

      public HashMap<String, Class> classesMap = new HashMap();

      public List<Runnable> stack = new ArrayList();

      public String curT;

      public char[] unquoteBuf = new char[unstructure_unquoteBufSize];

      public String unquote(String s) {
        return unquoteUsingCharArray(s, unquoteBuf);
      }

      public String t() {
        return curT;
      }

      public String tpp() {
        String t = curT;
        consume();
        return t;
      }

      public void parse(final unstructure_Receiver out) {
        String t = t();
        int refID = 0;
        if (structure_isMarker(t, 0, l(t))) {
          refID = parseInt(t.substring(1));
          consume();
        }
        final int _refID = refID;
        final int tokIndex = i;
        parse_inner(refID, tokIndex, new unstructure_Receiver() {

          public void set(Object o) {
            if (_refID != 0)
              refs.put(_refID, o);
            if (o != null)
              tokrefs.put(tokIndex, o);
            out.set(o);
          }
        });
      }

      public void parse_inner(int refID, int tokIndex, final unstructure_Receiver out) {
        String t = t();
        Class c = classesMap.get(t);
        if (c == null) {
          if (t.startsWith("\"")) {
            String s = internIfLongerThan(unquote(tpp()), structure_internStringsLongerThan);
            out.set(s);
            return;
          }
          if (t.startsWith("'")) {
            out.set(unquoteCharacter(tpp()));
            return;
          }
          if (t.equals("bigint")) {
            out.set(parseBigInt());
            return;
          }
          if (t.equals("d")) {
            out.set(parseDouble());
            return;
          }
          if (t.equals("fl")) {
            out.set(parseFloat());
            return;
          }
          if (t.equals("sh")) {
            consume();
            t = tpp();
            if (t.equals("-")) {
              t = tpp();
              out.set((short) (-parseInt(t)));
              return;
            }
            out.set((short) parseInt(t));
            return;
          }
          if (t.equals("-")) {
            consume();
            t = tpp();
            out.set(isLongConstant(t) ? (Object) (-parseLong(t)) : (Object) (-parseInt(t)));
            return;
          }
          if (isInteger(t) || isLongConstant(t)) {
            consume();
            if (isLongConstant(t)) {
              out.set(parseLong(t));
              return;
            }
            long l = parseLong(t);
            boolean isInt = l == (int) l;
            if (debug)
              print("l=" + l + ", isInt: " + isInt);
            out.set(isInt ? (Object) new Integer((int) l) : (Object) new Long(l));
            return;
          }
          if (t.equals("false") || t.equals("f")) {
            consume();
            out.set(false);
            return;
          }
          if (t.equals("true") || t.equals("t")) {
            consume();
            out.set(true);
            return;
          }
          if (t.equals("-")) {
            consume();
            t = tpp();
            out.set(isLongConstant(t) ? (Object) (-parseLong(t)) : (Object) (-parseInt(t)));
            return;
          }
          if (isInteger(t) || isLongConstant(t)) {
            consume();
            if (isLongConstant(t)) {
              out.set(parseLong(t));
              return;
            }
            long l = parseLong(t);
            boolean isInt = l == (int) l;
            if (debug)
              print("l=" + l + ", isInt: " + isInt);
            out.set(isInt ? (Object) new Integer((int) l) : (Object) new Long(l));
            return;
          }
          if (t.equals("File")) {
            consume();
            File f = new File(unquote(tpp()));
            out.set(f);
            return;
          }
          if (t.startsWith("r") && isInteger(t.substring(1))) {
            consume();
            int ref = Integer.parseInt(t.substring(1));
            Object o = refs.get(ref);
            if (o == null)
              print("Warning: unsatisfied back reference " + ref);
            out.set(o);
            return;
          }
          if (t.startsWith("t") && isInteger(t.substring(1))) {
            consume();
            int ref = Integer.parseInt(t.substring(1));
            Object o = tokrefs.get(ref);
            if (o == null)
              print("Warning: unsatisfied token reference " + ref);
            out.set(o);
            return;
          }
          if (t.equals("hashset")) {
            parseHashSet(out);
            return;
          }
          if (t.equals("treeset")) {
            parseTreeSet(out);
            return;
          }
          if (eqOneOf(t, "hashmap", "hm")) {
            consume();
            parseMap(new HashMap(), out);
            return;
          }
          if (t.equals("lhm")) {
            consume();
            parseMap(new LinkedHashMap(), out);
            return;
          }
          if (t.equals("{")) {
            parseMap(out);
            return;
          }
          if (t.equals("[")) {
            parseList(out);
            return;
          }
          if (t.equals("bitset")) {
            parseBitSet(out);
            return;
          }
          if (t.equals("array") || t.equals("intarray")) {
            parseArray(out);
            return;
          }
          if (t.equals("ba")) {
            consume();
            String hex = unquote(tpp());
            out.set(hexToBytes(hex));
            return;
          }
          if (t.equals("boolarray")) {
            consume();
            int n = parseInt(tpp());
            String hex = unquote(tpp());
            out.set(boolArrayFromBytes(hexToBytes(hex), n));
            return;
          }
          if (t.equals("class")) {
            out.set(parseClass());
            return;
          }
          if (t.equals("l")) {
            parseLisp(out);
            return;
          }
          if (t.equals("null")) {
            consume();
            out.set(null);
            return;
          }
          if (eq(t, "c")) {
            consume("c");
            t = t();
            assertTrue(isJavaIdentifier(t));
            concepts.add(t);
          }
        }
        if (eq(t, "j")) {
          consume("j");
          out.set(parseJava());
          return;
        }
        if (c == null && !isJavaIdentifier(t))
          throw new RuntimeException("Unknown token " + (i + 1) + ": " + t);
        if (c == null) {
          if (allDynamic)
            c = null;
          else
            c = classFinder != null ? (Class) callF(classFinder, t) : findClass(t);
          if (c != null)
            classesMap.put(t, c);
        }
        consume();
        boolean hasBracket = eq(t(), "(");
        if (hasBracket)
          consume();
        boolean hasOuter = hasBracket && eq(t(), "this$1");
        DynamicObject dO = null;
        Object o = null;
        if (c != null) {
          o = hasOuter ? nuStubInnerObject(c) : nuEmptyObject(c);
          if (o instanceof DynamicObject)
            dO = (DynamicObject) o;
        } else {
          if (concepts.contains(t) && (c = findClass("Concept")) != null)
            o = dO = (DynamicObject) nuEmptyObject(c);
          else
            dO = new DynamicObject();
          dO.className = t;
          if (debug)
            print("Made dynamic object " + t + " " + shortClassName(dO));
        }
        if (refID != 0)
          refs.put(refID, o != null ? o : dO);
        tokrefs.put(tokIndex, o != null ? o : dO);
        final LinkedHashMap<String, Object> fields = new LinkedHashMap();
        final Object _o = o;
        final DynamicObject _dO = dO;
        if (hasBracket) {
          stack.add(new Runnable() {

            public void run() {
              try {
                if (eq(t(), ")")) {
                  consume(")");
                  objRead(_o, _dO, fields);
                  out.set(_o != null ? _o : _dO);
                } else {
                  final String key = unquote(tpp());
                  consume("=");
                  stack.add(this);
                  parse(new unstructure_Receiver() {

                    public void set(Object value) {
                      fields.put(key, value);
                      if (eq(t(), ","))
                        consume();
                    }
                  });
                }
              } catch (Exception __e) {
                throw rethrow(__e);
              }
            }

            public String toString() {
              return "if (eq(t(), \")\")) {\r\n            consume(\")\");\r\n            objRead(_o, _dO, ...";
            }
          });
        } else {
          objRead(o, dO, fields);
          out.set(o != null ? o : dO);
        }
      }

      public void objRead(Object o, DynamicObject dO, Map<String, Object> fields) {
        if (o != null)
          if (dO != null) {
            if (debug)
              printStructure("setOptAllDyn", fields);
            setOptAllDyn(dO, fields);
          } else {
            setOptAll_pcall(o, fields);
          }
        else
          for (String field : keys(fields)) dO.fieldValues.put(intern(field), fields.get(field));
        if (o != null)
          pcallOpt_noArgs(o, "_doneLoading");
      }

      public void parseSet(final Set set, final unstructure_Receiver out) {
        parseList(new unstructure_Receiver() {

          public void set(Object o) {
            set.addAll((List) o);
            out.set(set);
          }
        });
      }

      public void parseLisp(final unstructure_Receiver out) {
        consume("l");
        consume("(");
        final ArrayList list = new ArrayList();
        stack.add(new Runnable() {

          public void run() {
            try {
              if (eq(t(), ")")) {
                consume(")");
                out.set(new Lisp((String) list.get(0), subList(list, 1)));
              } else {
                stack.add(this);
                parse(new unstructure_Receiver() {

                  public void set(Object o) {
                    list.add(o);
                    if (eq(t(), ","))
                      consume();
                  }
                });
              }
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "if (eq(t(), \")\")) {\r\n            consume(\")\");\r\n            out.set(Lisp((Str...";
          }
        });
        if (false)
          throw fail("class Lisp not included");
      }

      public void parseBitSet(final unstructure_Receiver out) {
        consume("bitset");
        consume("{");
        final BitSet bs = new BitSet();
        stack.add(new Runnable() {

          public void run() {
            try {
              if (eq(t(), "}")) {
                consume("}");
                out.set(bs);
              } else {
                stack.add(this);
                parse(new unstructure_Receiver() {

                  public void set(Object o) {
                    bs.set((Integer) o);
                    if (eq(t(), ","))
                      consume();
                  }
                });
              }
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "if (eq(t(), \"}\")) {\r\n          consume(\"}\");\r\n          out.set(bs);\r\n       ...";
          }
        });
      }

      public void parseList(final unstructure_Receiver out) {
        consume("[");
        final ArrayList list = new ArrayList();
        stack.add(new Runnable() {

          public void run() {
            try {
              if (eq(t(), "]")) {
                consume("]");
                out.set(list);
              } else {
                stack.add(this);
                parse(new unstructure_Receiver() {

                  public void set(Object o) {
                    list.add(o);
                    if (eq(t(), ","))
                      consume();
                  }
                });
              }
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "if (eq(t(), \"]\")) {\r\n          consume(\"]\");\r\n          out.set(list);\r\n     ...";
          }
        });
      }

      public void parseArray(final unstructure_Receiver out) {
        final String type = tpp();
        consume("{");
        final List list = new ArrayList();
        stack.add(new Runnable() {

          public void run() {
            try {
              if (eq(t(), "}")) {
                consume("}");
                out.set(type.equals("intarray") ? toIntArray(list) : list.toArray());
              } else {
                stack.add(this);
                parse(new unstructure_Receiver() {

                  public void set(Object o) {
                    list.add(o);
                    if (eq(t(), ","))
                      consume();
                  }
                });
              }
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "if (eq(t(), \"}\")) {\r\n          consume(\"}\");\r\n          out.set(type.equals(\"...";
          }
        });
      }

      public Object parseClass() {
        consume("class");
        consume("(");
        String name = unquote(tpp());
        consume(")");
        name = dropPrefix("main$", name);
        Class c = allDynamic ? null : classFinder != null ? (Class) callF(classFinder, name) : findClass(name);
        if (c != null)
          return c;
        DynamicObject dO = new DynamicObject();
        dO.className = "java.lang.Class";
        dO.fieldValues.put("name", name);
        return dO;
      }

      public Object parseBigInt() {
        consume("bigint");
        consume("(");
        String val = tpp();
        if (eq(val, "-"))
          val = "-" + tpp();
        consume(")");
        return new BigInteger(val);
      }

      public Object parseDouble() {
        consume("d");
        consume("(");
        String val = unquote(tpp());
        consume(")");
        return Double.parseDouble(val);
      }

      public Object parseFloat() {
        consume("fl");
        String val;
        if (eq(t(), "(")) {
          consume("(");
          val = unquote(tpp());
          consume(")");
        } else {
          val = unquote(tpp());
        }
        return Float.parseFloat(val);
      }

      public void parseHashSet(unstructure_Receiver out) {
        consume("hashset");
        parseSet(new HashSet(), out);
      }

      public void parseTreeSet(unstructure_Receiver out) {
        consume("treeset");
        parseSet(new TreeSet(), out);
      }

      public void parseMap(unstructure_Receiver out) {
        parseMap(new TreeMap(), out);
      }

      public Object parseJava() {
        String j = unquote(tpp());
        Matches m = new Matches();
        if (jmatch("java.awt.Color[r=*,g=*,b=*]", j, m))
          return nuObject("java.awt.Color", parseInt(m.unq(0)), parseInt(m.unq(1)), parseInt(m.unq(2)));
        else {
          warn("Unknown Java object: " + j);
          return null;
        }
      }

      public void parseMap(final Map map, final unstructure_Receiver out) {
        consume("{");
        stack.add(new Runnable() {

          public boolean v;

          public Object key;

          public void run() {
            if (v) {
              v = false;
              stack.add(this);
              consume("=");
              parse(new unstructure_Receiver() {

                public void set(Object value) {
                  map.put(key, value);
                  if (debug)
                    print("parseMap: Got value " + getClassName(value) + ", next token: " + quote(t()));
                  if (eq(t(), ","))
                    consume();
                }
              });
            } else {
              if (eq(t(), "}")) {
                consume("}");
                out.set(map);
              } else {
                v = true;
                stack.add(this);
                parse(new unstructure_Receiver() {

                  public void set(Object o) {
                    key = o;
                  }
                });
              }
            }
          }
        });
      }

      public void consume() {
        curT = tok.next();
        ++i;
      }

      public void consume(String s) {
        if (!eq(t(), s)) {
          throw fail(quote(s) + " expected, got " + quote(t()));
        }
        consume();
      }

      public void parse_x(unstructure_Receiver out) {
        consume();
        parse(out);
        while (nempty(stack)) popLast(stack).run();
      }
    }
    Boolean b = DynamicObject_loading.get();
    DynamicObject_loading.set(true);
    try {
      final Var v = new Var();
      X x = new X();
      x.parse_x(new unstructure_Receiver() {

        public void set(Object o) {
          v.set(o);
        }
      });
      unstructure_tokrefs = x.tokrefs.size();
      return v.get();
    } finally {
      DynamicObject_loading.set(b);
    }
  }

  public static boolean unstructure_debug;

  public static Concept getConcept(long id) {
    return mainConcepts.getConcept(id);
  }

  public static <A extends Concept> A getConcept(Class<A> cc, long id) {
    return getConcept(mainConcepts, cc, id);
  }

  public static <A extends Concept> A getConcept(Concepts concepts, Class<A> cc, long id) {
    Concept c = concepts.getConcept(id);
    if (c == null)
      return null;
    if (!isInstance(cc, c))
      throw fail("Can't convert concept: " + getClassName(c) + " -> " + getClassName(cc) + " (" + id + ")");
    return (A) c;
  }

  public static JTable sexyTableWithoutDrag() {
    final JTable table = tableWithToolTips();
    tablePopupMenu(table, new VF2<JPopupMenu, Integer>() {

      public void get(JPopupMenu menu, final Integer row) {
        try {
          final String item = first(getTableLine(table, row));
          MouseEvent e = tablePopupMenu_mouseEvent.get();
          final int col = table.columnAtPoint(e.getPoint());
          final Object value = table.getModel().getValueAt(row, col);
          if (value instanceof ImageIcon) {
            addMenuItem(menu, "Copy image to clipboard", new Runnable() {

              public void run() {
                try {
                  copyImageToClipboard(((ImageIcon) value).getImage());
                } catch (Exception __e) {
                  throw rethrow(__e);
                }
              }

              public String toString() {
                return "copyImageToClipboard(((ImageIcon) value).getImage());";
              }
            });
          } else {
            final String text = str(value);
            addMenuItem(menu, "Copy text to clipboard", new Runnable() {

              public void run() {
                try {
                  copyTextToClipboard(text);
                  print("Copied text to clipboard: " + quote(text));
                } catch (Exception __e) {
                  throw rethrow(__e);
                }
              }

              public String toString() {
                return "copyTextToClipboard(text);\r\n        print(\"Copied text to clipboard: \" + quot...";
              }
            });
          }
          addMenuItem(menu, "Set row height...", new Runnable() {

            public void run() {
              try {
                final JTextField tf = jTextField(table.getRowHeight());
                showTitledForm("Set row height", "Pixels", tf, new Runnable() {

                  public void run() {
                    try {
                      table.setRowHeight(parseInt(trim(tf.getText())));
                    } catch (Exception __e) {
                      throw rethrow(__e);
                    }
                  }

                  public String toString() {
                    return "table.setRowHeight(parseInt(trim(tf.getText())))";
                  }
                });
              } catch (Exception __e) {
                throw rethrow(__e);
              }
            }

            public String toString() {
              return "final JTextField tf = jTextField(table.getRowHeight());\r\n      showTitledForm...";
            }
          });
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "final S item = first(getTableLine(table, row));\r\n    MouseEvent e = tablePopu...";
      }
    });
    table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
    table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
    table.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
    table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
    table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
    ((InputMap) UIManager.get("Table.ancestorInputMap")).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
    return table;
  }

  public static <A extends Component> A centerFrame(A c) {
    Window w = getWindow(c);
    if (w != null)
      w.setLocationRelativeTo(null);
    return c;
  }

  public static <A extends Component> A centerFrame(int w, int h, A c) {
    return centerFrame(setFrameSize(w, h, c));
  }

  public static Random random_random = new Random();

  public static int random(int n) {
    return n <= 0 ? 0 : random_random.nextInt(n);
  }

  public static double random(double max) {
    return random() * max;
  }

  public static double random() {
    return random_random.nextInt(100001) / 100000.0;
  }

  public static double random(double min, double max) {
    return min + random() * (max - min);
  }

  public static int random(int min, int max) {
    return min + random(max - min);
  }

  public static <A> A random(List<A> l) {
    return oneOf(l);
  }

  public static <A> A random(Collection<A> c) {
    if (c instanceof List)
      return random((List<A>) c);
    int i = random(l(c));
    return collectionGet(c, i);
  }

  public static JWindow showLoadingAnimation() {
    return showLoadingAnimation("Hold on user...");
  }

  public static JWindow showLoadingAnimation(String text) {
    return showAnimationInTopRightCorner("#1003543", text);
  }

  public static long now_virtualTime;

  public static long now() {
    return now_virtualTime != 0 ? now_virtualTime : System.currentTimeMillis();
  }

  public static int showForm_defaultGap = 4;

  public static int showForm_gapBetweenColumns = 10;

  public static JPanel showFormTitled(final String title, final Object... _parts) {
    return swing(new F0<JPanel>() {

      public JPanel get() {
        try {
          final Var<JFrame> frame = new Var();
          JPanel panel = showForm_makePanel(frame, _parts);
          frame.set(showForm_makeFrame(title, panel));
          return panel;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "final new Var<JFrame> frame;\r\n    JPanel panel = showForm_makePanel(frame, _p...";
      }
    });
  }

  public static JPanel showForm_makePanel(final Var<JFrame> frame, Object... _parts) {
    List<JComponent> out = showForm_arrange1(showForm_makeComponents(frame, _parts));
    return vstackWithSpacing(out, showForm_defaultGap);
  }

  public static void useDBOf(String progID) {
    setDBProgramID(progID);
  }

  public static Thread startThread(Object runnable) {
    return startThread(defaultThreadName(), runnable);
  }

  public static Thread startThread(String name, Object runnable) {
    runnable = wrapAsActivity(runnable);
    return startThread(newThread(toRunnable(runnable), name));
  }

  public static Thread startThread(Thread t) {
    _registerThread(t);
    t.start();
    return t;
  }

  public static double autoRestart_interval = 10;

  public static boolean autoRestart_on, autoRestart_debug, autoRestart_simulate;

  public static java.util.Timer autoRestart_timer;

  public static void autoRestart(double interval) {
    autoRestart_interval = interval;
    autoRestart();
  }

  public static void autoRestart() {
    if (!isMain() || autoRestart_on)
      return;
    autoRestart_on = true;
    autoRestart_schedule();
    preloadProgramTitle();
  }

  public static void autoRestart_off() {
    if (!autoRestart_on)
      return;
    stopTimer(autoRestart_timer);
    autoRestart_timer = null;
  }

  public static void autoRestart_schedule() {
    autoRestart_timer = doLater_daemon(toMS(autoRestart_interval), "autoRestart_check");
  }

  public static void autoRestart_check() {
    try {
      String newMD5;
      try {
        newMD5 = loadPageSilently("http://botcompany.de/1010693/raw?id=" + psI(programID()));
      } catch (Throwable e) {
        return;
      }
      if (!isMD5(newMD5)) {
        if (autoRestart_debug)
          print("autoRestart: no server transpilation");
        return;
      }
      if (autoRestart_localMD5 == null)
        autoRestart_localMD5 = md5(loadCachedTranspilation(programID()));
      String localMD5 = autoRestart_localMD5();
      if (neq(localMD5, newMD5)) {
        if (autoRestart_simulate)
          print("Would upgrade now. " + localMD5 + " -> " + newMD5);
        else {
          infoBox("Upgrading " + programTitle());
          restartWithDelay(500);
          sleep();
        }
      }
    } finally {
      if (autoRestart_debug)
        print("autoRestart: Done");
      autoRestart_schedule();
    }
  }

  public static RuntimeException rethrow(Throwable t) {
    if (t instanceof Error)
      _handleError((Error) t);
    throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
  }

  public static void addToWindowNorth(Component c, Component toAdd) {
    addToWindowTop(c, toAdd);
  }

  public static void substance() {
    substanceLAF();
  }

  public static void substance(String skinName) {
    substanceLAF(skinName);
  }

  public static Runnable runnableThread(final Runnable r) {
    return new Runnable() {

      public void run() {
        try {
          new Thread(r).start();
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "new Thread(r).start()";
      }
    };
  }

  public static Map<Thread, Boolean> _registerThread_threads = newWeakHashMap();

  public static Thread _registerThread(Thread t) {
    _registerThread_threads.put(t, true);
    return t;
  }

  public static void _registerThread() {
    _registerThread(Thread.currentThread());
  }

  public static boolean nempty(Collection c) {
    return !isEmpty(c);
  }

  public static boolean nempty(CharSequence s) {
    return !isEmpty(s);
  }

  public static boolean nempty(Object[] o) {
    return !isEmpty(o);
  }

  public static boolean nempty(byte[] o) {
    return !isEmpty(o);
  }

  public static boolean nempty(Map m) {
    return !isEmpty(m);
  }

  public static boolean nempty(Iterator i) {
    return i != null && i.hasNext();
  }

  public static boolean nempty(Object o) {
    return !empty(o);
  }

  public static boolean match(String pat, String s) {
    return match3(pat, s);
  }

  public static boolean match(String pat, String s, Matches matches) {
    return match3(pat, s, matches);
  }

  public static Component awtReplaceComponent(final Component c, final Object makeNewComponent) {
    if (c == null)
      return null;
    return (Component) swing(new F0<Object>() {

      public Object get() {
        try {
          Container parent = c.getParent();
          if (parent == null)
            return null;
          Component[] l = parent.getComponents();
          LayoutManager layout = parent.getLayout();
          if (!(layout instanceof BorderLayout || layout instanceof ViewportLayout))
            warn("awtReplaceComponent only tested for BorderLayout/ViewportLayout. Have: " + layout);
          int idx = indexOf(l, c);
          if (idx < 0)
            throw fail("component not found in parent");
          Object constraints = callOpt(layout, "getConstraints", c);
          parent.remove(c);
          Component newComponent = (Component) (makeNewComponent instanceof Component ? makeNewComponent : callF(makeNewComponent, c));
          parent.add(newComponent, constraints, idx);
          validateFrame(parent);
          return newComponent;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "Container parent = c.getParent();\r\n    if (parent == null) null;\r\n    Compone...";
      }
    });
  }

  public static String getTextTrim(JTextComponent c) {
    return trim(getText(c));
  }

  public static String getTextTrim(JComboBox cb) {
    return trim(getText(cb));
  }

  public static String getTextTrim(JComponent c) {
    if (c instanceof JLabel)
      return trim(((JLabel) c).getText());
    if (c instanceof JComboBox)
      return getTextTrim((JComboBox) c);
    return getTextTrim((JTextComponent) c);
  }

  public static void disposeWindow(final Window window) {
    if (window != null) {
      swing(new Runnable() {

        public void run() {
          try {
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            window.dispose();
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING)); //...";
        }
      });
    }
  }

  public static void disposeWindow(final Component c) {
    disposeWindow(getWindow(c));
  }

  public static void disposeWindow(Object o) {
    if (o != null)
      disposeWindow(((Component) o));
  }

  public static void disposeWindow() {
    disposeWindow(heldInstance(Component.class));
  }

  public static boolean structure_showTiming, structure_checkTokenCount;

  public static String structure(Object o) {
    return structure(o, new structure_Data());
  }

  public static String structure(Object o, structure_Data d) {
    StringWriter sw = new StringWriter();
    d.out = new PrintWriter(sw);
    structure_go(o, d);
    String s = str(sw);
    if (structure_checkTokenCount) {
      print("token count=" + d.n);
      assertEquals("token count", l(javaTokC(s)), d.n);
    }
    return s;
  }

  public static void structure_go(Object o, structure_Data d) {
    structure_1(o, d);
    while (nempty(d.stack)) popLast(d.stack).run();
  }

  public static void structureToPrintWriter(Object o, PrintWriter out) {
    structure_Data d = new structure_Data();
    d.out = out;
    structure_go(o, d);
  }

  public static boolean structure_allowShortening = false;

  public static class structure_Data {

    public PrintWriter out;

    public int stringSizeLimit;

    public int shareStringsLongerThan = 20;

    public boolean noStringSharing;

    public IdentityHashMap<Object, Integer> seen = new IdentityHashMap();

    public HashMap<String, Integer> strings = new HashMap();

    public HashSet<String> concepts = new HashSet();

    public HashMap<Class, List<Field>> fieldsByClass = new HashMap();

    public int n;

    public List<Runnable> stack = new ArrayList();

    public structure_Data append(String token) {
      out.print(token);
      ++n;
      return this;
    }

    public structure_Data append(int i) {
      out.print(i);
      ++n;
      return this;
    }

    public structure_Data append(String token, int tokCount) {
      out.print(token);
      n += tokCount;
      return this;
    }

    public structure_Data app(String token) {
      out.print(token);
      return this;
    }

    public structure_Data app(int i) {
      out.print(i);
      return this;
    }
  }

  public static void structure_1(final Object o, final structure_Data d) {
    if (o == null) {
      d.append("null");
      return;
    }
    Class c = o.getClass();
    boolean concept = false;
    concept = o instanceof Concept;
    List<Field> lFields = d.fieldsByClass.get(c);
    if (lFields == null) {
      if (o instanceof Number) {
        PrintWriter out = d.out;
        if (o instanceof Integer) {
          int i = ((Integer) o).intValue();
          out.print(i);
          d.n += i < 0 ? 2 : 1;
          return;
        }
        if (o instanceof Long) {
          long l = ((Long) o).longValue();
          out.print(l);
          out.print("L");
          d.n += l < 0 ? 2 : 1;
          return;
        }
        if (o instanceof Short) {
          short s = ((Short) o).shortValue();
          d.append("sh ", 2);
          out.print(s);
          d.n += s < 0 ? 2 : 1;
          return;
        }
        if (o instanceof Float) {
          d.append("fl ", 2);
          quoteToPrintWriter(str(o), out);
          return;
        }
        if (o instanceof Double) {
          d.append("d(", 3);
          quoteToPrintWriter(str(o), out);
          d.append(")");
          return;
        }
        if (o instanceof BigInteger) {
          out.print("bigint(");
          out.print(o);
          out.print(")");
          d.n += ((BigInteger) o).signum() < 0 ? 5 : 4;
          return;
        }
      }
      if (o instanceof Boolean) {
        d.append(((Boolean) o).booleanValue() ? "t" : "f");
        return;
      }
      if (o instanceof Character) {
        d.append(quoteCharacter((Character) o));
        return;
      }
      if (o instanceof File) {
        d.append("File ").append(quote(((File) o).getPath()));
        return;
      }
      Integer ref = d.seen.get(o);
      if (o instanceof String && ref == null)
        ref = d.strings.get((String) o);
      if (ref != null) {
        d.append("t").app(ref);
        return;
      }
      if (!(o instanceof String))
        d.seen.put(o, d.n);
      else {
        String s = d.stringSizeLimit != 0 ? shorten((String) o, d.stringSizeLimit) : (String) o;
        if (!d.noStringSharing) {
          if (d.shareStringsLongerThan == Integer.MAX_VALUE)
            d.seen.put(o, d.n);
          if (l(s) >= d.shareStringsLongerThan)
            d.strings.put(s, d.n);
        }
        quoteToPrintWriter(s, d.out);
        d.n++;
        return;
      }
      if (o instanceof HashSet) {
        d.append("hashset ");
        structure_1(new ArrayList((Set) o), d);
        return;
      }
      if (o instanceof TreeSet) {
        d.append("treeset ");
        structure_1(new ArrayList((Set) o), d);
        return;
      }
      String name = c.getName();
      if (o instanceof Collection && !startsWith(name, "main$")) {
        d.append("[");
        final int l = d.n;
        final Iterator it = ((Collection) o).iterator();
        d.stack.add(new Runnable() {

          public void run() {
            try {
              if (!it.hasNext())
                d.append("]");
              else {
                d.stack.add(this);
                if (d.n != l)
                  d.append(", ");
                structure_1(it.next(), d);
              }
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "if (!it.hasNext())\r\n          d.append(\"]\");\r\n        else {\r\n          d.sta...";
          }
        });
        return;
      }
      if (o instanceof Map && !startsWith(name, "main$")) {
        if (o instanceof LinkedHashMap)
          d.append("lhm");
        else if (o instanceof HashMap)
          d.append("hm");
        d.append("{");
        final int l = d.n;
        final Iterator it = ((Map) o).entrySet().iterator();
        d.stack.add(new Runnable() {

          public boolean v;

          public Map.Entry e;

          public void run() {
            if (v) {
              d.append("=");
              v = false;
              d.stack.add(this);
              structure_1(e.getValue(), d);
            } else {
              if (!it.hasNext())
                d.append("}");
              else {
                e = (Map.Entry) it.next();
                v = true;
                d.stack.add(this);
                if (d.n != l)
                  d.append(", ");
                structure_1(e.getKey(), d);
              }
            }
          }
        });
        return;
      }
      if (c.isArray()) {
        if (o instanceof byte[]) {
          d.append("ba ").append(quote(bytesToHex((byte[]) o)));
          return;
        }
        final int n = Array.getLength(o);
        if (o instanceof boolean[]) {
          String hex = boolArrayToHex((boolean[]) o);
          int i = l(hex);
          while (i > 0 && hex.charAt(i - 1) == '0' && hex.charAt(i - 2) == '0') i -= 2;
          d.append("boolarray ").append(n).app(" ").append(quote(substring(hex, 0, i)));
          return;
        }
        String atype = "array", sep = ", ";
        if (o instanceof int[]) {
          atype = "intarray";
          sep = " ";
        }
        d.append(atype).append("{");
        d.stack.add(new Runnable() {

          public int i;

          public void run() {
            if (i >= n)
              d.append("}");
            else {
              d.stack.add(this);
              if (i > 0)
                d.append(", ");
              structure_1(Array.get(o, i++), d);
            }
          }
        });
        return;
      }
      if (o instanceof Class) {
        d.append("class(", 2).append(quote(((Class) o).getName())).append(")");
        return;
      }
      if (o instanceof Throwable) {
        d.append("exception(", 2).append(quote(((Throwable) o).getMessage())).append(")");
        return;
      }
      if (o instanceof BitSet) {
        BitSet bs = (BitSet) o;
        d.append("bitset{", 2);
        int l = d.n;
        for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
          if (d.n != l)
            d.append(", ");
          d.append(i);
        }
        d.append("}");
        return;
      }
      if (name.startsWith("java.") || name.startsWith("javax.")) {
        d.append("j ").append(quote(str(o)));
        return;
      }
      if (o instanceof Lisp) {
        d.append("l(", 2);
        final Lisp lisp = (Lisp) (o);
        structure_1(lisp.head, d);
        final Iterator it = lisp.args == null ? emptyIterator() : lisp.args.iterator();
        d.stack.add(new Runnable() {

          public void run() {
            try {
              if (!it.hasNext())
                d.append(")");
              else {
                d.stack.add(this);
                d.append(", ");
                structure_1(it.next(), d);
              }
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "if (!it.hasNext())\r\n            d.append(\")\");\r\n          else {\r\n           ...";
          }
        });
        return;
      }
      String dynName = shortDynamicClassName(o);
      if (concept && !d.concepts.contains(dynName)) {
        d.concepts.add(dynName);
        d.append("c ");
      }
      TreeSet<Field> fields = new TreeSet<Field>(new Comparator<Field>() {

        public int compare(Field a, Field b) {
          return stdcompare(a.getName(), b.getName());
        }
      });
      Class cc = c;
      while (cc != Object.class) {
        for (Field field : getDeclaredFields_cached(cc)) {
          if ((field.getModifiers() & (java.lang.reflect.Modifier.STATIC | java.lang.reflect.Modifier.TRANSIENT)) != 0)
            continue;
          String fieldName = field.getName();
          fields.add(field);
        }
        cc = cc.getSuperclass();
      }
      lFields = asList(fields);
      for (int i = 0; i < l(lFields); i++) {
        Field f = lFields.get(i);
        if (f.getName().equals("this$1")) {
          lFields.remove(i);
          lFields.add(0, f);
          break;
        }
      }
      d.fieldsByClass.put(c, lFields);
    } else {
      Integer ref = d.seen.get(o);
      if (ref != null) {
        d.append("t").app(ref);
        return;
      }
      d.seen.put(o, d.n);
    }
    LinkedHashMap<String, Object> fv = new LinkedHashMap();
    for (Field f : lFields) {
      Object value;
      try {
        value = f.get(o);
      } catch (Exception e) {
        value = "?";
      }
      if (value != null)
        fv.put(f.getName(), value);
    }
    String name = c.getName();
    String shortName = dropPrefix("main$", name);
    if (concept && eq(fv.get("className"), shortName))
      fv.remove("className");
    if (o instanceof DynamicObject) {
      fv.putAll((Map) fv.get("fieldValues"));
      fv.remove("fieldValues");
      shortName = shortDynamicClassName(o);
      fv.remove("className");
    }
    String singleField = fv.size() == 1 ? first(fv.keySet()) : null;
    d.append(shortName);
    final int l = d.n;
    final Iterator it = fv.entrySet().iterator();
    d.stack.add(new Runnable() {

      public void run() {
        try {
          if (!it.hasNext()) {
            if (d.n != l)
              d.append(")");
          } else {
            Map.Entry e = (Map.Entry) it.next();
            d.append(d.n == l ? "(" : ", ");
            d.append((String) e.getKey()).append("=");
            d.stack.add(this);
            structure_1(e.getValue(), d);
          }
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "if (!it.hasNext()) {\r\n      if (d.n != l)\r\n        d.append(\")\");\r\n    } else...";
      }
    });
  }

  public static void tablePopupMenuItem(final JTable table, final String name, final Object action) {
    tablePopupMenu(table, new VF2<JPopupMenu, Integer>() {

      public void get(JPopupMenu menu, final Integer row) {
        try {
          addMenuItem(menu, name, new Runnable() {

            public void run() {
              try {
                pcallF(action, row);
              } catch (Exception __e) {
                throw rethrow(__e);
              }
            }

            public String toString() {
              return "pcallF(action, row)";
            }
          });
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "addMenuItem(menu, name, r { pcallF(action, row) });";
      }
    });
  }

  public static Object swing(Object f) {
    return swingAndWait(f);
  }

  public static <A> A swing(F0<A> f) {
    return (A) swingAndWait(f);
  }

  public static Thread runInNewThread_awt(final String progID) {
    return startThread(progID, new Runnable() {

      public void run() {
        try {
          try {
            callMainAsChild(hotwire(progID));
          } catch (Throwable __e) {
            messageBox(__e);
          }
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "pcall-messagebox {\r\n      callMainAsChild(hotwire(progID));\r\n    }";
      }
    });
  }

  public static volatile StringBuffer local_log = new StringBuffer();

  public static volatile StringBuffer print_log = local_log;

  public static volatile int print_log_max = 1024 * 1024;

  public static volatile int local_log_max = 100 * 1024;

  public static boolean print_silent;

  public static Object print_byThread_lock = new Object();

  public static volatile ThreadLocal<Object> print_byThread;

  public static volatile Object print_allThreads;

  public static void print() {
    print("");
  }

  public static <A> A print(String s, A o) {
    print(s + o);
    return o;
  }

  public static <A> A print(A o) {
    ping();
    if (print_silent)
      return o;
    String s = String.valueOf(o) + "\n";
    print_noNewLine(s);
    return o;
  }

  public static void print_noNewLine(String s) {
    Object f = print_byThread == null ? null : print_byThread.get();
    if (f == null)
      f = print_allThreads;
    if (f != null)
      if (isFalse(f instanceof F1 ? ((F1) f).get(s) : callF(f, s)))
        return;
    print_raw(s);
  }

  public static void print_raw(String s) {
    s = fixNewLines(s);
    StringBuffer loc = local_log;
    StringBuffer buf = print_log;
    int loc_max = print_log_max;
    if (buf != loc && buf != null) {
      print_append(buf, s, print_log_max);
      loc_max = local_log_max;
    }
    if (loc != null)
      print_append(loc, s, loc_max);
    System.out.print(s);
  }

  public static void print(long l) {
    print(String.valueOf(l));
  }

  public static void print(char c) {
    print(String.valueOf(c));
  }

  public static void print_append(StringBuffer buf, String s, int max) {
    synchronized (buf) {
      buf.append(s);
      max /= 2;
      if (buf.length() > max)
        try {
          int newLength = max / 2;
          int ofs = buf.length() - newLength;
          String newString = buf.substring(ofs);
          buf.setLength(0);
          buf.append("[...] ").append(newString);
        } catch (Exception e) {
          buf.setLength(0);
        }
    }
  }

  public static Concept cnew(String name, Object... values) {
    Class<? extends Concept> cc = findClass(name);
    Concept c = cc != null ? nuObject(cc) : new Concept(name);
    csetAll(c, values);
    return c;
  }

  public static Concept cnew(Concepts concepts, String name, Object... values) {
    Class<? extends Concept> cc = findClass(name);
    concepts_unlisted.set(true);
    Concept c;
    try {
      c = cc != null ? nuObject(cc) : new Concept(name);
    } finally {
      concepts_unlisted.set(null);
    }
    concepts.register(c);
    csetAll(c, values);
    return c;
  }

  public static <A extends Concept> A cnew(Class<A> cc, Object... values) {
    A c = nuObject(cc);
    csetAll(c, values);
    return c;
  }

  public static <A extends Concept> A cnew(Concepts concepts, Class<A> cc, Object... values) {
    concepts_unlisted.set(true);
    A c;
    try {
      c = nuObject(cc);
    } finally {
      concepts_unlisted.set(null);
    }
    concepts.register(c);
    csetAll(c, values);
    return c;
  }

  public static Throwable printStackTrace2(Throwable e) {
    print(getStackTrace2(e));
    return e;
  }

  public static void printStackTrace2() {
    printStackTrace2(new Throwable());
  }

  public static void printStackTrace2(String msg) {
    printStackTrace2(new Throwable(msg));
  }

  public static String copyTextToClipboard(String text) {
    StringSelection selection = new StringSelection(text);
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    return text;
  }

  public static void db() {
    conceptsAndBot();
  }

  public static <A> List<A> reversed(Collection<A> l) {
    return reversedList(l);
  }

  public static <A> List<A> reversed(A[] l) {
    return reversedList(asList(l));
  }

  public static String reversed(String s) {
    return reversedString(s);
  }

  public static Object first(Object list) {
    return empty((List) list) ? null : ((List) list).get(0);
  }

  public static <A> A first(List<A> list) {
    return empty(list) ? null : list.get(0);
  }

  public static <A> A first(A[] bla) {
    return bla == null || bla.length == 0 ? null : bla[0];
  }

  public static <A> A first(Iterable<A> i) {
    if (i == null)
      return null;
    Iterator<A> it = i.iterator();
    return it.hasNext() ? it.next() : null;
  }

  public static Character first(String s) {
    return empty(s) ? null : s.charAt(0);
  }

  public static <A, B> A first(Pair<A, B> p) {
    return p == null ? null : p.a;
  }

  public static boolean setText_opt = true;

  public static <A extends JTextComponent> A setText(A c, Object text) {
    setText((Object) c, text);
    return c;
  }

  public static <A extends JComboBox> A setText(final A c, Object text) {
    final String s = strUnnull(text);
    {
      swing(new Runnable() {

        public void run() {
          try {
            c.getEditor().setItem(s);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "c.getEditor().setItem(s);";
        }
      });
    }
    return c;
  }

  public static void setText(JLabel c, Object text) {
    setText((Object) c, text);
  }

  public static JButton setText(JButton c, Object text) {
    setText((Object) c, jlabel_textAsHTML_center_ifNeeded(strUnnull(text)));
    return c;
  }

  public static <A> A setText(final A c, Object text) {
    if (c == null)
      return null;
    final String s = strUnnull(text);
    swingAndWait(new Runnable() {

      public void run() {
        try {
          if (!setText_opt || neq(callOpt(c, "getText"), s))
            call(c, "setText", s);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "if (!setText_opt || neq(callOpt(c, \"getText\"), s))\r\n      call(c, \"setText\", s);";
      }
    });
    return c;
  }

  public static void awtCalcOnConceptChanges(JComponent component, int delay, final Object runnable, final boolean runOnFirstTime) {
    awtCalcOnConceptChanges(component, delay, 0, runnable, runOnFirstTime);
  }

  public static void awtCalcOnConceptChanges(JComponent component, int delay, int firstDelay, final Object runnable, final boolean runOnFirstTime) {
    installTimer(component, delay, firstDelay, new Runnable() {

      public long c = runOnFirstTime ? -1 : changeCount();

      public SingleThread thread = new SingleThread();

      public void run() {
        long _c = changeCount();
        if (_c != c && !thread.running()) {
          c = _c;
          thread.go(runnable);
        }
      }
    });
  }

  public static Font typeWriterFont() {
    return typeWriterFont(14);
  }

  public static Font typeWriterFont(int size) {
    return new Font("Courier", Font.PLAIN, size);
  }

  public static Object[] asArray(List l) {
    return toObjectArray(l);
  }

  public static <A> A[] asArray(Class<A> type, List l) {
    return (A[]) l.toArray((Object[]) Array.newInstance(type, l.size()));
  }

  public static String uploadToImageServerIfNotThere(BufferedImage img, String name) {
    String md5 = md5OfRGBImage(new RGBImage(img));
    long id = imageServerCheckMD5(md5);
    if (id == 0)
      return uploadToImageServer_new(img, name);
    else
      return "http://ai1.lol/images/raw/" + id;
  }

  public static Map<JFrame, Boolean> makeFrame_myFrames = weakHashMap();

  public static String makeFrame_defaultIcon;

  public static boolean makeFrame_hideConsole;

  public static ThreadLocal<VF1<JFrame>> makeFrame_post = new ThreadLocal();

  public static JFrame makeFrame() {
    return makeFrame((Component) null);
  }

  public static JFrame makeFrame(Object content) {
    return makeFrame(programTitle(), content);
  }

  public static JFrame makeFrame(String title) {
    return makeFrame(title, null);
  }

  public static JFrame makeFrame(String title, Object content) {
    return makeFrame(title, content, true);
  }

  public static JFrame makeFrame(final String title, final Object content, final boolean showIt) {
    final VF1<JFrame> post = optParam(makeFrame_post);
    return swing(new F0<JFrame>() {

      public JFrame get() {
        try {
          if (getFrame(content) != null)
            return getFrame(setFrameTitle((Component) content, title));
          final JFrame frame = new JFrame(title);
          if (makeFrame_defaultIcon != null)
            setFrameIconLater(frame, makeFrame_defaultIcon);
          _initFrame(frame);
          JComponent wrapped = wrap(content);
          if (wrapped != null)
            frame.getContentPane().add(wrapped);
          frame.setBounds(defaultNewFrameBounds());
          callF(post, frame);
          if (showIt)
            frame.setVisible(true);
          if (showIt && makeFrame_hideConsole) {
            hideConsole();
            makeFrame_hideConsole = false;
          }
          return frame;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "if (getFrame(content) != null)\r\n      ret getFrame(setFrameTitle((Component) ...";
      }
    });
  }

  public static WeakHashMap<Class, ArrayList<Method>> callF_cache = new WeakHashMap();

  public static <A> A callF(F0<A> f) {
    return f == null ? null : f.get();
  }

  public static <A, B> B callF(F1<A, B> f, A a) {
    return f == null ? null : f.get(a);
  }

  public static Object callF(Object f, Object... args) {
    try {
      if (f instanceof String)
        return callMC((String) f, args);
      if (f instanceof Runnable) {
        ((Runnable) f).run();
        return null;
      }
      if (f == null)
        return null;
      Class c = f.getClass();
      ArrayList<Method> methods;
      synchronized (callF_cache) {
        methods = callF_cache.get(c);
        if (methods == null)
          methods = callF_makeCache(c);
      }
      int n = l(methods);
      if (n == 0) {
        throw fail("No get method in " + getClassName(c));
      }
      if (n == 1)
        return invokeMethod(methods.get(0), f, args);
      for (int i = 0; i < n; i++) {
        Method m = methods.get(i);
        if (call_checkArgs(m, args, false))
          return invokeMethod(m, f, args);
      }
      throw fail("No matching get method in " + getClassName(c));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static ArrayList<Method> callF_makeCache(Class c) {
    ArrayList<Method> l = new ArrayList();
    Class _c = c;
    do {
      for (Method m : _c.getDeclaredMethods()) if (m.getName().equals("get")) {
        m.setAccessible(true);
        l.add(m);
      }
      if (!l.isEmpty())
        break;
      _c = _c.getSuperclass();
    } while (_c != null);
    callF_cache.put(c, l);
    return l;
  }

  public static <A, B> Map<A, B> newWeakHashMap() {
    return _registerWeakMap(synchroMap(new WeakHashMap()));
  }

  public static <A> ArrayList<A> asList(A[] a) {
    return a == null ? new ArrayList<A>() : new ArrayList<A>(Arrays.asList(a));
  }

  public static ArrayList<Integer> asList(int[] a) {
    ArrayList<Integer> l = new ArrayList();
    for (int i : a) l.add(i);
    return l;
  }

  public static <A> ArrayList<A> asList(Iterable<A> s) {
    if (s instanceof ArrayList)
      return (ArrayList) s;
    ArrayList l = new ArrayList();
    if (s != null)
      for (A a : s) l.add(a);
    return l;
  }

  public static <A> ArrayList<A> asList(Enumeration<A> e) {
    ArrayList l = new ArrayList();
    if (e != null)
      while (e.hasMoreElements()) l.add(e.nextElement());
    return l;
  }

  public static boolean isInstance(Class type, Object arg) {
    return type.isInstance(arg);
  }

  public static class wrapAsActivity_R implements Runnable {

    public Object r;

    public wrapAsActivity_R() {
    }

    public wrapAsActivity_R(Object r) {
      this.r = r;
    }

    public void run() {
      AutoCloseable __366 = tempActivity(r);
      try {
        callF(r);
      } finally {
        _close(__366);
      }
    }
  }

  public static wrapAsActivity_R wrapAsActivity(final Object r) {
    return r instanceof wrapAsActivity_R ? ((wrapAsActivity_R) r) : new wrapAsActivity_R(r);
  }

  public static boolean isWindows() {
    return System.getProperty("os.name").contains("Windows");
  }

  public static String quoteCharacter(char c) {
    if (c == '\'')
      return "'\\''";
    if (c == '\\')
      return "'\\\\'";
    if (c == '\r')
      return "'\\r'";
    if (c == '\n')
      return "'\\n'";
    if (c == '\t')
      return "'\\t'";
    return "'" + c + "'";
  }

  public static JTextArea wrappedTextArea(final JTextArea ta) {
    {
      swing(new Runnable() {

        public void run() {
          try {
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "ta.setLineWrap(true);\r\n    ta.setWrapStyleWord(true);";
        }
      });
    }
    return ta;
  }

  public static JTextArea wrappedTextArea() {
    return wrappedTextArea(jtextarea());
  }

  public static JTextArea wrappedTextArea(String text) {
    JTextArea ta = wrappedTextArea();
    ta.setText(text);
    return ta;
  }

  public static JTextField jTextField() {
    return jTextField("");
  }

  public static JTextField jTextField(final String text) {
    return swing(new F0<JTextField>() {

      public JTextField get() {
        try {
          JTextField tf = new JTextField(unnull(text));
          jenableUndoRedo(tf);
          tf.selectAll();
          return tf;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JTextField tf = new JTextField(unnull(text));\r\n    jenableUndoRedo(tf);\r\n    ...";
      }
    });
  }

  public static JTextField jTextField(Object o) {
    return jTextField(strOrEmpty(o));
  }

  public static String shortDynamicClassName(Object o) {
    if (o instanceof DynamicObject && ((DynamicObject) o).className != null)
      return ((DynamicObject) o).className;
    return shortClassName(o);
  }

  public static boolean infoMessage_alwaysOnTop = true;

  public static double infoMessage_defaultTime = 5.0;

  public static JWindow infoMessage(String text) {
    return infoMessage(text, infoMessage_defaultTime);
  }

  public static JWindow infoMessage(final String text, final double seconds) {
    print(text);
    return infoMessage_noprint(text, seconds);
  }

  public static JWindow infoMessage_noprint(String text) {
    return infoMessage_noprint(text, infoMessage_defaultTime);
  }

  public static JWindow infoMessage_noprint(final String text, final double seconds) {
    if (empty(text))
      return null;
    logQuotedWithDate(infoBoxesLogFile(), text);
    if (isHeadless())
      return null;
    return (JWindow) swingAndWait(new F0<Object>() {

      public Object get() {
        try {
          final JWindow window = showWindow(infoMessage_makePanel(text));
          window.setSize(300, 150);
          moveToTopRightCorner(window);
          if (infoMessage_alwaysOnTop)
            window.setAlwaysOnTop(true);
          window.setVisible(true);
          disposeWindowAfter(iround(seconds * 1000), window);
          return window;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "final JWindow window = showWindow(infoMessage_makePanel(text));\r\n    window.s...";
      }
    });
  }

  public static JWindow infoMessage(Throwable e) {
    showConsole();
    printStackTrace(e);
    return infoMessage(exceptionToStringShort(e));
  }

  public static String getText(final AbstractButton c) {
    return c == null ? "" : (String) swingAndWait(new F0<Object>() {

      public Object get() {
        try {
          return c.getText();
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "c.getText()";
      }
    });
  }

  public static String getText(final JTextComponent c) {
    return c == null ? "" : (String) swingAndWait(new F0<Object>() {

      public Object get() {
        try {
          return c.getText();
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "c.getText()";
      }
    });
  }

  public static String getText(final JLabel l) {
    return l == null ? "" : (String) swingAndWait(new F0<Object>() {

      public Object get() {
        try {
          return l.getText();
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "l.getText()";
      }
    });
  }

  public static String getText(final JComboBox cb) {
    if (cb.isEditable())
      return unnull((String) cb.getEditor().getItem());
    else
      return str(cb.getSelectedItem());
  }

  public static byte[] hexToBytes(String s) {
    if (odd(l(s)))
      throw fail("Hex string has odd length: " + quote(shorten(10, s)));
    int n = l(s) / 2;
    byte[] bytes = new byte[n];
    for (int i = 0; i < n; i++) {
      int a = parseHexChar(s.charAt(i * 2));
      int b = parseHexChar(s.charAt(i * 2 + 1));
      if (a < 0 || b < 0)
        throw fail("Bad hex byte: " + quote(substring(s, i * 2, i * 2 + 2)) + " at " + i * 2 + "/" + l(s));
      bytes[i] = (byte) ((a << 4) | b);
    }
    return bytes;
  }

  public static void setDBProgramID(String progID) {
    getDBProgramID_id = progID;
  }

  public static String str(Object o) {
    return o == null ? "null" : o.toString();
  }

  public static String str(char[] c) {
    return new String(c);
  }

  public static JMenuBar addMenuBar(JFrame f) {
    if (f == null)
      return null;
    JMenuBar bar = f.getJMenuBar();
    if (bar == null) {
      f.setJMenuBar(bar = new JMenuBar());
      revalidateFrame(f);
    }
    return bar;
  }

  public static JMenuBar addMenuBar(Component c) {
    return addMenuBar(getFrame(c));
  }

  public static HashMap<Class, Constructor> nuEmptyObject_cache = new HashMap();

  public static <A> A nuEmptyObject(Class<A> c) {
    try {
      Constructor ctr;
      synchronized (nuEmptyObject_cache) {
        ctr = nuEmptyObject_cache.get(c);
        if (ctr == null) {
          nuEmptyObject_cache.put(c, ctr = nuEmptyObject_findConstructor(c));
          ctr.setAccessible(true);
        }
      }
      return (A) ctr.newInstance();
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static Constructor nuEmptyObject_findConstructor(Class c) {
    for (Constructor m : c.getDeclaredConstructors()) if (m.getParameterTypes().length == 0)
      return m;
    throw fail("No default constructor declared in " + c.getName());
  }

  public static void fillTableWithData(final JTable table, List<List> rows, List<String> colNames) {
    fillTableWithData(table, rows, toStringArray(colNames));
  }

  public static void fillTableWithData(final JTable table, List<List> rows, String... colNames) {
    final DefaultTableModel model = fillTableWithData_makeModel(rows, colNames);
    swingNowOrLater(new Runnable() {

      public void run() {
        try {
          setTableModel(table, model);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "setTableModel(table, model);";
      }
    });
  }

  public static DefaultTableModel fillTableWithData_makeModel(List<List> rows, String... colNames) {
    Object[][] data = new Object[rows.size()][];
    int w = 0;
    for (int i = 0; i < rows.size(); i++) {
      List l = rows.get(i);
      Object[] r = new Object[l.size()];
      for (int j = 0; j < l.size(); j++) {
        Object o = l.get(j);
        if (o instanceof BufferedImage)
          o = imageIcon((BufferedImage) o);
        if (o instanceof RGBImage)
          o = imageIcon((RGBImage) o);
        r[j] = o;
      }
      data[i] = r;
      w = Math.max(w, l.size());
    }
    Object[] columnNames = new Object[w];
    for (int i = 0; i < w; i++) columnNames[i] = i < l(colNames) ? colNames[i] : "?";
    return new DefaultTableModel(data, columnNames) {

      public Class getColumnClass(int column) {
        return or(_getClass(getValueAt(0, column)), String.class);
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
      }
    };
  }

  public static void setOptAll_pcall(Object o, Map<String, Object> fields) {
    if (fields == null)
      return;
    for (String field : keys(fields)) try {
      setOpt(o, field, fields.get(field));
    } catch (Throwable __e) {
      print(exceptionToStringShort(__e));
    }
  }

  public static void setOptAll_pcall(Object o, Object... values) {
    warnIfOddCount(values);
    for (int i = 0; i + 1 < l(values); i += 2) {
      String field = (String) values[i];
      Object value = values[i + 1];
      try {
        setOpt(o, field, value);
      } catch (Throwable __e) {
        print(exceptionToStringShort(__e));
      }
    }
  }

  public static String getType(Object o) {
    return getClassName(o);
  }

  public static List<JComponent> showForm_arrange1(List<List<JComponent>> l) {
    int minW = showForm_leftWidth(l);
    List<JComponent> out = new ArrayList();
    for (List<JComponent> row : l) out.add(westAndCenter(withRightMargin(showForm_gapBetweenColumns, jMinWidth(minW, first(row))), second(row)));
    return out;
  }

  public static boolean setOptAllDyn_debug;

  public static void setOptAllDyn(DynamicObject o, Map<String, Object> fields) {
    if (fields == null)
      return;
    HashMap<String, Field> fieldMap = instanceFieldsMap(o);
    for (Map.Entry<String, Object> e : fields.entrySet()) {
      String field = e.getKey();
      Object val = e.getValue();
      boolean has = fieldMap.containsKey(field);
      if (has)
        setOpt(o, field, val);
      else {
        o.fieldValues.put(intern(field), val);
        if (setOptAllDyn_debug)
          print("setOptAllDyn added dyn " + field + " to " + o + " [value: " + val + ", fieldValues = " + systemHashCode(o.fieldValues) + ", " + struct(keys(o.fieldValues)) + "]");
      }
    }
  }

  public static String fixNewLines(String s) {
    return s.replace("\r\n", "\n").replace("\r", "\n");
  }

  public static Thread newThread(Object runnable) {
    return new Thread(_topLevelErrorHandling(toRunnable(runnable)));
  }

  public static Thread newThread(Object runnable, String name) {
    if (name == null)
      name = defaultThreadName();
    return new Thread(_topLevelErrorHandling(toRunnable(runnable)), name);
  }

  public static Thread newThread(String name, Object runnable) {
    return newThread(runnable, name);
  }

  public static void setTableModel(JTable table, TableModel model) {
    Map<String, Integer> widths = tableColumnWidthsByName(table);
    int[] i = table.getSelectedRows();
    table.setModel(model);
    int n = model.getRowCount();
    ListSelectionModel sel = table.getSelectionModel();
    for (int j = 0; j < i.length; j++) if (i[j] < n)
      sel.addSelectionInterval(i[j], i[j]);
    tableSetColumnPreferredWidths(table, widths);
  }

  public static void deleteConcept(long id) {
    mainConcepts.deleteConcept(id);
  }

  public static void deleteConcept(Concept c) {
    if (c != null)
      c.delete();
  }

  public static void deleteConcept(Concept.Ref ref) {
    if (ref != null)
      deleteConcept(ref.get());
  }

  public static JSplitPane addToWindowSplitRight_f(Component c, Component toAdd, Object f) {
    JFrame frame = getFrame(c);
    JSplitPane sp;
    setContentPane(frame, sp = jhsplit((Component) callF(f, frame.getContentPane()), toAdd));
    return sp;
  }

  public static volatile boolean sleep_noSleep;

  public static void sleep(long ms) {
    ping();
    if (ms < 0)
      return;
    if (isAWTThread() && ms > 100)
      throw fail("Should not sleep on AWT thread");
    try {
      Thread.sleep(ms);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void sleep() {
    try {
      if (sleep_noSleep)
        throw fail("nosleep");
      print("Sleeping.");
      sleepQuietly();
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static boolean structure_isMarker(String s, int i, int j) {
    if (i >= j)
      return false;
    if (s.charAt(i) != 'm')
      return false;
    ++i;
    while (i < j) {
      char c = s.charAt(i);
      if (c < '0' || c > '9')
        return false;
      ++i;
    }
    return true;
  }

  public static BufferedImage newBufferedImage(int w, int h) {
    return new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
  }

  public static BufferedImage newBufferedImage(int w, int h, Color color) {
    BufferedImage img = newBufferedImage(w, h);
    Graphics2D g = img.createGraphics();
    g.setColor(color);
    g.fillRect(0, 0, w, h);
    return img;
  }

  public static BufferedImage newBufferedImage(Pt p, Color color) {
    return newBufferedImage(p.x, p.y, color);
  }

  public static double roundToOneHundredth(double d) {
    return new BigDecimal(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

  public static <A> List<A> reversedList(Collection<A> l) {
    List<A> x = cloneList(l);
    Collections.reverse(x);
    return x;
  }

  public static <A> List<A> subList(List<A> l, int startIndex) {
    return subList(l, startIndex, l(l));
  }

  public static <A> List<A> subList(List<A> l, int startIndex, int endIndex) {
    startIndex = max(0, min(l(l), startIndex));
    endIndex = max(0, min(l(l), endIndex));
    if (startIndex > endIndex)
      return litlist();
    return l.subList(startIndex, endIndex);
  }

  public static String intern(String s) {
    return fastIntern(s);
  }

  public static List collectField(Collection c, String field) {
    List l = new ArrayList();
    for (Object a : c) l.add(getOpt(a, field));
    return l;
  }

  public static List collectField(String field, Collection c) {
    return collectField(c, field);
  }

  public static int l(Object[] a) {
    return a == null ? 0 : a.length;
  }

  public static int l(boolean[] a) {
    return a == null ? 0 : a.length;
  }

  public static int l(byte[] a) {
    return a == null ? 0 : a.length;
  }

  public static int l(int[] a) {
    return a == null ? 0 : a.length;
  }

  public static int l(float[] a) {
    return a == null ? 0 : a.length;
  }

  public static int l(char[] a) {
    return a == null ? 0 : a.length;
  }

  public static int l(Collection c) {
    return c == null ? 0 : c.size();
  }

  public static int l(Map m) {
    return m == null ? 0 : m.size();
  }

  public static int l(CharSequence s) {
    return s == null ? 0 : s.length();
  }

  public static long l(File f) {
    return f == null ? 0 : f.length();
  }

  public static int l(Object o) {
    return o == null ? 0 : o instanceof String ? l((String) o) : o instanceof Map ? l((Map) o) : o instanceof Collection ? l((Collection) o) : (Integer) call(o, "size");
  }

  public static int l(Lisp l) {
    return l == null ? 0 : l.size();
  }

  public static String[] toStringArray(Collection<String> c) {
    String[] a = new String[l(c)];
    Iterator<String> it = c.iterator();
    for (int i = 0; i < l(a); i++) a[i] = it.next();
    return a;
  }

  public static String[] toStringArray(Object o) {
    if (o instanceof String[])
      return (String[]) o;
    else if (o instanceof Collection)
      return toStringArray((Collection<String>) o);
    else
      throw fail("Not a collection or array: " + getClassName(o));
  }

  public static String n(long l, String name) {
    return l + " " + trim(l == 1 ? singular(name) : getPlural(name));
  }

  public static String n(Collection l, String name) {
    return n(l(l), name);
  }

  public static String n(Map m, String name) {
    return n(l(m), name);
  }

  public static String n(Object[] a, String name) {
    return n(l(a), name);
  }

  public static HashMap<Class, Field[]> getDeclaredFields_cache = new HashMap();

  public static Field[] getDeclaredFields_cached(Class c) {
    Field[] fields;
    synchronized (getDeclaredFields_cache) {
      fields = getDeclaredFields_cache.get(c);
      if (fields == null) {
        getDeclaredFields_cache.put(c, fields = c.getDeclaredFields());
        for (Field f : fields) f.setAccessible(true);
      }
    }
    return fields;
  }

  public static Object call(Object o) {
    return callFunction(o);
  }

  public static Object call(Object o, String method, String[] arg) {
    return call(o, method, new Object[] { arg });
  }

  public static Object call(Object o, String method, Object... args) {
    try {
      if (o instanceof Class) {
        Method m = call_findStaticMethod((Class) o, method, args, false);
        m.setAccessible(true);
        return invokeMethod(m, null, args);
      } else {
        Method m = call_findMethod(o, method, args, false);
        m.setAccessible(true);
        return invokeMethod(m, o, args);
      }
    } catch (Exception e) {
      throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
    }
  }

  public static Method call_findStaticMethod(Class c, String method, Object[] args, boolean debug) {
    Class _c = c;
    while (c != null) {
      for (Method m : c.getDeclaredMethods()) {
        if (debug)
          System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");
        ;
        if (!m.getName().equals(method)) {
          if (debug)
            System.out.println("Method name mismatch: " + method);
          continue;
        }
        if ((m.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0 || !call_checkArgs(m, args, debug))
          continue;
        return m;
      }
      c = c.getSuperclass();
    }
    throw new RuntimeException("Method '" + method + "' (static) with " + args.length + " parameter(s) not found in " + _c.getName());
  }

  public static Method call_findMethod(Object o, String method, Object[] args, boolean debug) {
    Class c = o.getClass();
    while (c != null) {
      for (Method m : c.getDeclaredMethods()) {
        if (debug)
          System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");
        ;
        if (m.getName().equals(method) && call_checkArgs(m, args, debug))
          return m;
      }
      c = c.getSuperclass();
    }
    throw new RuntimeException("Method '" + method + "' (non-static) with " + args.length + " parameter(s) not found in " + o.getClass().getName());
  }

  public static boolean call_checkArgs(Method m, Object[] args, boolean debug) {
    Class<?>[] types = m.getParameterTypes();
    if (types.length != args.length) {
      if (debug)
        System.out.println("Bad parameter length: " + args.length + " vs " + types.length);
      return false;
    }
    for (int i = 0; i < types.length; i++) if (!(args[i] == null || isInstanceX(types[i], args[i]))) {
      if (debug)
        System.out.println("Bad parameter " + i + ": " + args[i] + " vs " + types[i]);
      return false;
    }
    return true;
  }

  public static boolean isTrue(Object o) {
    if (o instanceof Boolean)
      return ((Boolean) o).booleanValue();
    if (o == null)
      return false;
    if (o instanceof ThreadLocal)
      return isTrue(((ThreadLocal) o).get());
    throw fail(getClassName(o));
  }

  public static String reversedString(String s) {
    return reverseString(s);
  }

  public static String boolArrayToHex(boolean[] a) {
    return bytesToHex(boolArrayToBytes(a));
  }

  public static Iterator emptyIterator() {
    return Collections.emptyIterator();
  }

  public static boolean match3(String pat, String s) {
    return match3(pat, s, null);
  }

  public static boolean match3(String pat, String s, Matches matches) {
    if (pat == null || s == null)
      return false;
    return match3(pat, parse3_cached(s), matches);
  }

  public static boolean match3(String pat, List<String> toks, Matches matches) {
    List<String> tokpat = parse3(pat);
    return match3(tokpat, toks, matches);
  }

  public static boolean match3(List<String> tokpat, List<String> toks, Matches matches) {
    String[] m = match2(tokpat, toks);
    if (m == null)
      return false;
    if (matches != null)
      matches.m = m;
    return true;
  }

  public static boolean makeAndroid3_disable;

  public static class Android3 {

    public String greeting;

    public boolean publicOverride;

    public int startPort = 5000;

    public Responder responder;

    public boolean console = true;

    public boolean quiet;

    public boolean daemon = false;

    public boolean incomingSilent = false;

    public int incomingPrintLimit = 200;

    public boolean useMultiPort = true;

    public boolean recordHistory;

    public boolean verbose;

    public int answerPrintLimit = 500;

    public boolean newLineAboveAnswer, newLineBelowAnswer;

    public int port;

    public long vport;

    public DialogHandler handler;

    public ServerSocket server;

    public Android3(String greeting) {
      this.greeting = greeting;
    }

    public Android3() {
    }

    public synchronized void dispose() {
      if (server != null) {
        try {
          server.close();
        } catch (IOException e) {
          print("[internal] " + e);
        }
        server = null;
      }
      if (vport != 0)
        try {
          print("Disposing " + this);
          removeFromMultiPort(vport);
          vport = 0;
        } catch (Throwable __e) {
          printStackTrace2(__e);
        }
    }

    public String toString() {
      return "Bot: " + greeting + " [vport " + vport + "]";
    }
  }

  public abstract static class Responder {

    public abstract String answer(String s, List<String> history);
  }

  public static Android3 makeAndroid3(final String greeting) {
    return makeAndroid3(new Android3(greeting));
  }

  public static Android3 makeAndroid3(final String greeting, Responder responder) {
    Android3 android = new Android3(greeting);
    android.responder = responder;
    return makeAndroid3(android);
  }

  public static Android3 makeAndroid3(final Android3 a) {
    if (makeAndroid3_disable)
      return a;
    if (a.responder == null)
      a.responder = new Responder() {

        public String answer(String s, List<String> history) {
          return callStaticAnswerMethod(s, history);
        }
      };
    if (!a.quiet)
      print("[bot] " + a.greeting);
    if (a.console && (readLine_noReadLine || makeAndroid3_consoleInUse()))
      a.console = false;
    record(a);
    if (a.useMultiPort)
      a.vport = addToMultiPort(a.greeting, makeAndroid3_verboseResponder(a));
    if (a.console)
      makeAndroid3_handleConsole(a);
    if (a.useMultiPort)
      return a;
    a.handler = makeAndroid3_makeDialogHandler(a);
    if (a.quiet)
      startDialogServer_quiet.set(true);
    try {
      a.port = a.daemon ? startDialogServerOnPortAboveDaemon(a.startPort, a.handler) : startDialogServerOnPortAbove(a.startPort, a.handler);
    } finally {
      startDialogServer_quiet.set(null);
    }
    a.server = startDialogServer_serverSocket;
    return a;
  }

  public static void makeAndroid3_handleConsole(final Android3 a) {
    if (!a.quiet)
      print("You may also type on this console.");
    {
      Thread _t_0 = new Thread() {

        public void run() {
          try {
            List<String> history = new ArrayList();
            while (licensed()) {
              String line;
              try {
                line = readLine();
              } catch (Throwable e) {
                print(getInnerMessage(e));
                break;
              }
              if (line == null)
                break;
              {
                history.add(line);
                history.add(makeAndroid3_getAnswer(line, history, a));
              }
            }
          } catch (Throwable __e) {
            printStackTrace2(__e);
          }
        }
      };
      startThread(_t_0);
    }
  }

  public static DialogHandler makeAndroid3_makeDialogHandler(final Android3 a) {
    return new DialogHandler() {

      public void run(final DialogIO io) {
        if (!a.publicOverride && !(publicCommOn() || io.isLocalConnection())) {
          io.sendLine("Sorry, not allowed");
          return;
        }
        String dialogID = randomID(8);
        io.sendLine(a.greeting + " / Your ID: " + dialogID);
        List<String> history = new ArrayList();
        while (io.isStillConnected()) {
          if (io.waitForLine()) {
            final String line = io.readLineNoBlock();
            String s = dialogID + " at " + now() + ": " + quote(line);
            if (!a.incomingSilent)
              print(shorten(s, a.incomingPrintLimit));
            if (eq(line, "bye")) {
              io.sendLine("bye stranger");
              return;
            }
            Matches m = new Matches();
            if (a.recordHistory)
              history.add(line);
            String answer;
            if (match3("this is a continuation of talk *", s, m) || match3("hello bot! this is a continuation of talk *", s, m)) {
              dialogID = unquote(m.m[0]);
              answer = "ok";
            } else
              try {
                makeAndroid3_io.set(io);
                answer = makeAndroid3_getAnswer(line, history, a);
              } finally {
                makeAndroid3_io.set(null);
              }
            if (a.recordHistory)
              history.add(answer);
            io.sendLine(answer);
          }
        }
      }
    };
  }

  public static String makeAndroid3_getAnswer(String line, List<String> history, Android3 a) {
    String answer, originalAnswer;
    try {
      originalAnswer = a.responder.answer(line, history);
      answer = makeAndroid3_fallback(line, history, originalAnswer);
    } catch (Throwable e) {
      e = getInnerException(e);
      printStackTrace(e);
      originalAnswer = answer = e.toString();
    }
    if (!a.incomingSilent) {
      if (originalAnswer == null)
        originalAnswer = "?";
      if (a.newLineAboveAnswer)
        print();
      print(">" + dropFirst(indentx(2, shorten(rtrim(originalAnswer), a.answerPrintLimit))));
      if (a.newLineBelowAnswer)
        print();
    }
    return answer;
  }

  public static String makeAndroid3_fallback(String s, List<String> history, String answer) {
    if (answer == null && match3("what is your pid", s))
      return getPID();
    if (answer == null && match3("what is your program id", s))
      return getProgramID();
    if (match3("get injection id", s))
      return getInjectionID();
    if (answer == null)
      answer = "?";
    if (answer.indexOf('\n') >= 0 || answer.indexOf('\r') >= 0)
      answer = quote(answer);
    return answer;
  }

  public static boolean makeAndroid3_consoleInUse() {
    for (Object o : record_list) if (o instanceof Android3 && ((Android3) o).console)
      return true;
    return false;
  }

  public static Responder makeAndroid3_verboseResponder(final Android3 a) {
    return new Responder() {

      public String answer(String s, List<String> history) {
        if (a.verbose)
          print("> " + shorten(s, a.incomingPrintLimit));
        String answer = a.responder.answer(s, history);
        if (a.verbose)
          print("< " + shorten(answer, a.incomingPrintLimit));
        return answer;
      }
    };
  }

  public static ThreadLocal<DialogIO> makeAndroid3_io = new ThreadLocal();

  public static Android3 makeAndroid3() {
    return makeAndroid3(getProgramTitle() + ".");
  }

  public static <A> A callMain(A c, String... args) {
    callOpt(c, "main", new Object[] { args });
    return c;
  }

  public static void callMain() {
    callMain(mc());
  }

  public static Runnable toRunnable(final Object o) {
    if (o instanceof Runnable)
      return (Runnable) o;
    return new Runnable() {

      public void run() {
        try {
          callF(o);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "callF(o)";
      }
    };
  }

  public static <A> A getAndClearThreadLocal(ThreadLocal<A> tl) {
    A a = tl.get();
    tl.set(null);
    return a;
  }

  public static boolean showAnimationInTopRightCorner_alwaysOnTop = true;

  public static JWindow showAnimationInTopRightCorner(String imageID, String text) {
    if (isHeadless())
      return null;
    return showAnimationInTopRightCorner(imageIcon(imageID), text);
  }

  public static JWindow showAnimationInTopRightCorner(final BufferedImage image, final String text) {
    if (isHeadless())
      return null;
    return showAnimationInTopRightCorner(imageIcon(image), text);
  }

  public static JWindow showAnimationInTopRightCorner(final ImageIcon imageIcon, final String text) {
    if (isHeadless())
      return null;
    return (JWindow) swingAndWait(new F0<Object>() {

      public Object get() {
        try {
          JLabel label = new JLabel(imageIcon);
          if (nempty(text)) {
            label.setText(text);
            label.setVerticalTextPosition(SwingConstants.BOTTOM);
            label.setHorizontalTextPosition(SwingConstants.CENTER);
          }
          final JWindow window = showInTopRightCorner(label);
          onClick(label, new Runnable() {

            public void run() {
              try {
                window.dispose();
              } catch (Exception __e) {
                throw rethrow(__e);
              }
            }

            public String toString() {
              return "window.dispose()";
            }
          });
          if (showAnimationInTopRightCorner_alwaysOnTop)
            window.setAlwaysOnTop(true);
          return window;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JLabel label = new JLabel(imageIcon);\r\n    if (nempty(text)) {\r\n      label.s...";
      }
    });
  }

  public static JWindow showAnimationInTopRightCorner(final String imageID) {
    return showAnimationInTopRightCorner(imageID, "");
  }

  public static JWindow showAnimationInTopRightCorner(String imageID, double seconds) {
    return showAnimationInTopRightCorner(imageID, "", seconds);
  }

  public static JWindow showAnimationInTopRightCorner(String imageID, String text, double seconds) {
    if (isHeadless())
      return null;
    return disposeWindowAfter(iround(seconds * 1000), showAnimationInTopRightCorner(imageID, text));
  }

  public static JWindow showAnimationInTopRightCorner(BufferedImage img, String text, double seconds) {
    return disposeWindowAfter(iround(seconds * 1000), showAnimationInTopRightCorner(img, text));
  }

  public static String bytesToHex(byte[] bytes) {
    return bytesToHex(bytes, 0, bytes.length);
  }

  public static String bytesToHex(byte[] bytes, int ofs, int len) {
    StringBuilder stringBuilder = new StringBuilder(len * 2);
    for (int i = 0; i < len; i++) {
      String s = "0" + Integer.toHexString(bytes[ofs + i]);
      stringBuilder.append(s.substring(s.length() - 2, s.length()));
    }
    return stringBuilder.toString();
  }

  public static boolean isInteger(String s) {
    if (s == null)
      return false;
    int n = l(s);
    if (n == 0)
      return false;
    int i = 0;
    if (s.charAt(0) == '-')
      if (++i >= n)
        return false;
    while (i < n) {
      char c = s.charAt(i);
      if (c < '0' || c > '9')
        return false;
      ++i;
    }
    return true;
  }

  public static <A> A oneOf(List<A> l) {
    return l.isEmpty() ? null : l.get(new Random().nextInt(l.size()));
  }

  public static char oneOf(String s) {
    return empty(s) ? '?' : s.charAt(random(l(s)));
  }

  public static String oneOf(String... l) {
    return oneOf(asList(l));
  }

  public static boolean jmatch(String pat, String s) {
    return jmatch(pat, s, null);
  }

  public static boolean jmatch(String pat, String s, Matches matches) {
    if (s == null)
      return false;
    return jmatch(pat, javaTok(s), matches);
  }

  public static boolean jmatch(String pat, List<String> toks) {
    return jmatch(pat, toks, null);
  }

  public static boolean jmatch(String pat, List<String> toks, Matches matches) {
    List<String> tokpat = javaTok(pat);
    String[] m = match2(tokpat, toks);
    if (m == null)
      return false;
    else {
      if (matches != null)
        matches.m = m;
      return true;
    }
  }

  public static void dataToTable_dynSet(List l, int i, Object s) {
    while (i >= l.size()) l.add("");
    l.set(i, s);
  }

  public static List dataToTable_makeRow(Object x, List<String> cols) {
    if (instanceOf(x, "DynamicObject"))
      x = get_raw(x, "fieldValues");
    if (x instanceof Map) {
      Map m = (Map) (x);
      List row = new ArrayList();
      for (Object _field : keysWithoutHidden(m)) {
        String field = (String) (_field);
        Object value = m.get(field);
        int col = cols.indexOf(field);
        if (col < 0) {
          cols.add(field);
          col = cols.size() - 1;
        }
        dataToTable_dynSet(row, col, dataToTable_wrapValue(value));
      }
      return row;
    }
    return litlist(structureOrText(x));
  }

  public static Object dataToTable_wrapValue(Object o) {
    if (o instanceof BufferedImage)
      return o;
    if (o instanceof RGBImage)
      return o;
    if (o instanceof Boolean)
      return o;
    return structureOrText(o);
  }

  public static String formatSnippetID(String id) {
    return "#" + parseSnippetID(id);
  }

  public static String formatSnippetID(long id) {
    return "#" + id;
  }

  public static JComponent withLabel(String label, JComponent component) {
    return westAndCenter(jlabel(label + " "), component);
  }

  public static List<List<JComponent>> showForm_makeComponents(final Var<JFrame> frame, Object... _parts) {
    List<List<JComponent>> l = new ArrayList();
    List parts = asList(_parts);
    Runnable submit = null;
    for (int i = 0; i < l(parts); i++) {
      final Object o = parts.get(i), next = get(parts, i + 1);
      if (o instanceof String && next instanceof Component)
        setComponentID((Component) next, (String) o);
      if (o instanceof Component || o instanceof String || next instanceof Component) {
        l.add(mapLL("wrapForSmartAdd", o == null ? new JPanel() : o, next));
        if (next instanceof JButton && submit == null)
          submit = new Runnable() {

            public void run() {
              try {
                ((JButton) next).doClick();
              } catch (Exception __e) {
                throw rethrow(__e);
              }
            }

            public String toString() {
              return "((JButton) next).doClick()";
            }
          };
        i++;
      } else if (isRunnable(o))
        l.add(mapLL("wrapForSmartAdd", null, jbutton(showFormSubmitButtonName(), submit = new Runnable() {

          public void run() {
            try {
              Object result = call(o);
              if (neq(Boolean.FALSE, result))
                disposeFrame(frame.get());
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "Object result = call(o);\r\n        if (neq(Boolean.FALSE, result))\r\n          ...";
          }
        })));
      else
        print("showForm: Unknown element type: " + getClassName(o));
    }
    onEnterInAllTextFields(concatLists(l), submit);
    for (List<JComponent> row : l) {
      JComponent left = first(row);
      if (left instanceof JLabel)
        makeBold((JLabel) left).setVerticalAlignment(JLabel.TOP);
    }
    return l;
  }

  public static long psI(String snippetID) {
    return parseSnippetID(snippetID);
  }

  public static Producer<String> javaTokC_noMLS_iterator(final String s) {
    return javaTokC_noMLS_iterator(s, 0);
  }

  public static Producer<String> javaTokC_noMLS_iterator(final String s, final int startIndex) {
    return new Producer<String>() {

      public final int l = s.length();

      public int i = startIndex;

      public String next() {
        if (i >= l)
          return null;
        int j = i;
        char c, d;
        while (j < l) {
          c = s.charAt(j);
          d = j + 1 >= l ? '\0' : s.charAt(j + 1);
          if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
            ++j;
          else if (c == '/' && d == '*') {
            do ++j; while (j < l && !s.substring(j, Math.min(j + 2, l)).equals("*/"));
            j = Math.min(j + 2, l);
          } else if (c == '/' && d == '/') {
            do ++j; while (j < l && "\r\n".indexOf(s.charAt(j)) < 0);
          } else
            break;
        }
        i = j;
        if (i >= l)
          return null;
        c = s.charAt(i);
        d = i + 1 >= l ? '\0' : s.charAt(i + 1);
        if (c == '\'' || c == '"') {
          char opener = c;
          ++j;
          while (j < l) {
            if (s.charAt(j) == opener || s.charAt(j) == '\n') {
              ++j;
              break;
            } else if (s.charAt(j) == '\\' && j + 1 < l)
              j += 2;
            else
              ++j;
          }
        } else if (Character.isJavaIdentifierStart(c))
          do ++j; while (j < l && (Character.isJavaIdentifierPart(s.charAt(j)) || "'".indexOf(s.charAt(j)) >= 0));
        else if (Character.isDigit(c)) {
          do ++j; while (j < l && Character.isDigit(s.charAt(j)));
          if (j < l && s.charAt(j) == 'L')
            ++j;
        } else
          ++j;
        String t = quickSubstring(s, i, j);
        i = j;
        return t;
      }
    };
  }

  public static boolean isFalse(Object o) {
    return eq(false, o);
  }

  public static void addToWindowTop(Component c, Component toAdd) {
    JFrame frame = getFrame(c);
    Container cp = frame.getContentPane();
    setContentPane(frame, northAndCenter(toAdd, cp));
  }

  public static boolean eq(Object a, Object b) {
    return a == null ? b == null : a == b || a.equals(b);
  }

  public static boolean warn_on = true;

  public static void warn(String s) {
    if (warn_on)
      print("Warning: " + s);
  }

  public static void warn(String s, List<String> warnings) {
    warn(s);
    if (warnings != null)
      warnings.add(s);
  }

  public static volatile boolean conceptsAndBot_running;

  public static void conceptsAndBot() {
    conceptsAndBot(null);
  }

  public static void conceptsAndBot(Integer autoSaveInterval) {
    if (conceptsAndBot_running)
      return;
    conceptsAndBot_running = true;
    try {
      ensureDBNotRunning(dbBotStandardName());
    } catch (Throwable _e) {
      mainConcepts.dontSave = true;
      throw rethrow(_e);
    }
    if (autoSaveInterval != null)
      loadAndAutoSaveConcepts(autoSaveInterval);
    else
      loadAndAutoSaveConcepts();
    dbBot();
  }

  public static Window getWindow(Object o) {
    if (!(o instanceof Component))
      return null;
    Component c = (Component) o;
    while (c != null) {
      if (c instanceof Window)
        return (Window) c;
      c = c.getParent();
    }
    return null;
  }

  public static void warnIfOddCount(Object... list) {
    if (odd(l(list)))
      printStackTrace("Odd list size: " + list);
  }

  public static String substanceLAF_defaultSkin = "Creme";

  public static void substanceLAF() {
    substanceLAF(null);
  }

  public static void substanceLAF(String skinName) {
    try {
      enableSubstance_impl(or2(skinName, substanceLAF_defaultSkin));
    } catch (Throwable __e) {
      printStackTrace2(__e);
    }
  }

  public static List<String> getTableLine(JTable tbl, int row) {
    if (row >= 0 && row < tbl.getModel().getRowCount()) {
      List<String> l = new ArrayList();
      for (int i = 0; i < tbl.getModel().getColumnCount(); i++) l.add(String.valueOf(tbl.getModel().getValueAt(row, i)));
      return l;
    }
    return null;
  }

  public static String loadCachedTranspilation(String id) {
    return loadTextFilePossiblyGZipped(new File(getCodeProgramDir(id), "Transpilation"));
  }

  public static <A> A collectionGet(Collection<A> c, int idx) {
    if (c == null || idx < 0 || idx >= l(c))
      return null;
    if (c instanceof List)
      return listGet((List<A>) c, idx);
    Iterator<A> it = c.iterator();
    for (int i = 0; i < idx; i++) if (it.hasNext())
      it.next();
    else
      return null;
    return it.hasNext() ? it.next() : null;
  }

  public static int hstackWithSpacing_spacing = 10;

  public static JPanel hstackWithSpacing(Object... parts) {
    parts = flattenArray2(parts);
    int spacing = hstackWithSpacing_spacing;
    int i = 0;
    if (first(parts) instanceof Integer) {
      spacing = toInt(first(parts));
      ++i;
    }
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.VERTICAL;
    gbc.gridheight = GridBagConstraints.REMAINDER;
    for (; i < l(parts); i++) {
      if (i != 0)
        panel.add(javax.swing.Box.createRigidArea(new Dimension(spacing, 0)), gbc);
      panel.add(wrapForSmartAdd(parts[i]), gbc);
    }
    gbc.weightx = 1;
    panel.add(jrigid(), gbc);
    return panel;
  }

  public static String shortClassName(Object o) {
    if (o == null)
      return null;
    Class c = o instanceof Class ? (Class) o : o.getClass();
    String name = c.getName();
    return shortenClassName(name);
  }

  public static String unquote(String s) {
    if (s == null)
      return null;
    if (startsWith(s, '[')) {
      int i = 1;
      while (i < s.length() && s.charAt(i) == '=') ++i;
      if (i < s.length() && s.charAt(i) == '[') {
        String m = s.substring(1, i);
        if (s.endsWith("]" + m + "]"))
          return s.substring(i + 1, s.length() - i - 1);
      }
    }
    if (s.length() > 1) {
      char c = s.charAt(0);
      if (c == '\"' || c == '\'') {
        int l = endsWith(s, c) ? s.length() - 1 : s.length();
        StringBuilder sb = new StringBuilder(l - 1);
        for (int i = 1; i < l; i++) {
          char ch = s.charAt(i);
          if (ch == '\\') {
            char nextChar = (i == l - 1) ? '\\' : s.charAt(i + 1);
            if (nextChar >= '0' && nextChar <= '7') {
              String code = "" + nextChar;
              i++;
              if ((i < l - 1) && s.charAt(i + 1) >= '0' && s.charAt(i + 1) <= '7') {
                code += s.charAt(i + 1);
                i++;
                if ((i < l - 1) && s.charAt(i + 1) >= '0' && s.charAt(i + 1) <= '7') {
                  code += s.charAt(i + 1);
                  i++;
                }
              }
              sb.append((char) Integer.parseInt(code, 8));
              continue;
            }
            switch(nextChar) {
              case '\"':
                ch = '\"';
                break;
              case '\\':
                ch = '\\';
                break;
              case 'b':
                ch = '\b';
                break;
              case 'f':
                ch = '\f';
                break;
              case 'n':
                ch = '\n';
                break;
              case 'r':
                ch = '\r';
                break;
              case 't':
                ch = '\t';
                break;
              case '\'':
                ch = '\'';
                break;
              case 'u':
                if (i >= l - 5) {
                  ch = 'u';
                  break;
                }
                int code = Integer.parseInt("" + s.charAt(i + 2) + s.charAt(i + 3) + s.charAt(i + 4) + s.charAt(i + 5), 16);
                sb.append(Character.toChars(code));
                i += 5;
                continue;
              default:
                ch = nextChar;
            }
            i++;
          }
          sb.append(ch);
        }
        return sb.toString();
      }
    }
    return s;
  }

  public static String programID() {
    return getProgramID();
  }

  public static boolean isEmpty(Collection c) {
    return c == null || c.isEmpty();
  }

  public static boolean isEmpty(CharSequence s) {
    return s == null || s.length() == 0;
  }

  public static boolean isEmpty(Object[] a) {
    return a == null || a.length == 0;
  }

  public static boolean isEmpty(byte[] a) {
    return a == null || a.length == 0;
  }

  public static boolean isEmpty(Map map) {
    return map == null || map.isEmpty();
  }

  public static void stopTimer(java.util.Timer timer) {
    if (timer != null)
      timer.cancel();
  }

  public static MenuItem menuItem(String text, final Object r) {
    MenuItem mi = new MenuItem(text);
    mi.addActionListener(actionListener(r));
    return mi;
  }

  public static JSplitPane setSplitPaneLater(final Component c, final double value) {
    final JSplitPane sp = first(childrenOfType(c, JSplitPane.class));
    if (sp != null)
      swingLater(new Runnable() {

        public void run() {
          try {
            sp.setDividerLocation(value);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "sp.setDividerLocation(value);";
        }
      });
    return sp;
  }

  public static <A extends JComponent> A jMinWidth_pure(int w, A c) {
    Dimension size = c.getMinimumSize();
    c.setMinimumSize(new Dimension(w, size.height));
    return c;
  }

  public static char unquoteCharacter(String s) {
    assertTrue(s.startsWith("'") && s.length() > 1);
    return unquote("\"" + s.substring(1, s.endsWith("'") ? s.length() - 1 : s.length()) + "\"").charAt(0);
  }

  public static void validateFrame(Component c) {
    revalidateFrame(c);
  }

  public static int parseInt(String s) {
    return empty(s) ? 0 : Integer.parseInt(s);
  }

  public static int parseInt(char c) {
    return Integer.parseInt(str(c));
  }

  public static void autoVMExit() {
    call(getJavaX(), "autoVMExit");
  }

  public static String structureOrText(Object o) {
    return o instanceof String ? (String) o : structure(o);
  }

  public static Producer<String> javaTokC_noMLS_onReader(final BufferedReader r) {
    final class X implements Producer<String> {

      public StringBuilder buf = new StringBuilder();

      public char c, d, e = 'x';

      public X() {
        nc();
        nc();
        nc();
      }

      public void nc() {
        try {
          c = d;
          d = e;
          if (e == '\0')
            return;
          int i = r.read();
          e = i < 0 ? '\0' : (char) i;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public void ncSave() {
        if (c != '\0') {
          buf.append(c);
          nc();
        }
      }

      public String next() {
        while (c != '\0') {
          if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
            nc();
          else if (c == '/' && d == '*') {
            do nc(); while (c != '\0' && !(c == '*' && d == '/'));
            nc();
            nc();
          } else if (c == '/' && d == '/') {
            do nc(); while (c != '\0' && "\r\n".indexOf(c) < 0);
          } else
            break;
        }
        if (c == '\0')
          return null;
        if (c == '\'' || c == '"') {
          char opener = c;
          ncSave();
          while (c != '\0') {
            if (c == opener || c == '\n') {
              ncSave();
              break;
            } else if (c == '\\') {
              ncSave();
              ncSave();
            } else
              ncSave();
          }
        } else if (Character.isJavaIdentifierStart(c))
          do ncSave(); while (Character.isJavaIdentifierPart(c) || c == '\'');
        else if (Character.isDigit(c)) {
          do ncSave(); while (Character.isDigit(c));
          if (c == 'L')
            ncSave();
        } else
          ncSave();
        String t = buf.toString();
        buf.setLength(0);
        return t;
      }
    }
    return new X();
  }

  public static <A extends Component> A setFrameSize(A c, int w, int h) {
    JFrame f = getFrame(c);
    if (f != null)
      f.setSize(w, h);
    return c;
  }

  public static <A extends Component> A setFrameSize(int w, int h, A c) {
    return setFrameSize(c, w, h);
  }

  public static Object getOpt(Object o, String field) {
    return getOpt_cached(o, field);
  }

  public static Object getOpt_raw(Object o, String field) {
    try {
      Field f = getOpt_findField(o.getClass(), field);
      if (f == null)
        return null;
      f.setAccessible(true);
      return f.get(o);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Object getOpt(Class c, String field) {
    try {
      if (c == null)
        return null;
      Field f = getOpt_findStaticField(c, field);
      if (f == null)
        return null;
      f.setAccessible(true);
      return f.get(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Field getOpt_findStaticField(Class<?> c, String field) {
    Class _c = c;
    do {
      for (Field f : _c.getDeclaredFields()) if (f.getName().equals(field) && (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0)
        return f;
      _c = _c.getSuperclass();
    } while (_c != null);
    return null;
  }

  public static <A> A popLast(List<A> l) {
    return liftLast(l);
  }

  public static <A> A nuStubInnerObject(Class<A> c) {
    try {
      Class outerType = getOuterClass(c);
      Constructor m = c.getDeclaredConstructor(outerType);
      m.setAccessible(true);
      return (A) m.newInstance(new Object[] { null });
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static Map litmap(Object... x) {
    HashMap map = new HashMap();
    litmap_impl(map, x);
    return map;
  }

  public static void litmap_impl(Map map, Object... x) {
    for (int i = 0; i < x.length - 1; i += 2) if (x[i + 1] != null)
      map.put(x[i], x[i + 1]);
  }

  public static String formatDate() {
    return formatDate(now());
  }

  public static String formatDate(long timestamp) {
    return timestamp == 0 ? "-" : str(new Date(timestamp));
  }

  public static JFrame consoleFrame() {
    return (JFrame) getOpt(get(getJavaX(), "console"), "frame");
  }

  public static boolean[] boolArrayFromBytes(byte[] a, int n) {
    boolean[] b = new boolean[n];
    int m = min(n, l(a) * 8);
    for (int i = 0; i < m; i++) b[i] = (a[i / 8] & 1 << (i & 7)) != 0;
    return b;
  }

  public static JMenu getMenuNamed(JMenuBar bar, String name) {
    int n = bar.getMenuCount();
    for (int i = 0; i < n; i++) {
      JMenu m = bar.getMenu(i);
      if (m != null && eq(m.getText(), name))
        return m;
    }
    return null;
  }

  public static int shorten_default = 100;

  public static String shorten(String s) {
    return shorten(s, shorten_default);
  }

  public static String shorten(String s, int max) {
    return shorten(s, max, "...");
  }

  public static String shorten(String s, int max, String shortener) {
    if (s == null)
      return "";
    if (max < 0)
      return s;
    return s.length() <= max ? s : substring(s, 0, min(s.length(), max - l(shortener))) + shortener;
  }

  public static String shorten(int max, String s) {
    return shorten(s, max);
  }

  public static void swingLater(long delay, final Object r) {
    javax.swing.Timer timer = new javax.swing.Timer(toInt(delay), actionListener(wrapAsActivity(r)));
    timer.setRepeats(false);
    timer.start();
  }

  public static void swingLater(Object r) {
    SwingUtilities.invokeLater(toRunnable(r));
  }

  public static <A> A swingNu(final Class<A> c, final Object... args) {
    return swingConstruct(c, args);
  }

  public static String getStackTrace2(Throwable e) {
    return hideCredentials(getStackTrace(unwrapTrivialExceptionWraps(e)) + replacePrefix("java.lang.RuntimeException: ", "FAIL: ", hideCredentials(str(getInnerException(e)))) + "\n");
  }

  public static void swingAndWait(Runnable r) {
    try {
      if (isAWTThread())
        r.run();
      else
        EventQueue.invokeAndWait(r);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static Object swingAndWait(final Object f) {
    if (isAWTThread())
      return callF(f);
    else {
      final Var result = new Var();
      swingAndWait(new Runnable() {

        public void run() {
          try {
            result.set(callF(f));
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "result.set(callF(f));";
        }
      });
      return result.get();
    }
  }

  public static String dropPrefix(String prefix, String s) {
    return s == null ? null : s.startsWith(prefix) ? s.substring(l(prefix)) : s;
  }

  public static String unquoteUsingCharArray(String s, char[] buf) {
    if (s == null)
      return null;
    if (startsWith(s, '[')) {
      int i = 1;
      while (i < s.length() && s.charAt(i) == '=') ++i;
      if (i < s.length() && s.charAt(i) == '[') {
        String m = s.substring(1, i);
        if (s.endsWith("]" + m + "]"))
          return s.substring(i + 1, s.length() - i - 1);
      }
    }
    if (s.length() > 1) {
      char c = s.charAt(0);
      if (c == '\"' || c == '\'') {
        int l = endsWith(s, c) ? s.length() - 1 : s.length();
        if (l > buf.length)
          return unquote(s);
        int n = 0;
        for (int i = 1; i < l; i++) {
          char ch = s.charAt(i);
          if (ch == '\\') {
            char nextChar = (i == l - 1) ? '\\' : s.charAt(i + 1);
            if (nextChar >= '0' && nextChar <= '7') {
              String code = "" + nextChar;
              i++;
              if ((i < l - 1) && s.charAt(i + 1) >= '0' && s.charAt(i + 1) <= '7') {
                code += s.charAt(i + 1);
                i++;
                if ((i < l - 1) && s.charAt(i + 1) >= '0' && s.charAt(i + 1) <= '7') {
                  code += s.charAt(i + 1);
                  i++;
                }
              }
              buf[n++] = (char) Integer.parseInt(code, 8);
              continue;
            }
            switch(nextChar) {
              case '\"':
                ch = '\"';
                break;
              case '\\':
                ch = '\\';
                break;
              case 'b':
                ch = '\b';
                break;
              case 'f':
                ch = '\f';
                break;
              case 'n':
                ch = '\n';
                break;
              case 'r':
                ch = '\r';
                break;
              case 't':
                ch = '\t';
                break;
              case '\'':
                ch = '\'';
                break;
              case 'u':
                if (i >= l - 5) {
                  ch = 'u';
                  break;
                }
                int code = Integer.parseInt("" + s.charAt(i + 2) + s.charAt(i + 3) + s.charAt(i + 4) + s.charAt(i + 5), 16);
                char[] x = Character.toChars(code);
                int lx = x.length;
                for (int j = 0; j < lx; j++) buf[n++] = x[j];
                i += 5;
                continue;
              default:
                ch = nextChar;
            }
            i++;
          }
          buf[n++] = ch;
        }
        return new String(buf, 0, n);
      }
    }
    return s;
  }

  public static JMenuItem jmenuItem(String text, final Object r) {
    JMenuItem mi = new JMenuItem(text);
    mi.addActionListener(actionListener(r));
    return mi;
  }

  public static String defaultThreadName_name;

  public static String defaultThreadName() {
    if (defaultThreadName_name == null)
      defaultThreadName_name = "A thread by " + programID();
    return defaultThreadName_name;
  }

  public static HashMap<String, Class> findClass_cache = new HashMap();

  public static Class findClass(String name) {
    synchronized (findClass_cache) {
      if (findClass_cache.containsKey(name))
        return findClass_cache.get(name);
      if (!isJavaIdentifier(name))
        return null;
      Class c;
      try {
        c = Class.forName("main$" + name);
      } catch (ClassNotFoundException e) {
        c = null;
      }
      findClass_cache.put(name, c);
      return c;
    }
  }

  public static ThreadLocal<Boolean> doPost_silently = new ThreadLocal();

  public static ThreadLocal<Long> doPost_timeout = new ThreadLocal();

  public static String doPost(Map urlParameters, String url) {
    return doPost(makePostData(urlParameters), url);
  }

  public static String doPost(String urlParameters, String url) {
    try {
      URL _url = new URL(url);
      ping();
      return doPost(urlParameters, _url.openConnection(), _url);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String doPost(String urlParameters, URLConnection conn, URL url) {
    try {
      boolean silently = isTrue(optParam(doPost_silently));
      Long timeout = optParam(doPost_timeout);
      setHeaders(conn);
      int l = lUtf8(urlParameters);
      if (!silently)
        print("Sending POST request: " + url + " (" + l + " bytes)");
      if (timeout != null)
        setURLConnectionTimeouts(conn, timeout);
      ((HttpURLConnection) conn).setRequestMethod("POST");
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Length", str(l));
      OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
      writer.write(urlParameters);
      writer.flush();
      loadPage_charset.set("UTF-8");
      String contents = loadPage(conn, url, false);
      writer.close();
      return contents;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static Field setOpt_findField(Class c, String field) {
    HashMap<String, Field> map;
    synchronized (getOpt_cache) {
      map = getOpt_cache.get(c);
      if (map == null)
        map = getOpt_makeCache(c);
    }
    return map.get(field);
  }

  public static void setOpt(Object o, String field, Object value) {
    try {
      if (o == null)
        return;
      Class c = o.getClass();
      HashMap<String, Field> map;
      synchronized (getOpt_cache) {
        map = getOpt_cache.get(c);
        if (map == null)
          map = getOpt_makeCache(c);
      }
      if (map == getOpt_special) {
        if (o instanceof Class) {
          setOpt((Class) o, field, value);
          return;
        }
        setOpt_raw(o, field, value);
        return;
      }
      Field f = map.get(field);
      if (f != null)
        smartSet(f, o, value);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void setOpt(Class c, String field, Object value) {
    if (c == null)
      return;
    try {
      Field f = setOpt_findStaticField(c, field);
      if (f != null)
        smartSet(f, null, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Field setOpt_findStaticField(Class<?> c, String field) {
    Class _c = c;
    do {
      for (Field f : _c.getDeclaredFields()) if (f.getName().equals(field) && (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0) {
        f.setAccessible(true);
        return f;
      }
      _c = _c.getSuperclass();
    } while (_c != null);
    return null;
  }

  public static boolean isSubtypeOf(Class a, Class b) {
    return b.isAssignableFrom(a);
  }

  public static RuntimeException fail() {
    throw new RuntimeException("fail");
  }

  public static RuntimeException fail(Throwable e) {
    throw asRuntimeException(e);
  }

  public static RuntimeException fail(Object msg) {
    throw new RuntimeException(String.valueOf(msg));
  }

  public static RuntimeException fail(String msg) {
    throw new RuntimeException(msg == null ? "" : msg);
  }

  public static RuntimeException fail(String msg, Throwable innerException) {
    throw new RuntimeException(msg, innerException);
  }

  public static boolean showTable_searcher = true;

  public static JTable showTable(Object data) {
    return dataToTable_uneditable(data);
  }

  public static JTable showTable(String title, Object data) {
    return showTable(data, title);
  }

  public static JTable showTable(Object data, String title) {
    return dataToTable_uneditable(data, title);
  }

  public static JTable showTable(JTable table, Object data) {
    return showTable(table, data, autoFrameTitle());
  }

  public static JTable showTable(Object data, JTable table) {
    return showTable(table, data);
  }

  public static JTable showTable(JTable table, Object data, String title) {
    if (table == null)
      table = showTable(data, title);
    else {
      setFrameTitle(table, title);
      dataToTable_uneditable(table, data);
    }
    return table;
  }

  public static JTable showTable() {
    return showTable(new ArrayList<List<String>>(), new ArrayList<String>());
  }

  public static JTable showTable(String title) {
    return showTable(new ArrayList<List<String>>(), new ArrayList<String>(), title);
  }

  public static JTable showTable(List<List<String>> rows, List<String> cols) {
    return showTable(rows, cols, autoFrameTitle());
  }

  public static JTable showTable(List<List<String>> rows, List<String> cols, String title) {
    JTable tbl = sexyTable();
    fillTableWithStrings(tbl, rows, cols);
    showFrame(title, tbl);
    return tbl;
  }

  public static void swingNowOrLater(Runnable r) {
    if (isAWTThread())
      r.run();
    else
      swingLater(r);
  }

  public static TableWithTooltips tableWithToolTips() {
    return tableWithTooltips();
  }

  public static java.util.Timer doLater_daemon(long delay, final Object r) {
    final java.util.Timer timer = new java.util.Timer(true);
    timer.schedule(timerTask(r), delay);
    return timer;
  }

  public static java.util.Timer doLater_daemon(double delaySeconds, final Object r) {
    return doLater_daemon(toMS(delaySeconds), r);
  }

  public static boolean isJavaIdentifier(String s) {
    if (empty(s) || !Character.isJavaIdentifierStart(s.charAt(0)))
      return false;
    for (int i = 1; i < s.length(); i++) if (!Character.isJavaIdentifierPart(s.charAt(i)))
      return false;
    return true;
  }

  public static float parseFloat(String s) {
    return Float.parseFloat(s);
  }

  public static void restartWithDelay(int delay) {
    Object j = getJavaX();
    call(j, "preKill");
    call(j, "nohupJavax", smartJoin((String[]) get(j, "fullArgs")), call(j, "fullVMArguments"));
    sleep(delay);
    System.exit(0);
    sleep();
  }

  public static volatile boolean ping_pauseAll;

  public static int ping_sleep = 100;

  public static volatile boolean ping_anyActions;

  public static Map<Thread, Object> ping_actions = newWeakHashMap();

  public static boolean ping() {
    if (ping_pauseAll || ping_anyActions)
      ping_impl();
    return true;
  }

  public static boolean ping_impl() {
    try {
      if (ping_pauseAll && !isAWTThread()) {
        do Thread.sleep(ping_sleep); while (ping_pauseAll);
        return true;
      }
      if (ping_anyActions) {
        Object action;
        synchronized (ping_actions) {
          action = ping_actions.get(currentThread());
          if (action instanceof Runnable)
            ping_actions.remove(currentThread());
          if (ping_actions.isEmpty())
            ping_anyActions = false;
        }
        if (action instanceof Runnable)
          ((Runnable) action).run();
        else if (eq(action, "cancelled"))
          throw fail("Thread cancelled.");
      }
      return false;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static List parseList(String s) {
    return (List) safeUnstructure(s);
  }

  public static boolean isSnippetID(String s) {
    try {
      parseSnippetID(s);
      return true;
    } catch (RuntimeException e) {
      return false;
    }
  }

  public static int loadPage_defaultTimeout = 60000;

  public static ThreadLocal<String> loadPage_charset = new ThreadLocal();

  public static boolean loadPage_allowGzip = true, loadPage_debug;

  public static boolean loadPage_anonymous;

  public static int loadPage_verboseness = 100000;

  public static int loadPage_retries = 1;

  public static ThreadLocal<Boolean> loadPage_silent = new ThreadLocal();

  public static volatile int loadPage_forcedTimeout;

  public static ThreadLocal<Integer> loadPage_forcedTimeout_byThread = new ThreadLocal();

  public static ThreadLocal<Map<String, List<String>>> loadPage_responseHeaders = new ThreadLocal();

  public static ThreadLocal<Map<String, String>> loadPage_extraHeaders = new ThreadLocal();

  public static String loadPageSilently(String url) {
    try {
      return loadPageSilently(new URL(loadPage_preprocess(url)));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String loadPageSilently(URL url) {
    try {
      if (url.getProtocol().equals("https"))
        disableCertificateValidation();
      IOException e = null;
      for (int tries = 0; tries < loadPage_retries; tries++) try {
        URLConnection con = loadPage_openConnection(url);
        return loadPage(con, url);
      } catch (IOException _e) {
        e = _e;
        if (loadPageThroughProxy_enabled) {
          print("Trying proxy because of: " + e);
          try {
            return loadPageThroughProxy(str(url));
          } catch (Throwable e2) {
            print("  " + exceptionToStringShort(e2));
          }
        } else if (loadPage_debug)
          print(e);
        if (tries < loadPage_retries - 1)
          sleepSeconds(1);
      }
      throw e;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String loadPage_preprocess(String url) {
    if (url.startsWith("tb/"))
      url = tb_mainServer() + "/" + url;
    if (url.indexOf("://") < 0)
      url = "http://" + url;
    return url;
  }

  public static String loadPage(String url) {
    try {
      url = loadPage_preprocess(url);
      if (!isTrue(loadPage_silent.get()))
        printWithTime("Loading: " + hideCredentials(url));
      return loadPageSilently(new URL(url));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String loadPage(URL url) {
    return loadPage(url.toExternalForm());
  }

  public static String loadPage(URLConnection con, URL url) throws IOException {
    return loadPage(con, url, true);
  }

  public static String loadPage(URLConnection con, URL url, boolean addHeaders) throws IOException {
    Map<String, String> extraHeaders = getAndClearThreadLocal(loadPage_extraHeaders);
    if (addHeaders)
      try {
        if (!loadPage_anonymous)
          setHeaders(con);
        if (loadPage_allowGzip)
          con.setRequestProperty("Accept-Encoding", "gzip");
        con.setRequestProperty("X-No-Cookies", "1");
        for (String key : keys(extraHeaders)) con.setRequestProperty(key, extraHeaders.get(key));
      } catch (Throwable e) {
      }
    loadPage_responseHeaders.set(con.getHeaderFields());
    String contentType = con.getContentType();
    if (contentType == null) {
      throw new IOException("Page could not be read: " + url);
    }
    String charset = loadPage_charset == null ? null : loadPage_charset.get();
    if (charset == null)
      charset = loadPage_guessCharset(contentType);
    InputStream in = con.getInputStream();
    try {
      if ("gzip".equals(con.getContentEncoding())) {
        if (loadPage_debug)
          print("loadPage: Using gzip.");
        in = newGZIPInputStream(in);
      }
      Reader r = new InputStreamReader(in, charset);
      StringBuilder buf = new StringBuilder();
      int n = 0;
      while (true) {
        int ch = r.read();
        if (ch < 0)
          break;
        buf.append((char) ch);
        ++n;
        if ((n % loadPage_verboseness) == 0)
          print("  " + n + " chars read");
      }
      return buf.toString();
    } finally {
      in.close();
    }
  }

  public static String loadPage_guessCharset(String contentType) {
    Pattern p = Pattern.compile("text/[a-z]+;\\s*charset=([^\\s]+)\\s*");
    Matcher m = p.matcher(contentType);
    String match = m.matches() ? m.group(1) : null;
    if (loadPage_debug)
      print("loadPage: contentType=" + contentType + ", match: " + match);
    return or(match, "UTF-8");
  }

  public static URLConnection loadPage_openConnection(URL url) {
    URLConnection con = openConnection(url);
    int timeout = toInt(loadPage_forcedTimeout_byThread.get());
    if (timeout == 0)
      timeout = loadPage_forcedTimeout;
    if (timeout != 0)
      setURLConnectionTimeouts(con, loadPage_forcedTimeout);
    else
      setURLConnectionDefaultTimeouts(con, loadPage_defaultTimeout);
    return con;
  }

  public static JComponent showTitledForm(String title, Object... _parts) {
    return showFormTitled(title, _parts);
  }

  public static JPanel northAndCenter(Component n, Component c) {
    return centerAndNorth(c, n);
  }

  public static Frame getAWTFrame(final Object _o) {
    return swing(new F0<Frame>() {

      public Frame get() {
        try {
          Object o = _o;
          if (o instanceof ButtonGroup)
            o = first(buttonsInGroup((ButtonGroup) o));
          if (!(o instanceof Component))
            return null;
          Component c = (Component) o;
          while (c != null) {
            if (c instanceof Frame)
              return (Frame) c;
            c = c.getParent();
          }
          return null;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "O o = _o;\r\n    /*\r\n    ifdef HaveProcessing\r\n      if (o instanceof PApplet) ...";
      }
    });
  }

  public static void assertTrue(Object o) {
    if (!(eq(o, true)))
      throw fail(str(o));
  }

  public static boolean assertTrue(String msg, boolean b) {
    if (!b)
      throw fail(msg);
    return b;
  }

  public static boolean assertTrue(boolean b) {
    if (!b)
      throw fail("oops");
    return b;
  }

  public static <A> ArrayList<A> litlist(A... a) {
    ArrayList l = new ArrayList(a.length);
    for (A x : a) l.add(x);
    return l;
  }

  public static <A> A get(List<A> l, int idx) {
    return l != null && idx >= 0 && idx < l(l) ? l.get(idx) : null;
  }

  public static <A> A get(A[] l, int idx) {
    return idx >= 0 && idx < l(l) ? l[idx] : null;
  }

  public static boolean get(boolean[] l, int idx) {
    return idx >= 0 && idx < l(l) ? l[idx] : false;
  }

  public static Object get(Object o, String field) {
    try {
      if (o instanceof Class)
        return get((Class) o, field);
      if (o instanceof Map)
        return ((Map) o).get(field);
      Field f = getOpt_findField(o.getClass(), field);
      if (f != null) {
        f.setAccessible(true);
        return f.get(o);
      }
      if (o instanceof DynamicObject)
        return ((DynamicObject) o).fieldValues.get(field);
    } catch (Exception e) {
      throw asRuntimeException(e);
    }
    throw new RuntimeException("Field '" + field + "' not found in " + o.getClass().getName());
  }

  public static Object get_raw(Object o, String field) {
    try {
      Field f = get_findField(o.getClass(), field);
      f.setAccessible(true);
      return f.get(o);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Object get(Class c, String field) {
    try {
      Field f = get_findStaticField(c, field);
      f.setAccessible(true);
      return f.get(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Field get_findStaticField(Class<?> c, String field) {
    Class _c = c;
    do {
      for (Field f : _c.getDeclaredFields()) if (f.getName().equals(field) && (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0)
        return f;
      _c = _c.getSuperclass();
    } while (_c != null);
    throw new RuntimeException("Static field '" + field + "' not found in " + c.getName());
  }

  public static Field get_findField(Class<?> c, String field) {
    Class _c = c;
    do {
      for (Field f : _c.getDeclaredFields()) if (f.getName().equals(field))
        return f;
      _c = _c.getSuperclass();
    } while (_c != null);
    throw new RuntimeException("Field '" + field + "' not found in " + c.getName());
  }

  public static long toMS(double seconds) {
    return (long) (seconds * 1000);
  }

  public static int makeBot(String greeting) {
    return makeAndroid3(greeting).port;
  }

  public static Android3 makeBot(Android3 a) {
    makeAndroid3(a);
    return a;
  }

  public static Android3 makeBot(String greeting, Object responder) {
    Android3 a = new Android3(greeting);
    a.responder = makeResponder(responder);
    makeBot(a);
    return a;
  }

  public static Android3 makeBot() {
    return makeAndroid3(defaultBotName());
  }

  public static boolean isLongConstant(String s) {
    if (!s.endsWith("L"))
      return false;
    s = s.substring(0, l(s) - 1);
    return isInteger(s);
  }

  public static String autoFrameTitle_value;

  public static String autoFrameTitle() {
    return autoFrameTitle_value != null ? autoFrameTitle_value : getProgramTitle();
  }

  public static void autoFrameTitle(Component c) {
    setFrameTitle(getFrame(c), autoFrameTitle());
  }

  public static JFrame showFrame() {
    return makeFrame();
  }

  public static JFrame showFrame(Object content) {
    return makeFrame(content);
  }

  public static JFrame showFrame(String title) {
    return makeFrame(title);
  }

  public static JFrame showFrame(String title, Object content) {
    return makeFrame(title, content);
  }

  public static JFrame showFrame(final JFrame f) {
    if (f != null) {
      swing(new Runnable() {

        public void run() {
          try {
            if (frameTooSmall(f))
              frameStandardSize(f);
            if (!f.isVisible())
              f.setVisible(true);
            if (f.getState() == Frame.ICONIFIED)
              f.setState(Frame.NORMAL);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "if (frameTooSmall(f)) frameStandardSize(f);\r\n    if (!f.isVisible()) f.setVis...";
        }
      });
    }
    return f;
  }

  public static JFrame showFrame(String title, Object content, JFrame frame) {
    if (frame == null)
      return showFrame(title, content);
    else {
      frame.setTitle(title);
      setFrameContents(frame, content);
      return frame;
    }
  }

  public static String internIfLongerThan(String s, int l) {
    return s == null ? null : l(s) >= l ? intern(s) : s;
  }

  public static void pcallOpt_noArgs(Object o, String method) {
    try {
      callOpt_noArgs(o, method);
    } catch (Throwable __e) {
      printStackTrace2(__e);
    }
  }

  public static void fillJMenu(JMenu m, Object... x) {
    if (x == null)
      return;
    for (int i = 0; i < l(x); i++) {
      Object o = x[i], y = get(x, i + 1);
      if (o instanceof List)
        fillJMenu(m, asArray((List) o));
      else if (eqOneOf(o, "***", "---", "===", ""))
        m.addSeparator();
      else if (o instanceof String && isRunnableX(y)) {
        m.add(jmenuItem((String) o, y));
        ++i;
      } else if (o instanceof JMenuItem)
        m.add((JMenuItem) o);
      else if (o instanceof String || o instanceof Action || o instanceof Component)
        call(m, "add", o);
      else
        print("Unknown menu item: " + o);
    }
  }

  public static List<String> javaTokC(String s) {
    if (s == null)
      return null;
    int l = s.length();
    ArrayList<String> tok = new ArrayList();
    int i = 0;
    while (i < l) {
      int j = i;
      char c, d;
      while (j < l) {
        c = s.charAt(j);
        d = j + 1 >= l ? '\0' : s.charAt(j + 1);
        if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
          ++j;
        else if (c == '/' && d == '*') {
          do ++j; while (j < l && !s.substring(j, Math.min(j + 2, l)).equals("*/"));
          j = Math.min(j + 2, l);
        } else if (c == '/' && d == '/') {
          do ++j; while (j < l && "\r\n".indexOf(s.charAt(j)) < 0);
        } else
          break;
      }
      i = j;
      if (i >= l)
        break;
      c = s.charAt(i);
      d = i + 1 >= l ? '\0' : s.charAt(i + 1);
      if (c == '\'' || c == '"') {
        char opener = c;
        ++j;
        while (j < l) {
          if (s.charAt(j) == opener || s.charAt(j) == '\n') {
            ++j;
            break;
          } else if (s.charAt(j) == '\\' && j + 1 < l)
            j += 2;
          else
            ++j;
        }
      } else if (Character.isJavaIdentifierStart(c))
        do ++j; while (j < l && (Character.isJavaIdentifierPart(s.charAt(j)) || "'".indexOf(s.charAt(j)) >= 0));
      else if (Character.isDigit(c)) {
        do ++j; while (j < l && Character.isDigit(s.charAt(j)));
        if (j < l && s.charAt(j) == 'L')
          ++j;
      } else if (c == '[' && d == '[') {
        do ++j; while (j + 1 < l && !s.substring(j, j + 2).equals("]]"));
        j = Math.min(j + 2, l);
      } else if (c == '[' && d == '=' && i + 2 < l && s.charAt(i + 2) == '[') {
        do ++j; while (j + 2 < l && !s.substring(j, j + 3).equals("]=]"));
        j = Math.min(j + 3, l);
      } else
        ++j;
      tok.add(javaTok_substringC(s, i, j));
      i = j;
    }
    return tok;
  }

  public static <A> A assertEquals(Object x, A y) {
    return assertEquals(null, x, y);
  }

  public static <A> A assertEquals(String msg, Object x, A y) {
    if (!(x == null ? y == null : x.equals(y)))
      throw fail((msg != null ? msg + ": " : "") + y + " != " + x);
    return y;
  }

  public static String autoRestart_localMD5;

  public static String autoRestart_localMD5() {
    if (autoRestart_localMD5 == null)
      autoRestart_localMD5 = md5(loadCachedTranspilation(programID()));
    return autoRestart_localMD5;
  }

  public static String programTitle() {
    return getProgramName();
  }

  public static long parseLong(String s) {
    if (s == null)
      return 0;
    return Long.parseLong(dropSuffix("L", s));
  }

  public static long parseLong(Object s) {
    return Long.parseLong((String) s);
  }

  public static JButton newButton(final String text, final Object action) {
    return swing(new F0<JButton>() {

      public JButton get() {
        try {
          JButton btn = new JButton(text);
          if (action != null)
            btn.addActionListener(actionListener(action, btn));
          return btn;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JButton btn = new JButton(text);\r\n    // submitButtonOnEnter(btn); // test th...";
      }
    });
  }

  public static void onUpdate(JComponent c, final Object r) {
    if (c instanceof JTextComponent)
      ((JTextComponent) c).getDocument().addDocumentListener(new DocumentListener() {

        public void insertUpdate(DocumentEvent e) {
          call(r);
        }

        public void removeUpdate(DocumentEvent e) {
          call(r);
        }

        public void changedUpdate(DocumentEvent e) {
          call(r);
        }
      });
    else if (c instanceof ItemSelectable)
      ((ItemSelectable) c).addItemListener(new ItemListener() {

        public void itemStateChanged(ItemEvent e) {
          call(r);
        }
      });
    else
      print("Warning: onUpdate doesn't know " + getClassName(c));
  }

  public static void onUpdate(List<? extends JComponent> l, Object r) {
    for (JComponent c : l) onUpdate(c, r);
  }

  public static void messageBox(final String msg) {
    if (headless())
      print(msg);
    else {
      swing(new Runnable() {

        public void run() {
          try {
            JOptionPane.showMessageDialog(null, msg, "JavaX", JOptionPane.INFORMATION_MESSAGE);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "JOptionPane.showMessageDialog(null, msg, \"JavaX\", JOptionPane.INFORMATION_MES...";
        }
      });
    }
  }

  public static void messageBox(Throwable e) {
    showConsole();
    printStackTrace(e);
    messageBox(hideCredentials(innerException(e)));
  }

  public static String md5(String text) {
    try {
      if (text == null)
        return "-";
      return bytesToHex(md5_impl(text.getBytes("UTF-8")));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String md5(byte[] data) {
    if (data == null)
      return "-";
    return bytesToHex(md5_impl(data));
  }

  public static MessageDigest md5_md;

  public static byte[] md5_impl(byte[] data) {
    try {
      return MessageDigest.getInstance("MD5").digest(data);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String md5(File file) {
    try {
      return md5(loadBinaryFile(file));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static <A> List<A> ll(A... a) {
    ArrayList l = new ArrayList(a.length);
    for (A x : a) l.add(x);
    return l;
  }

  public static AtomicLong _handleError_nonVMErrors = new AtomicLong();

  public static AtomicLong _handleError_vmErrors = new AtomicLong();

  public static AtomicLong _handleError_outOfMemoryErrors = new AtomicLong();

  public static volatile long _handleError_lastOutOfMemoryError;

  public static volatile Error _handleError_lastHardError;

  public static void _handleError(Error e) {
    if (!(e instanceof VirtualMachineError)) {
      incAtomicLong(_handleError_nonVMErrors);
      return;
    }
    print("\nHARD ERROR\n");
    printStackTrace2(e);
    print("\nHARD ERROR\n");
    _handleError_lastHardError = e;
    incAtomicLong(_handleError_vmErrors);
    if (e instanceof OutOfMemoryError) {
      incAtomicLong(_handleError_outOfMemoryErrors);
      _handleError_lastOutOfMemoryError = sysNow();
    }
  }

  public static Object pcallF(Object f, Object... args) {
    return pcallFunction(f, args);
  }

  public static <A> A pcallF(F0<A> f) {
    try {
      return f == null ? null : f.get();
    } catch (Throwable __e) {
      return null;
    }
  }

  public static <A, B> B pcallF(F1<A, B> f, A a) {
    try {
      return f == null ? null : f.get(a);
    } catch (Throwable __e) {
      return null;
    }
  }

  public static JPanel jFullCenter(final Component c) {
    return swing(new F0<JPanel>() {

      public JPanel get() {
        try {
          JPanel panel = new JPanel(new GridBagLayout());
          panel.add(c);
          return panel;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JPanel panel = new JPanel(new GridBagLayout);\r\n    panel.add(c);\r\n    ret panel;";
      }
    });
  }

  public static boolean anyValueContainsIgnoreCase(Map map, String pat) {
    for (Object val : values(map)) if (val instanceof String && containsIgnoreCase((String) val, pat))
      return true;
    return false;
  }

  public static Class mc() {
    return main.class;
  }

  public static int[] toIntArray(List<Integer> l) {
    int[] a = new int[l(l)];
    for (int i = 0; i < a.length; i++) a[i] = l.get(i);
    return a;
  }

  public static boolean neq(Object a, Object b) {
    return !eq(a, b);
  }

  public static List<Map<String, Object>> rawTableData(JTable t) {
    int n = tableRows(t);
    List l = new ArrayList();
    for (int i = 0; i < n; i++) l.add(rawTableLineAsMap(t, i));
    return l;
  }

  public static <A> int indexOf(List<A> l, A a, int startIndex) {
    if (l == null)
      return -1;
    for (int i = startIndex; i < l(l); i++) if (eq(l.get(i), a))
      return i;
    return -1;
  }

  public static <A> int indexOf(List<A> l, int startIndex, A a) {
    return indexOf(l, a, startIndex);
  }

  public static <A> int indexOf(List<A> l, A a) {
    if (l == null)
      return -1;
    return l.indexOf(a);
  }

  public static int indexOf(String a, String b) {
    return a == null || b == null ? -1 : a.indexOf(b);
  }

  public static int indexOf(String a, String b, int i) {
    return a == null || b == null ? -1 : a.indexOf(b, i);
  }

  public static int indexOf(String a, char b) {
    return a == null ? -1 : a.indexOf(b);
  }

  public static int indexOf(String a, int i, char b) {
    return indexOf(a, b, i);
  }

  public static int indexOf(String a, char b, int i) {
    return a == null ? -1 : a.indexOf(b, i);
  }

  public static int indexOf(String a, int i, String b) {
    return a == null || b == null ? -1 : a.indexOf(b, i);
  }

  public static <A> int indexOf(A[] x, A a) {
    if (x == null)
      return -1;
    for (int i = 0; i < l(x); i++) if (eq(x[i], a))
      return i;
    return -1;
  }

  public static void quoteToPrintWriter(String s, PrintWriter out) {
    if (s == null) {
      out.print("null");
      return;
    }
    out.print('"');
    int l = s.length();
    for (int i = 0; i < l; i++) {
      char c = s.charAt(i);
      if (c == '\\' || c == '"') {
        out.print('\\');
        out.print(c);
      } else if (c == '\r')
        out.print("\\r");
      else if (c == '\n')
        out.print("\\n");
      else
        out.print(c);
    }
    out.print('"');
  }

  public static void printStructure(String prefix, Object o) {
    if (endsWithLetter(prefix))
      prefix += ": ";
    print(prefix + structureForUser(o));
  }

  public static void printStructure(Object o) {
    print(structureForUser(o));
  }

  public static <A, B> void mapPut2(Map<A, B> map, A key, B value) {
    if (map != null && key != null)
      if (value != null)
        map.put(key, value);
      else
        map.remove(key);
  }

  public static JButton setButtonImage(BufferedImage img, JButton btn) {
    btn.setIcon(imageIcon(img));
    return btn;
  }

  public static JButton setButtonImage(JButton btn, BufferedImage img) {
    return setButtonImage(img, btn);
  }

  public static boolean eqOneOf(Object o, Object... l) {
    for (Object x : l) if (eq(o, x))
      return true;
    return false;
  }

  public static boolean isMain() {
    return isMainProgram();
  }

  public static void preloadProgramTitle() {
    {
      Thread _t_0 = new Thread() {

        public void run() {
          try {
            programTitle();
          } catch (Throwable __e) {
            printStackTrace2(__e);
          }
        }
      };
      startThread(_t_0);
    }
  }

  public static String strUnnull(Object o) {
    return o == null ? "" : str(o);
  }

  public static String jextract(String pat, String s) {
    return jextract(pat, javaTok(s));
  }

  public static String jextract(String pat, List<String> tok) {
    List<String> tokpat = javaTok(pat);
    jfind_preprocess(tokpat);
    int i = jfind(tok, tokpat);
    if (i < 0)
      return null;
    int j = i + l(tokpat) - 2;
    return join(subList(tok, i, j));
  }

  public static Object nuObject(String className, Object... args) {
    try {
      return nuObject(classForName(className), args);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static <A> A nuObject(Class<A> c, Object... args) {
    try {
      if (args.length == 0)
        return nuObjectWithoutArguments(c);
      Constructor m = nuObject_findConstructor(c, args);
      m.setAccessible(true);
      return (A) m.newInstance(args);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static Constructor nuObject_findConstructor(Class c, Object... args) {
    for (Constructor m : c.getDeclaredConstructors()) {
      if (!nuObject_checkArgs(m.getParameterTypes(), args, false))
        continue;
      return m;
    }
    throw fail("Constructor " + c.getName() + getClasses(args) + " not found" + (args.length == 0 && (c.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0 ? " - hint: it's a non-static class!" : ""));
  }

  public static boolean nuObject_checkArgs(Class[] types, Object[] args, boolean debug) {
    if (types.length != args.length) {
      if (debug)
        System.out.println("Bad parameter length: " + args.length + " vs " + types.length);
      return false;
    }
    for (int i = 0; i < types.length; i++) if (!(args[i] == null || isInstanceX(types[i], args[i]))) {
      if (debug)
        System.out.println("Bad parameter " + i + ": " + args[i] + " vs " + types[i]);
      return false;
    }
    return true;
  }

  public static int vstackWithSpacing_default = 10;

  public static JPanel vstackWithSpacing(final List parts, final int spacing) {
    return swing(new F0<JPanel>() {

      public JPanel get() {
        try {
          JPanel panel = new JPanel(new GridBagLayout());
          GridBagConstraints gbc = new GridBagConstraints();
          gbc.weightx = 1;
          gbc.fill = GridBagConstraints.HORIZONTAL;
          gbc.gridwidth = GridBagConstraints.REMAINDER;
          gbc.insets = new Insets(spacing / 2, 0, spacing / 2, 0);
          smartAddWithLayout(panel, gbc, toObjectArray(parts));
          gbc.weighty = 1;
          gbc.insets = new Insets(0, 0, 0, 0);
          panel.add(jrigid(), gbc);
          return panel;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JPanel panel = new JPanel(new GridBagLayout);\r\n    new GridBagConstraints gbc...";
      }
    });
  }

  public static JPanel vstackWithSpacing(Component... parts) {
    return vstackWithSpacing(asList(parts), vstackWithSpacing_default);
  }

  public static String getClassName(Object o) {
    return o == null ? "null" : o instanceof Class ? ((Class) o).getName() : o.getClass().getName();
  }

  public static boolean isMainProgram() {
    return creator() == null;
  }

  public static Object deref(Object o) {
    if (o instanceof Derefable)
      o = ((Derefable) o).get();
    return o;
  }

  public static <A, B> Set<A> keys(Map<A, B> map) {
    return map == null ? new HashSet() : map.keySet();
  }

  public static Set keys(Object map) {
    return keys((Map) map);
  }

  public static Object callOpt(Object o) {
    if (o == null)
      return null;
    return callF(o);
  }

  public static Object callOpt(Object o, String method, Object... args) {
    try {
      if (o == null)
        return null;
      if (o instanceof Class) {
        Method m = callOpt_findStaticMethod((Class) o, method, args, false);
        if (m == null)
          return null;
        m.setAccessible(true);
        return invokeMethod(m, null, args);
      } else {
        Method m = callOpt_findMethod(o, method, args, false);
        if (m == null)
          return null;
        m.setAccessible(true);
        return invokeMethod(m, o, args);
      }
    } catch (Exception e) {
      throw rethrow(e);
    }
  }

  public static Method callOpt_findStaticMethod(Class c, String method, Object[] args, boolean debug) {
    Class _c = c;
    while (c != null) {
      for (Method m : c.getDeclaredMethods()) {
        if (debug)
          System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");
        ;
        if (!m.getName().equals(method)) {
          if (debug)
            System.out.println("Method name mismatch: " + method);
          continue;
        }
        if ((m.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0 || !callOpt_checkArgs(m, args, debug))
          continue;
        return m;
      }
      c = c.getSuperclass();
    }
    return null;
  }

  public static Method callOpt_findMethod(Object o, String method, Object[] args, boolean debug) {
    Class c = o.getClass();
    while (c != null) {
      for (Method m : c.getDeclaredMethods()) {
        if (debug)
          System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");
        ;
        if (m.getName().equals(method) && callOpt_checkArgs(m, args, debug))
          return m;
      }
      c = c.getSuperclass();
    }
    return null;
  }

  public static boolean callOpt_checkArgs(Method m, Object[] args, boolean debug) {
    Class<?>[] types = m.getParameterTypes();
    if (types.length != args.length) {
      if (debug)
        System.out.println("Bad parameter length: " + args.length + " vs " + types.length);
      return false;
    }
    for (int i = 0; i < types.length; i++) if (!(args[i] == null || isInstanceX(types[i], args[i]))) {
      if (debug)
        System.out.println("Bad parameter " + i + ": " + args[i] + " vs " + types[i]);
      return false;
    }
    return true;
  }

  public static JFrame getFrame(final Object _o) {
    return swing(new F0<JFrame>() {

      public JFrame get() {
        try {
          Object o = _o;
          if (o instanceof ButtonGroup)
            o = first(buttonsInGroup((ButtonGroup) o));
          if (!(o instanceof Component))
            return null;
          Component c = (Component) o;
          while (c != null) {
            if (c instanceof JFrame)
              return (JFrame) c;
            c = c.getParent();
          }
          return null;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "O o = _o;\r\n    if (o instanceof ButtonGroup) o = first(buttonsInGroup((Button...";
      }
    });
  }

  public static void copyImageToClipboard(Image img) {
    TransferableImage trans = new TransferableImage(img);
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
    print("Copied image to clipboard (" + img.getWidth(null) + "*" + img.getHeight(null) + " px)");
  }

  public static void callMainAsChild(Object child, String... args) {
    moveThisThreadToChild(child);
    callMain(child, args);
  }

  public static <A> A restructure(A a) {
    return (A) unstructure(structure(a));
  }

  public static String substring(String s, int x) {
    return substring(s, x, l(s));
  }

  public static String substring(String s, int x, int y) {
    if (s == null)
      return null;
    if (x < 0)
      x = 0;
    if (x >= s.length())
      return "";
    if (y < x)
      y = x;
    if (y > s.length())
      y = s.length();
    return s.substring(x, y);
  }

  public static double parseDouble(String s) {
    return Double.parseDouble(s);
  }

  public static JFrame showForm_makeFrame(String title, JPanel panel) {
    return handleEscapeKey(minFrameWidth(showPackedFrame(title, withMargin(panel)), 400));
  }

  public static Object derefRef(Object o) {
    if (o instanceof Concept.Ref)
      o = ((Concept.Ref) o).get();
    return o;
  }

  public static <A extends Concept> Object[] expandParams(Class<A> c, Object[] params) {
    if (l(params) == 1)
      params = new Object[] { singleFieldName(c), params[0] };
    else
      warnIfOddCount(params);
    return params;
  }

  public static Class<?> hotwire(String src) {
    assertFalse(_inCore());
    Class j = getJavaX();
    if (isAndroid()) {
      synchronized (j) {
        List<File> libraries = new ArrayList<File>();
        File srcDir = (File) call(j, "transpileMain", src, libraries);
        if (srcDir == null)
          throw fail("transpileMain returned null (src=" + quote(src) + ")");
        Object androidContext = get(j, "androidContext");
        return (Class) call(j, "loadx2android", srcDir, src);
      }
    } else {
      Class c = (Class) (call(j, "hotwire", src));
      hotwire_copyOver(c);
      return c;
    }
  }

  public static <A> A heldInstance(Class<A> c) {
    List<Object> l = holdInstance_l.get();
    for (int i = l(l) - 1; i >= 0; i--) {
      Object o = l.get(i);
      if (isInstanceOf(o, c))
        return (A) o;
    }
    throw fail("No instance of " + className(c) + " held");
  }

  public static boolean isMD5(String s) {
    return l(s) == 32 && isLowerHexString(s);
  }

  public static void revalidateFrame(Component c) {
    revalidate(getFrame(c));
  }

  public static String jlabel_textAsHTML_center_ifNeeded(String text) {
    if (swic(text, "<html>") && ewic(text, "</html>"))
      return text;
    if (!containsNewLines(text))
      return text;
    return jlabel_textAsHTML_center(text);
  }

  public static int randomID_defaultLength = 12;

  public static String randomID(int length) {
    return makeRandomID(length);
  }

  public static String randomID() {
    return randomID(randomID_defaultLength);
  }

  public static String getProgramName_cache;

  public static synchronized String getProgramName() {
    if (getProgramName_cache == null)
      getProgramName_cache = getSnippetTitleOpt(programID());
    return getProgramName_cache;
  }

  public static int stdcompare(Number a, Number b) {
    return cmp(a, b);
  }

  public static int stdcompare(String a, String b) {
    return cmp(a, b);
  }

  public static int stdcompare(long a, long b) {
    return a < b ? -1 : a > b ? 1 : 0;
  }

  public static int stdcompare(Object a, Object b) {
    return cmp(a, b);
  }

  public static <A> A swingConstruct(final Class<A> c, final Object... args) {
    return swing(new F0<A>() {

      public A get() {
        try {
          return nuObject(c, args);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "nuObject(c, args)";
      }
    });
  }

  public static boolean startsWith(String a, String b) {
    return a != null && a.startsWith(b);
  }

  public static boolean startsWith(String a, char c) {
    return nempty(a) && a.charAt(0) == c;
  }

  public static boolean startsWith(String a, String b, Matches m) {
    if (!startsWith(a, b))
      return false;
    m.m = new String[] { substring(a, l(b)) };
    return true;
  }

  public static boolean startsWith(List a, List b) {
    if (a == null || l(b) > l(a))
      return false;
    for (int i = 0; i < l(b); i++) if (neq(a.get(i), b.get(i)))
      return false;
    return true;
  }

  public static <A extends Window> A disposeWindowAfter(int delay, final A w) {
    if (w != null)
      swingLater(delay, new Runnable() {

        public void run() {
          try {
            w.dispose();
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "w.dispose();";
        }
      });
    return w;
  }

  public static <A extends Window> A disposeWindowAfter(A w, double seconds) {
    return disposeWindowAfter(toMS_int(seconds), w);
  }

  public static String reverseString(String s) {
    return new StringBuilder(s).reverse().toString();
  }

  public static void printWithTime(String s) {
    print(hmsWithColons() + ": " + s);
  }

  public static boolean isRunnableX(Object o) {
    if (o == null)
      return false;
    if (o instanceof String)
      return hasMethod(mc(), (String) o);
    return o instanceof Runnable || hasMethod(o, "get");
  }

  public static List<Object> record_list = synchroList();

  public static void record(Object o) {
    record_list.add(o);
  }

  public static boolean isLowerHexString(String s) {
    for (int i = 0; i < l(s); i++) {
      char c = s.charAt(i);
      if (c >= '0' && c <= '9' || c >= 'a' && c <= 'f') {
      } else
        return false;
    }
    return true;
  }

  public static Throwable printStackTrace(Throwable e) {
    print(getStackTrace(e));
    return e;
  }

  public static void printStackTrace() {
    printStackTrace(new Throwable());
  }

  public static void printStackTrace(String msg) {
    printStackTrace(new Throwable(msg));
  }

  public static List<String> getPlural_specials = ll("sheep", "fish");

  public static String getPlural(String s) {
    if (containsIgnoreCase(getPlural_specials, s))
      return s;
    if (ewic(s, "y"))
      return dropSuffixIgnoreCase("y", s) + "ies";
    if (ewic(s, "ss"))
      return s + "es";
    if (ewic(s, "s"))
      return s;
    return s + "s";
  }

  public static String replacePrefix(String prefix, String replacement, String s) {
    if (!startsWith(s, prefix))
      return s;
    return replacement + substring(s, l(prefix));
  }

  public static JWindow showInTopRightCorner(Component c) {
    JWindow w = new JWindow();
    w.add(c);
    w.pack();
    moveToTopRightCorner(w);
    w.setVisible(true);
    return w;
  }

  public static boolean endsWith(String a, String b) {
    return a != null && a.endsWith(b);
  }

  public static boolean endsWith(String a, char c) {
    return nempty(a) && lastChar(a) == c;
  }

  public static boolean endsWith(String a, String b, Matches m) {
    if (!endsWith(a, b))
      return false;
    m.m = new String[] { dropLast(l(b), a) };
    return true;
  }

  public static String quickSubstring(String s, int i, int j) {
    if (i == j)
      return "";
    return s.substring(i, j);
  }

  public static boolean isRunnable(Object o) {
    return o instanceof Runnable || hasMethod(o, "get");
  }

  public static void showConsole() {
    showFrame(consoleFrame());
  }

  public static int javaTok_n, javaTok_elements;

  public static boolean javaTok_opt;

  public static List<String> javaTok(String s) {
    ++javaTok_n;
    ArrayList<String> tok = new ArrayList();
    int l = s.length();
    int i = 0, n = 0;
    while (i < l) {
      int j = i;
      char c, d;
      while (j < l) {
        c = s.charAt(j);
        d = j + 1 >= l ? '\0' : s.charAt(j + 1);
        if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
          ++j;
        else if (c == '/' && d == '*') {
          do ++j; while (j < l && !s.substring(j, Math.min(j + 2, l)).equals("*/"));
          j = Math.min(j + 2, l);
        } else if (c == '/' && d == '/') {
          do ++j; while (j < l && "\r\n".indexOf(s.charAt(j)) < 0);
        } else
          break;
      }
      tok.add(javaTok_substringN(s, i, j));
      ++n;
      i = j;
      if (i >= l)
        break;
      c = s.charAt(i);
      d = i + 1 >= l ? '\0' : s.charAt(i + 1);
      if (c == '\'' && Character.isJavaIdentifierStart(d) && i + 2 < l && "'\\".indexOf(s.charAt(i + 2)) < 0) {
        j += 2;
        while (j < l && Character.isJavaIdentifierPart(s.charAt(j))) ++j;
      } else if (c == '\'' || c == '"') {
        char opener = c;
        ++j;
        while (j < l) {
          int c2 = s.charAt(j);
          if (c2 == opener || c2 == '\n' && opener == '\'') {
            ++j;
            break;
          } else if (c2 == '\\' && j + 1 < l)
            j += 2;
          else
            ++j;
        }
      } else if (Character.isJavaIdentifierStart(c))
        do ++j; while (j < l && (Character.isJavaIdentifierPart(s.charAt(j)) || "'".indexOf(s.charAt(j)) >= 0));
      else if (Character.isDigit(c)) {
        do ++j; while (j < l && Character.isDigit(s.charAt(j)));
        if (j < l && s.charAt(j) == 'L')
          ++j;
      } else if (c == '[' && d == '[') {
        do ++j; while (j + 1 < l && !s.substring(j, j + 2).equals("]]"));
        j = Math.min(j + 2, l);
      } else if (c == '[' && d == '=' && i + 2 < l && s.charAt(i + 2) == '[') {
        do ++j; while (j + 2 < l && !s.substring(j, j + 3).equals("]=]"));
        j = Math.min(j + 3, l);
      } else
        ++j;
      tok.add(javaTok_substringC(s, i, j));
      ++n;
      i = j;
    }
    if ((tok.size() % 2) == 0)
      tok.add("");
    javaTok_elements += tok.size();
    return tok;
  }

  public static List<String> javaTok(List<String> tok) {
    return javaTokWithExisting(join(tok), tok);
  }

  public static <A> List<A> childrenOfType(Component c, Class<A> theClass) {
    List<A> l = new ArrayList();
    scanForComponents(c, theClass, l);
    return l;
  }

  public static String unCurlyBracket(String s) {
    return tok_unCurlyBracket(s);
  }

  public static List<AbstractButton> buttonsInGroup(ButtonGroup g) {
    if (g == null)
      return ll();
    return asList(g.getElements());
  }

  public static ActionListener actionListener(final Object runnable) {
    return actionListener(runnable, null);
  }

  public static ActionListener actionListener(final Object runnable, final Object instanceToHold) {
    if (runnable instanceof ActionListener)
      return (ActionListener) runnable;
    return new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent _evt) {
        try {
          AutoCloseable __411 = holdInstance(instanceToHold);
          try {
            callF(runnable);
          } finally {
            _close(__411);
          }
        } catch (Throwable __e) {
          messageBox(__e);
        }
      }
    };
  }

  public static void incAtomicLong(AtomicLong l) {
    l.incrementAndGet();
  }

  public static void setComponentID(Component c, String id) {
    if (c != null)
      componentID_map.put(c, id);
  }

  public static Class __javax;

  public static Class getJavaX() {
    try {
      if (__javax == null)
        __javax = Class.forName("x30");
      return __javax;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void setHeaders(URLConnection con) throws IOException {
    String computerID = getComputerID_quick();
    if (computerID != null)
      try {
        con.setRequestProperty("X-ComputerID", computerID);
        con.setRequestProperty("X-OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
      } catch (Throwable e) {
      }
  }

  public static volatile boolean licensed_yes = true;

  public static boolean licensed() {
    ping();
    return licensed_yes;
  }

  public static void licensed_off() {
    licensed_yes = false;
  }

  public static String exceptionToStringShort(Throwable e) {
    lastException(e);
    e = getInnerException(e);
    String msg = unnull(e.getMessage());
    if (msg.indexOf("Error") < 0 && msg.indexOf("Exception") < 0)
      return baseClassName(e) + prependIfNempty(": ", msg);
    else
      return msg;
  }

  public static Throwable getInnerException(Throwable e) {
    while (e.getCause() != null) e = e.getCause();
    return e;
  }

  public static Map<Class, Constructor> nuObjectWithoutArguments_cache = newDangerousWeakHashMap();

  public static Object nuObjectWithoutArguments(String className) {
    try {
      return nuObjectWithoutArguments(classForName(className));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static <A> A nuObjectWithoutArguments(Class<A> c) {
    try {
      Constructor m;
      m = nuObjectWithoutArguments_cache.get(c);
      if (m == null)
        nuObjectWithoutArguments_cache.put(c, m = nuObjectWithoutArguments_findConstructor(c));
      return (A) m.newInstance();
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static Constructor nuObjectWithoutArguments_findConstructor(Class c) {
    for (Constructor m : c.getDeclaredConstructors()) if (empty(m.getParameterTypes())) {
      m.setAccessible(true);
      return m;
    }
    throw fail("No default constructor found in " + c.getName());
  }

  public static <A> A optParam(ThreadLocal<A> tl, A defaultValue) {
    return optPar(tl, defaultValue);
  }

  public static <A> A optParam(ThreadLocal<A> tl) {
    return optPar(tl);
  }

  public static HashMap<String, Field> instanceFieldsMap(Object o) {
    Class c = o.getClass();
    HashMap<String, Field> map;
    synchronized (getOpt_cache) {
      map = getOpt_cache.get(c);
      if (map == null)
        map = getOpt_makeCache(c);
    }
    return map;
  }

  public static JComponent wrap(Object swingable) {
    if (swingable == null)
      return null;
    JComponent c = (JComponent) (swingable instanceof JComponent ? swingable : callOpt(swingable, "swing"));
    if (c instanceof JTable || c instanceof JList || c instanceof JTextArea || c instanceof JEditorPane || c instanceof JTextPane || c instanceof JTree)
      return jscroll(c);
    return c;
  }

  public static String[] match2(List<String> pat, List<String> tok) {
    int i = pat.indexOf("...");
    if (i < 0)
      return match2_match(pat, tok);
    pat = new ArrayList<String>(pat);
    pat.set(i, "*");
    while (pat.size() < tok.size()) {
      pat.add(i, "*");
      pat.add(i + 1, "");
    }
    return match2_match(pat, tok);
  }

  public static String[] match2_match(List<String> pat, List<String> tok) {
    List<String> result = new ArrayList<String>();
    if (pat.size() != tok.size()) {
      return null;
    }
    for (int i = 1; i < pat.size(); i += 2) {
      String p = pat.get(i), t = tok.get(i);
      if (eq(p, "*"))
        result.add(t);
      else if (!equalsIgnoreCase(unquote(p), unquote(t)))
        return null;
    }
    return result.toArray(new String[result.size()]);
  }

  public static Boolean isHeadless_cache;

  public static boolean isHeadless() {
    if (isHeadless_cache != null)
      return isHeadless_cache;
    if (isAndroid())
      return isHeadless_cache = true;
    if (GraphicsEnvironment.isHeadless())
      return isHeadless_cache = true;
    try {
      SwingUtilities.isEventDispatchThread();
      return isHeadless_cache = false;
    } catch (Throwable e) {
      return isHeadless_cache = true;
    }
  }

  public static Class<?> _getClass(String name) {
    try {
      return Class.forName(name);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  public static Class _getClass(Object o) {
    return o == null ? null : o instanceof Class ? (Class) o : o.getClass();
  }

  public static Class _getClass(Object realm, String name) {
    try {
      return getClass(realm).getClassLoader().loadClass(classNameToVM(name));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static boolean instanceOf(Object o, String className) {
    if (o == null)
      return false;
    String c = o.getClass().getName();
    return eq(c, className) || eq(c, "main$" + className);
  }

  public static boolean instanceOf(Object o, Class c) {
    if (c == null)
      return false;
    return c.isInstance(o);
  }

  public static String getStackTrace(Throwable throwable) {
    lastException(throwable);
    return getStackTrace_noRecord(throwable);
  }

  public static String getStackTrace_noRecord(Throwable throwable) {
    StringWriter writer = new StringWriter();
    throwable.printStackTrace(new PrintWriter(writer));
    return hideCredentials(writer.toString());
  }

  public static String getStackTrace() {
    return getStackTrace_noRecord(new Throwable());
  }

  public static String getProgramTitle() {
    return getProgramName();
  }

  public static String uploadToImageServer_new(BufferedImage img, String name) {
    byte[] imgData = toPNG(img);
    return uploadToImageServer_rawBytes(imgData, name);
  }

  public static File getCodeProgramDir() {
    return getCodeProgramDir(getProgramID());
  }

  public static File getCodeProgramDir(String snippetID) {
    return new File(javaxCodeDir(), formatSnippetID(snippetID));
  }

  public static File getCodeProgramDir(long snippetID) {
    return getCodeProgramDir(formatSnippetID(snippetID));
  }

  public static URLConnection openConnection(URL url) {
    try {
      ping();
      callOpt(javax(), "recordOpenURLConnection", str(url));
      return url.openConnection();
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void setContentPane(final JFrame frame, final Container c) {
    {
      swing(new Runnable() {

        public void run() {
          try {
            frame.setContentPane(c);
            revalidate(frame);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "frame.setContentPane(c);\r\n    revalidate(frame);";
        }
      });
    }
  }

  public static JFrame setFrameIconLater(Component c, final String imageID) {
    final JFrame frame = getFrame(c);
    if (frame != null) {
      Thread _t_0 = new Thread("Loading Icon") {

        public void run() {
          try {
            final Image i = imageIcon(or2(imageID, "#1005557")).getImage();
            swingLater(new Runnable() {

              public void run() {
                try {
                  frame.setIconImage(i);
                } catch (Exception __e) {
                  throw rethrow(__e);
                }
              }

              public String toString() {
                return "frame.setIconImage(i);";
              }
            });
          } catch (Throwable __e) {
            printStackTrace2(__e);
          }
        }
      };
      startThread(_t_0);
    }
    return frame;
  }

  public static File infoBoxesLogFile() {
    return new File(javaxDataDir(), "Logs/infoBoxes.txt");
  }

  public static AtomicInteger dialogServer_clients = new AtomicInteger();

  public static boolean dialogServer_printConnects;

  public static ThreadLocal<Boolean> startDialogServer_quiet = new ThreadLocal();

  public static Set<String> dialogServer_knownClients = synchroTreeSet();

  public static int startDialogServerOnPortAbove(int port, DialogHandler handler) {
    while (!forbiddenPort(port) && !startDialogServerIfPortAvailable(port, handler)) ++port;
    return port;
  }

  public static int startDialogServerOnPortAboveDaemon(int port, DialogHandler handler) {
    while (!forbiddenPort(port) && !startDialogServerIfPortAvailable(port, handler, true)) ++port;
    return port;
  }

  public static void startDialogServer(int port, DialogHandler handler) {
    if (!startDialogServerIfPortAvailable(port, handler))
      throw fail("Can't start dialog server on port " + port);
  }

  public static boolean startDialogServerIfPortAvailable(int port, final DialogHandler handler) {
    return startDialogServerIfPortAvailable(port, handler, false);
  }

  public static ServerSocket startDialogServer_serverSocket;

  public static boolean startDialogServerIfPortAvailable(int port, final DialogHandler handler, boolean daemon) {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      return false;
    }
    final ServerSocket _serverSocket = serverSocket;
    startDialogServer_serverSocket = serverSocket;
    Thread thread = new Thread("Socket accept port " + port) {

      public void run() {
        try {
          while (true) {
            try {
              final Socket s = _serverSocket.accept();
              String client = s.getInetAddress().toString();
              if (!dialogServer_knownClients.contains(client) && neq(client, "/127.0.0.1")) {
                print("connect from " + client + " - clients: " + dialogServer_clients.incrementAndGet());
                dialogServer_knownClients.add(client);
              }
              String threadName = "Handling client " + s.getInetAddress();
              Thread t2 = new Thread(threadName) {

                public void run() {
                  try {
                    final Writer w = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
                    final BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
                    DialogIO io = new DialogIO() {

                      public boolean isLocalConnection() {
                        return s.getInetAddress().isLoopbackAddress();
                      }

                      public boolean isStillConnected() {
                        return !(eos || s.isClosed());
                      }

                      public void sendLine(String line) {
                        try {
                          w.write(line + "\n");
                          w.flush();
                        } catch (Exception __e) {
                          throw rethrow(__e);
                        }
                      }

                      public String readLineImpl() {
                        try {
                          return in.readLine();
                        } catch (Exception __e) {
                          throw rethrow(__e);
                        }
                      }

                      public void close() {
                        try {
                          s.close();
                        } catch (IOException e) {
                        }
                      }

                      public Socket getSocket() {
                        return s;
                      }
                    };
                    try {
                      handler.run(io);
                    } finally {
                      s.close();
                    }
                  } catch (IOException e) {
                    print("[internal] " + e);
                  } finally {
                  }
                }
              };
              t2.setDaemon(true);
              t2.start();
            } catch (SocketTimeoutException e) {
            }
          }
        } catch (IOException e) {
          print("[internal] " + e);
        }
      }
    };
    if (daemon)
      thread.setDaemon(true);
    thread.start();
    if (!isTrue(getAndClearThreadLocal(startDialogServer_quiet)))
      print("Dialog server on port " + port + " started.");
    return true;
  }

  public static void frameStandardSize(JFrame frame) {
    frame.setBounds(300, 100, 500, 400);
  }

  public static void logQuotedWithDate(String s) {
    logQuotedWithTime(s);
  }

  public static void logQuotedWithDate(String logFile, String s) {
    logQuotedWithTime(logFile, s);
  }

  public static void logQuotedWithDate(File logFile, String s) {
    logQuotedWithTime(logFile, s);
  }

  public static JFrame showPackedFrame(String title, Component contents) {
    return packFrame(showFrame(title, contents));
  }

  public static JFrame showPackedFrame(Component contents) {
    return packFrame(showFrame(contents));
  }

  public static String md5OfRGBImage(RGBImage img) {
    try {
      MessageDigest m = MessageDigest.getInstance("MD5");
      m.update(intToBytes(img.getWidth()));
      int[] pixels = img.getPixels();
      for (int i = 0; i < l(pixels); i++) m.update(intToBytes(pixels[i]));
      return bytesToHex(m.digest());
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String makeRandomID(int length) {
    Random random = new Random();
    char[] id = new char[length];
    for (int i = 0; i < id.length; i++) id[i] = (char) ((int) 'a' + random.nextInt(26));
    return new String(id);
  }

  public static boolean containsIgnoreCase(Collection<String> l, String s) {
    for (String x : l) if (eqic(x, s))
      return true;
    return false;
  }

  public static boolean containsIgnoreCase(String[] l, String s) {
    for (String x : l) if (eqic(x, s))
      return true;
    return false;
  }

  public static boolean containsIgnoreCase(String s, char c) {
    return indexOfIgnoreCase(s, String.valueOf(c)) >= 0;
  }

  public static boolean containsIgnoreCase(String a, String b) {
    return indexOfIgnoreCase(a, b) >= 0;
  }

  public static GZIPInputStream newGZIPInputStream(File f) {
    return gzInputStream(f);
  }

  public static GZIPInputStream newGZIPInputStream(InputStream in) {
    return gzInputStream(in);
  }

  public static byte[] loadBinaryFile(String fileName) {
    try {
      if (!new File(fileName).exists())
        return null;
      FileInputStream in = new FileInputStream(fileName);
      byte[] buf = new byte[1024];
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int l;
      while (true) {
        l = in.read(buf);
        if (l <= 0)
          break;
        out.write(buf, 0, l);
      }
      in.close();
      return out.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] loadBinaryFile(File file) {
    return loadBinaryFile(file.getPath());
  }

  public static Runnable _topLevelErrorHandling(final Runnable runnable) {
    return new Runnable() {

      public void run() {
        try {
          try {
            runnable.run();
          } catch (Throwable __e) {
            printStackTrace2(__e);
          }
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "pcall { runnable.run(); }";
      }
    };
  }

  public static int showForm_leftWidth(List<List<JComponent>> l) {
    int minW = 0;
    for (List<JComponent> row : l) minW = max(minW, getMinimumSize(first(row)).width);
    return minW;
  }

  public static String defaultBotName() {
    return getProgramTitle() + ".";
  }

  public static String struct(Object o) {
    return structure(o);
  }

  public static String struct(Object o, structure_Data data) {
    return structure(o, data);
  }

  public static Class getOuterClass(Class c) {
    try {
      String s = c.getName();
      int i = s.lastIndexOf('$');
      return Class.forName(substring(s, 0, i));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static <A> ArrayList<A> cloneList(Collection<A> l) {
    if (l == null)
      return new ArrayList();
    synchronized (collectionMutex(l)) {
      return new ArrayList<A>(l);
    }
  }

  public static JTextArea jtextarea() {
    return jTextArea();
  }

  public static JTextArea jtextarea(String text) {
    return jTextArea(text);
  }

  public static Object safeUnstructure(String s) {
    return unstructure(s, true);
  }

  public static boolean odd(int i) {
    return (i & 1) != 0;
  }

  public static boolean odd(long i) {
    return (i & 1) != 0;
  }

  public static void setEnabled(final JComponent c, final boolean enable) {
    if (c != null) {
      swing(new Runnable() {

        public void run() {
          try {
            c.setEnabled(enable);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "c.setEnabled(enable);";
        }
      });
    }
  }

  public static List _registerWeakMap_preList;

  public static <A> A _registerWeakMap(A map) {
    if (javax() == null) {
      if (_registerWeakMap_preList == null)
        _registerWeakMap_preList = synchroList();
      _registerWeakMap_preList.add(map);
      return map;
    }
    try {
      call(javax(), "_registerWeakMap", map);
    } catch (Throwable e) {
      printException(e);
      print("Upgrade JavaX!!");
    }
    return map;
  }

  public static void _onLoad_registerWeakMap() {
    assertNotNull(javax());
    if (_registerWeakMap_preList == null)
      return;
    for (Object o : _registerWeakMap_preList) _registerWeakMap(o);
    _registerWeakMap_preList = null;
  }

  public static boolean isInstanceOf(Object o, Class type) {
    return type.isInstance(o);
  }

  public static TimerTask timerTask(final Object r, final java.util.Timer timer) {
    return new TimerTask() {

      public void run() {
        if (!licensed())
          timer.cancel();
        else
          pcallF(r);
      }
    };
  }

  public static TimerTask timerTask(final Object r) {
    return new TimerTask() {

      public void run() {
        ping();
        pcallF(r);
      }
    };
  }

  public static int cmp(Number a, Number b) {
    return a == null ? b == null ? 0 : -1 : cmp(a.doubleValue(), b.doubleValue());
  }

  public static int cmp(double a, double b) {
    return a < b ? -1 : a == b ? 0 : 1;
  }

  public static int cmp(String a, String b) {
    return a == null ? b == null ? 0 : -1 : a.compareTo(b);
  }

  public static int cmp(Object a, Object b) {
    if (a == null)
      return b == null ? 0 : -1;
    if (b == null)
      return 1;
    return ((Comparable) a).compareTo(b);
  }

  public static String myJavaSource_code;

  public static String myJavaSource() {
    return myJavaSource_code;
  }

  public static boolean _inCore() {
    return false;
  }

  public static int jfind(String s, String in) {
    return jfind(javaTok(s), in);
  }

  public static int jfind(List<String> tok, String in) {
    return jfind(tok, 1, in);
  }

  public static int jfind(List<String> tok, int startIdx, String in) {
    return jfind(tok, startIdx, in, null);
  }

  public static int jfind(List<String> tok, String in, Object condition) {
    return jfind(tok, 1, in, condition);
  }

  public static int jfind(List<String> tok, int startIdx, String in, Object condition) {
    List<String> tokin = javaTok(in);
    jfind_preprocess(tokin);
    return jfind(tok, startIdx, tokin, condition);
  }

  public static int jfind(List<String> tok, List<String> tokin) {
    return jfind(tok, 1, tokin);
  }

  public static int jfind(List<String> tok, int startIdx, List<String> tokin) {
    return jfind(tok, startIdx, tokin, null);
  }

  public static int jfind(List<String> tok, int startIdx, List<String> tokin, Object condition) {
    return findCodeTokens(tok, startIdx, false, toStringArray(codeTokensOnly(tokin)), condition);
  }

  public static List<String> jfind_preprocess(List<String> tok) {
    for (String type : litlist("quoted", "id", "int")) replaceSublist(tok, ll("<", "", type, "", ">"), ll("<" + type + ">"));
    replaceSublist(tok, ll("\\", "", "*"), ll("\\*"));
    return tok;
  }

  public static String parse3_cached_s;

  public static List<String> parse3_cached_l;

  public static synchronized List<String> parse3_cached(String s) {
    if (neq(s, parse3_cached_s))
      parse3_cached_l = parse3(parse3_cached_s = s);
    return parse3_cached_l;
  }

  public static boolean endsWithLetter(String s) {
    return nempty(s) && isLetter(last(s));
  }

  public static WeakReference<Class> creator_class;

  public static Class creator() {
    return creator_class == null ? null : creator_class.get();
  }

  public static boolean ewic(String a, String b) {
    return endsWithIgnoreCase(a, b);
  }

  public static boolean ewic(String a, String b, Matches m) {
    return endsWithIgnoreCase(a, b, m);
  }

  public static String getDBProgramID_id;

  public static String getDBProgramID() {
    return nempty(getDBProgramID_id) ? getDBProgramID_id : programIDWithCase();
  }

  public static int max(int a, int b) {
    return Math.max(a, b);
  }

  public static int max(int a, int b, int c) {
    return max(max(a, b), c);
  }

  public static long max(int a, long b) {
    return Math.max((long) a, b);
  }

  public static long max(long a, long b) {
    return Math.max(a, b);
  }

  public static double max(int a, double b) {
    return Math.max((double) a, b);
  }

  public static float max(float a, float b) {
    return Math.max(a, b);
  }

  public static double max(double a, double b) {
    return Math.max(a, b);
  }

  public static int max(Collection<Integer> c) {
    int x = Integer.MIN_VALUE;
    for (int i : c) x = max(x, i);
    return x;
  }

  public static double max(double[] c) {
    if (c.length == 0)
      return Double.MIN_VALUE;
    double x = c[0];
    for (int i = 1; i < c.length; i++) x = Math.max(x, c[i]);
    return x;
  }

  public static float max(float[] c) {
    if (c.length == 0)
      return Float.MAX_VALUE;
    float x = c[0];
    for (int i = 1; i < c.length; i++) x = Math.max(x, c[i]);
    return x;
  }

  public static byte max(byte[] c) {
    byte x = -128;
    for (byte d : c) if (d > x)
      x = d;
    return x;
  }

  public static short max(short[] c) {
    short x = -0x8000;
    for (short d : c) if (d > x)
      x = d;
    return x;
  }

  public static int max(int[] c) {
    int x = Integer.MIN_VALUE;
    for (int d : c) if (d > x)
      x = d;
    return x;
  }

  public static volatile boolean disableCertificateValidation_attempted;

  public static void disableCertificateValidation() {
    try {
      if (disableCertificateValidation_attempted)
        return;
      disableCertificateValidation_attempted = true;
      try {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

          public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
          }

          public void checkClientTrusted(X509Certificate[] certs, String authType) {
          }

          public void checkServerTrusted(X509Certificate[] certs, String authType) {
          }
        } };
        HostnameVerifier hv = new HostnameVerifier() {

          public boolean verify(String hostname, SSLSession session) {
            return true;
          }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
      } catch (Throwable __e) {
        printStackTrace2(__e);
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static Field getOpt_findField(Class<?> c, String field) {
    Class _c = c;
    do {
      for (Field f : _c.getDeclaredFields()) if (f.getName().equals(field))
        return f;
      _c = _c.getSuperclass();
    } while (_c != null);
    return null;
  }

  public static void onClick(JComponent c, final Object runnable) {
    c.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        callF(runnable, e);
      }
    });
  }

  public static void onClick(JButton btn, final Object runnable) {
    onEnter(btn, runnable);
  }

  public static <A> A second(List<A> l) {
    return get(l, 1);
  }

  public static <A> A second(A[] bla) {
    return bla == null || bla.length <= 1 ? null : bla[1];
  }

  public static <A, B> B second(Pair<A, B> p) {
    return p == null ? null : p.b;
  }

  public static boolean publicCommOn() {
    return "1".equals(loadTextFile(new File(userHome(), ".javax/public-communication")));
  }

  public static <A extends JTextComponent> A jenableUndoRedo(A textcomp) {
    final UndoManager undo = new UndoManager();
    textcomp.getDocument().addUndoableEditListener(new UndoableEditListener() {

      public void undoableEditHappened(UndoableEditEvent evt) {
        undo.addEdit(evt.getEdit());
      }
    });
    textcomp.getActionMap().put("Undo", abstractAction("Undo", new Runnable() {

      public void run() {
        try {
          if (undo.canUndo())
            undo.undo();
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "if (undo.canUndo()) undo.undo()";
      }
    }));
    textcomp.getActionMap().put("Redo", abstractAction("Redo", new Runnable() {

      public void run() {
        try {
          if (undo.canRedo())
            undo.redo();
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "if (undo.canRedo()) undo.redo()";
      }
    }));
    textcomp.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
    textcomp.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
    return textcomp;
  }

  public static int parseHexChar(char c) {
    if (c >= '0' && c <= '9')
      return charDiff(c, '0');
    if (c >= 'a' && c <= 'f')
      return charDiff(c, 'a') + 10;
    if (c >= 'A' && c <= 'F')
      return charDiff(c, 'A') + 10;
    return -1;
  }

  public static String singleFieldName(Class c) {
    Set<String> l = listFields(c);
    if (l(l) != 1)
      throw fail("No single field found in " + c + " (have " + n(l(l), "fields") + ")");
    return first(l);
  }

  public static JWindow showWindow(Component c) {
    JWindow w = new JWindow();
    w.add(wrap(c));
    return w;
  }

  public static void onEnterInAllTextFields(JComponent c, Object action) {
    if (action == null)
      return;
    for (Component tf : allChildren(c)) onEnterIfTextField(tf, action);
  }

  public static void onEnterInAllTextFields(List c, Object action) {
    for (Object o : unnull(c)) if (o instanceof JComponent)
      onEnterIfTextField((JComponent) o, action);
  }

  public static Map synchroMap() {
    return synchroHashMap();
  }

  public static <A, B> Map<A, B> synchroMap(Map<A, B> map) {
    return Collections.synchronizedMap(map);
  }

  public static boolean tableSetColumnPreferredWidths_debug;

  public static void tableSetColumnPreferredWidths(final JTable table, final Map<String, Integer> widths) {
    {
      swing(new Runnable() {

        public void run() {
          try {
            try {
              TableColumnModel tcm = table.getColumnModel();
              int n = tcm.getColumnCount();
              for (int i = 0; i < n; i++) {
                TableColumn tc = tcm.getColumn(i);
                Integer w = widths.get(str(tc.getHeaderValue()));
                if (w != null) {
                  tc.setPreferredWidth(w);
                  if (tableSetColumnPreferredWidths_debug)
                    print("Setting preferred width of column " + i + " to " + w);
                }
              }
            } catch (Throwable __e) {
              printStackTrace2(__e);
            }
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "pcall {\r\n    TableColumnModel tcm = table.getColumnModel();\r\n    int n = tcm....";
        }
      });
    }
  }

  public static void tableSetColumnPreferredWidths(JTable table, Object... widths) {
    tableSetColumnPreferredWidths(table, litorderedmap(widths));
  }

  public static volatile Android3 dbBot_instance;

  public static Android3 dbBot() {
    return dbBot(dbBotStandardName());
  }

  public static Android3 dbBot(String name) {
    ensureDBNotRunning(name);
    assertNotNull(mainConcepts);
    return dbBot_instance = methodsBot2(name, mainConcepts, exposedDBMethods, mainConcepts.lock);
  }

  public static class getOpt_Map extends WeakHashMap {

    public getOpt_Map() {
      if (getOpt_special == null)
        getOpt_special = new HashMap();
      clear();
    }

    public void clear() {
      super.clear();
      put(Class.class, getOpt_special);
      put(String.class, getOpt_special);
    }
  }

  public static final Map<Class, HashMap<String, Field>> getOpt_cache = _registerDangerousWeakMap(synchroMap(new getOpt_Map()));

  public static HashMap getOpt_special;

  public static Object getOpt_cached(Object o, String field) {
    try {
      if (o == null)
        return null;
      Class c = o.getClass();
      HashMap<String, Field> map;
      synchronized (getOpt_cache) {
        map = getOpt_cache.get(c);
        if (map == null)
          map = getOpt_makeCache(c);
      }
      if (map == getOpt_special) {
        if (o instanceof Class)
          return getOpt((Class) o, field);
        if (o instanceof Map)
          return ((Map) o).get(field);
      }
      Field f = map.get(field);
      if (f != null)
        return f.get(o);
      if (o instanceof DynamicObject)
        return ((DynamicObject) o).fieldValues.get(field);
      return null;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static HashMap<String, Field> getOpt_makeCache(Class c) {
    HashMap<String, Field> map;
    if (isSubtypeOf(c, Map.class))
      map = getOpt_special;
    else {
      map = new HashMap();
      Class _c = c;
      do {
        for (Field f : _c.getDeclaredFields()) {
          f.setAccessible(true);
          String name = f.getName();
          if (!map.containsKey(name))
            map.put(name, f);
        }
        _c = _c.getSuperclass();
      } while (_c != null);
    }
    getOpt_cache.put(c, map);
    return map;
  }

  public static String dbBotStandardName() {
    return dbBotName(getDBProgramID()) + ".";
  }

  public static Object sleepQuietly_monitor = new Object();

  public static void sleepQuietly() {
    try {
      assertFalse(isAWTThread());
      synchronized (sleepQuietly_monitor) {
        sleepQuietly_monitor.wait();
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static Map<String, Object> rawTableLineAsMap(JTable tbl, int row) {
    if (row >= 0 && row < tbl.getModel().getRowCount()) {
      Map<String, Object> map = litorderedmap();
      for (int i = 0; i < tbl.getModel().getColumnCount(); i++) mapPut(map, tbl.getModel().getColumnName(i), tbl.getModel().getValueAt(row, i));
      return map;
    }
    return null;
  }

  public static String structureForUser(Object o) {
    return beautifyStructure(struct_noStringSharing(o));
  }

  public static int toInt(Object o) {
    if (o == null)
      return 0;
    if (o instanceof Number)
      return ((Number) o).intValue();
    if (o instanceof String)
      return parseInt((String) o);
    throw fail("woot not int: " + getClassName(o));
  }

  public static int toInt(long l) {
    if (l != (int) l)
      throw fail("Too large for int: " + l);
    return (int) l;
  }

  public static <A> A or(A a, A b) {
    return a != null ? a : b;
  }

  public static String rtrim(String s) {
    if (s == null)
      return null;
    int i = s.length();
    while (i > 0 && " \t\r\n".indexOf(s.charAt(i - 1)) >= 0) --i;
    return i < s.length() ? s.substring(0, i) : s;
  }

  public static String dropSuffix(String suffix, String s) {
    return s.endsWith(suffix) ? s.substring(0, l(s) - l(suffix)) : s;
  }

  public static String callStaticAnswerMethod(List bots, String s) {
    for (Object c : bots) try {
      String answer = callStaticAnswerMethod(c, s);
      if (!empty(answer))
        return answer;
    } catch (Throwable e) {
      print("Error calling " + getProgramID(c));
      e.printStackTrace();
    }
    return null;
  }

  public static String callStaticAnswerMethod(Object c, String s) {
    String answer = (String) callOpt(c, "answer", s, litlist(s));
    if (answer == null)
      answer = (String) callOpt(c, "answer", s);
    return emptyToNull(answer);
  }

  public static String callStaticAnswerMethod(String s) {
    return callStaticAnswerMethod(mc(), s);
  }

  public static String callStaticAnswerMethod(String s, List<String> history) {
    return callStaticAnswerMethod(mc(), s, history);
  }

  public static String callStaticAnswerMethod(Object c, String s, List<String> history) {
    String answer = (String) callOpt(c, "answer", s, history);
    if (answer == null)
      answer = (String) callOpt(c, "answer", s);
    return emptyToNull(answer);
  }

  public static void smartSet(Field f, Object o, Object value) throws Exception {
    try {
      f.set(o, value);
    } catch (Exception e) {
      Class type = f.getType();
      if (type == int.class && value instanceof Long)
        value = ((Long) value).intValue();
      if (type == LinkedHashMap.class && value instanceof Map) {
        f.set(o, asLinkedHashMap((Map) value));
        return;
      }
      try {
        if (f.getType() == Concept.Ref.class) {
          f.set(o, ((Concept) o).new Ref((Concept) value));
          return;
        }
        if (o instanceof Concept.Ref) {
          f.set(o, ((Concept.Ref) o).get());
          return;
        }
      } catch (Throwable _e) {
      }
      throw e;
    }
  }

  public static String tb_mainServer_default = "http://tinybrain.de:8080";

  public static Object tb_mainServer_override;

  public static String tb_mainServer() {
    if (tb_mainServer_override != null)
      return (String) callF(tb_mainServer_override);
    return trim(loadTextFile(tb_mainServer_file(), tb_mainServer_default));
  }

  public static File tb_mainServer_file() {
    return getProgramFile("#1001638", "mainserver.txt");
  }

  public static boolean tb_mainServer_isDefault() {
    return eq(tb_mainServer(), tb_mainServer_default);
  }

  public static List mapLL(Object f, Object... data) {
    return map(f, ll(data));
  }

  public static Map<String, Integer> tableColumnWidthsByName(JTable table) {
    TableColumnModel tcm = table.getColumnModel();
    int n = tcm.getColumnCount();
    TreeMap<String, Integer> map = new TreeMap();
    for (int i = 0; i < n; i++) {
      TableColumn tc = tcm.getColumn(i);
      map.put(str(tc.getHeaderValue()), tc.getWidth());
    }
    return map;
  }

  public static boolean containsNewLines(String s) {
    return containsNewLine(s);
  }

  public static int moveToTopRightCorner_inset = 20;

  public static <A extends Component> A moveToTopRightCorner(A a) {
    return moveToTopRightCorner(moveToTopRightCorner_inset, moveToTopRightCorner_inset, a);
  }

  public static <A extends Component> A moveToTopRightCorner(int insetX, int insetY, A a) {
    Window w = getWindow(a);
    if (w != null)
      w.setLocation(getScreenSize().width - w.getWidth() - insetX, insetY);
    return a;
  }

  public static JFrame handleEscapeKey(final JFrame frame) {
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    frame.getRootPane().registerKeyboardAction(new ActionListener() {

      public void actionPerformed(ActionEvent actionEvent) {
        frame.dispose();
      }
    }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    return frame;
  }

  public static Object callFunction(Object f, Object... args) {
    return callF(f, args);
  }

  public static JPanel infoMessage_makePanel(String text) {
    final JTextArea ta = wrappedTextArea(text);
    onClick(ta, new Runnable() {

      public void run() {
        try {
          disposeWindow(ta);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "disposeWindow(ta)";
      }
    });
    int size = 14;
    if (l(text) <= 50)
      size *= 2;
    else if (l(text) < 100)
      size = iround(size * 1.5);
    ta.setFont(typeWriterFont(size));
    JScrollPane sp = jscroll(ta);
    return withMargin(sp);
  }

  public static HashMap<String, List<Method>> callMC_cache = new HashMap();

  public static String callMC_key;

  public static Method callMC_value;

  public static Object callMC(String method, String[] arg) {
    return callMC(method, new Object[] { arg });
  }

  public static Object callMC(String method, Object... args) {
    try {
      Method me;
      if (callMC_cache == null)
        callMC_cache = new HashMap();
      synchronized (callMC_cache) {
        me = method == callMC_key ? callMC_value : null;
      }
      if (me != null)
        try {
          return invokeMethod(me, null, args);
        } catch (IllegalArgumentException e) {
          throw new RuntimeException("Can't call " + me + " with arguments " + classNames(args), e);
        }
      List<Method> m;
      synchronized (callMC_cache) {
        m = callMC_cache.get(method);
      }
      if (m == null) {
        if (callMC_cache.isEmpty()) {
          callMC_makeCache();
          m = callMC_cache.get(method);
        }
        if (m == null)
          throw fail("Method named " + method + " not found in main");
      }
      int n = m.size();
      if (n == 1) {
        me = m.get(0);
        synchronized (callMC_cache) {
          callMC_key = method;
          callMC_value = me;
        }
        try {
          return invokeMethod(me, null, args);
        } catch (IllegalArgumentException e) {
          throw new RuntimeException("Can't call " + me + " with arguments " + classNames(args), e);
        }
      }
      for (int i = 0; i < n; i++) {
        me = m.get(i);
        if (call_checkArgs(me, args, false))
          return invokeMethod(me, null, args);
      }
      throw fail("No method called " + method + " with matching arguments found in main");
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void callMC_makeCache() {
    synchronized (callMC_cache) {
      callMC_cache.clear();
      Class _c = (Class) mc(), c = _c;
      while (c != null) {
        for (Method m : c.getDeclaredMethods()) if ((m.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0) {
          m.setAccessible(true);
          multiMapPut(callMC_cache, m.getName(), m);
        }
        c = c.getSuperclass();
      }
    }
  }

  public static boolean isCurlyBracketed(String s) {
    return isCurlyBraced(s);
  }

  public static Method fastIntern_method;

  public static String fastIntern(String s) {
    try {
      if (s == null)
        return null;
      if (fastIntern_method == null) {
        fastIntern_method = findMethodNamed(javax(), "internPerProgram");
        if (fastIntern_method == null)
          upgradeJavaXAndRestart();
      }
      return (String) fastIntern_method.invoke(null, s);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static class _Activity {

    public long started;

    public Object r;

    public Thread thread;
  }

  public static Set<_Activity> tempActivity_list = synchroHashSet();

  public static boolean tempActivity_debug;

  public static <A> AutoCloseable tempActivity(final Object r) {
    if (tempActivity_debug)
      print("Activity started: " + r);
    final _Activity a = new _Activity();
    a.started = sysNow();
    a.r = r;
    a.thread = currentThread();
    tempActivity_list.add(a);
    return new AutoCloseable() {

      public void close() {
        tempActivity_list.remove(a);
        if (tempActivity_debug) {
          int n = l(tempActivity_list);
          print("Activity ended: " + r + (n == 0 ? "" : " - " + n + " remaining"));
        }
      }
    };
  }

  public static void disposeFrame(final Component c) {
    disposeWindow(c);
  }

  public static void setFrameContents(final Component c, final Object contents) {
    {
      swing(new Runnable() {

        public void run() {
          try {
            JFrame frame = getFrame(c);
            frame.getContentPane().removeAll();
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(wrap(contents));
            revalidate(frame);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "JFrame frame = getFrame(c);\r\n    frame.getContentPane().removeAll();\r\n    fra...";
        }
      });
    }
  }

  public static boolean isAWTThread() {
    if (isAndroid())
      return false;
    if (isHeadless())
      return false;
    return isAWTThread_awt();
  }

  public static boolean isAWTThread_awt() {
    return SwingUtilities.isEventDispatchThread();
  }

  public static Thread currentThread() {
    return Thread.currentThread();
  }

  public static void removeFromMultiPort(long vport) {
    if (vport == 0)
      return;
    for (Object port : getMultiPorts()) call(port, "removePort", vport);
  }

  public static void enableSubstance_impl(final String skinName) {
    if (headless())
      return;
    {
      swing(new Runnable() {

        public void run() {
          try {
            if (!substanceLookAndFeelEnabled())
              enableSubstance_impl_2(skinName);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "if (!substanceLookAndFeelEnabled())\r\n      enableSubstance_impl_2(skinName);";
        }
      });
    }
  }

  public static void enableSubstance_impl_2(String skinName) {
    try {
      boolean wasEnabled = substanceLookAndFeelEnabled();
      ClassLoader cl = main.class.getClassLoader();
      UIManager.getDefaults().put("ClassLoader", cl);
      Thread.currentThread().setContextClassLoader(cl);
      String skinClassName = "org.pushingpixels.substance.api.skin." + addSuffix(skinName, "Skin");
      SubstanceSkin skin = (SubstanceSkin) nuObject(cl.loadClass(skinClassName));
      SubstanceLookAndFeel.setSkin(skin);
      JFrame.setDefaultLookAndFeelDecorated(true);
      updateLookAndFeelOnAllWindows_noRenew();
      if (!wasEnabled)
        renewConsoleFrame();
      if (substanceLookAndFeelEnabled())
        print("Substance L&F enabled.");
      else
        print("Could not enable Substance L&F?");
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static JPanel westAndCenter(final Component w, final Component c) {
    return swing(new F0<JPanel>() {

      public JPanel get() {
        try {
          JPanel panel = new JPanel(new BorderLayout());
          panel.add(BorderLayout.WEST, wrap(w));
          panel.add(BorderLayout.CENTER, wrap(c));
          return panel;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JPanel panel = new JPanel(new BorderLayout);\r\n    panel.add(BorderLayout.WEST...";
      }
    });
  }

  public static Map<String, String> singular_specials = litmap("children", "child", "images", "image", "chess", "chess");

  public static Set<String> singular_specials2 = litset("time", "machine", "line");

  public static String singular(String s) {
    if (s == null)
      return null;
    {
      String _a_1036 = singular_specials.get(s);
      if (!empty(_a_1036))
        return _a_1036;
    }
    {
      String _a_1037 = hippoSingulars().get(lower(s));
      if (!empty(_a_1037))
        return _a_1037;
    }
    if (singular_specials2.contains(dropSuffix("s", lastWord(s))))
      return dropSuffix("s", s);
    if (s.endsWith("ness"))
      return s;
    if (s.endsWith("ges"))
      return dropSuffix("s", s);
    if (endsWith(s, "bases"))
      return dropLast(s);
    s = dropSuffix("es", s);
    s = dropSuffix("s", s);
    return s;
  }

  public static void _initFrame(JFrame f) {
    makeFrame_myFrames.put(f, Boolean.TRUE);
    standardTitlePopupMenu(f);
  }

  public static Object addToMultiPort_responder;

  public static long addToMultiPort(final String botName) {
    return addToMultiPort(botName, new Object() {

      public String answer(String s, List<String> history) {
        String answer = (String) (callOpt(getMainClass(), "answer", s, history));
        if (answer != null)
          return answer;
        answer = (String) callOpt(getMainClass(), "answer", s);
        if (answer != null)
          return answer;
        if (match3("get injection id", s))
          return getInjectionID();
        return null;
      }
    });
  }

  public static long addToMultiPort(final String botName, final Object responder) {
    addToMultiPort_responder = responder;
    startMultiPort();
    List ports = getMultiPorts();
    if (ports == null)
      return 0;
    if (ports.isEmpty())
      throw fail("No multiports!");
    if (ports.size() > 1)
      print("Multiple multi-ports. Using last one.");
    Object port = last(ports);
    Object responder2 = new Object() {

      public String answer(String s, List<String> history) {
        if (match3("get injection id", s))
          return getInjectionID();
        if (match3("your name", s))
          return botName;
        return (String) call(responder, "answer", s, history);
      }
    };
    record(responder2);
    return (Long) call(port, "addResponder", botName, responder2);
  }

  public static <A> A liftLast(List<A> l) {
    if (l.isEmpty())
      return null;
    int i = l(l) - 1;
    A a = l.get(i);
    l.remove(i);
    return a;
  }

  public static String programID;

  public static String getProgramID() {
    return nempty(programID) ? formatSnippetIDOpt(programID) : "?";
  }

  public static String getProgramID(Class c) {
    String id = (String) getOpt(c, "programID");
    if (nempty(id))
      return formatSnippetID(id);
    return "?";
  }

  public static String getProgramID(Object o) {
    return getProgramID(getMainClass(o));
  }

  public static String unnull(String s) {
    return s == null ? "" : s;
  }

  public static <A> List<A> unnull(List<A> l) {
    return l == null ? emptyList() : l;
  }

  public static <A, B> Map<A, B> unnull(Map<A, B> l) {
    return l == null ? emptyMap() : l;
  }

  public static <A> Iterable<A> unnull(Iterable<A> i) {
    return i == null ? emptyList() : i;
  }

  public static <A> A[] unnull(A[] a) {
    return a == null ? (A[]) new Object[0] : a;
  }

  public static BitSet unnull(BitSet b) {
    return b == null ? new BitSet() : b;
  }

  public static Pt unnull(Pt p) {
    return p == null ? new Pt() : p;
  }

  public static boolean swic(String a, String b) {
    return startsWithIgnoreCase(a, b);
  }

  public static boolean swic(String a, String b, Matches m) {
    if (!swic(a, b))
      return false;
    m.m = new String[] { substring(a, l(b)) };
    return true;
  }

  public static int min(int a, int b) {
    return Math.min(a, b);
  }

  public static long min(long a, long b) {
    return Math.min(a, b);
  }

  public static float min(float a, float b) {
    return Math.min(a, b);
  }

  public static float min(float a, float b, float c) {
    return min(min(a, b), c);
  }

  public static double min(double a, double b) {
    return Math.min(a, b);
  }

  public static double min(double[] c) {
    double x = Double.MAX_VALUE;
    for (double d : c) x = Math.min(x, d);
    return x;
  }

  public static float min(float[] c) {
    float x = Float.MAX_VALUE;
    for (float d : c) x = Math.min(x, d);
    return x;
  }

  public static byte min(byte[] c) {
    byte x = 127;
    for (byte d : c) if (d < x)
      x = d;
    return x;
  }

  public static short min(short[] c) {
    short x = 0x7FFF;
    for (short d : c) if (d < x)
      x = d;
    return x;
  }

  public static int min(int[] c) {
    int x = Integer.MAX_VALUE;
    for (int d : c) if (d < x)
      x = d;
    return x;
  }

  public static String hideCredentials(URL url) {
    return url == null ? null : hideCredentials(str(url));
  }

  public static String hideCredentials(String url) {
    return url.replaceAll("([&?])_pass=[^&\\s\"]*", "$1_pass=<hidden>");
  }

  public static String hideCredentials(Object o) {
    return hideCredentials(str(o));
  }

  public static Object[] flattenArray2(Object... a) {
    List l = new ArrayList();
    for (Object x : a) if (x instanceof Object[])
      l.addAll(asList((Object[]) x));
    else if (x instanceof Collection)
      l.addAll((Collection) x);
    else
      l.add(x);
    return asObjectArray(l);
  }

  public static String getSnippetTitleOpt(String s) {
    return isSnippetID(s) ? getSnippetTitle(s) : s;
  }

  public static String strOrEmpty(Object o) {
    return o == null ? "" : str(o);
  }

  public static Rectangle defaultNewFrameBounds_r = new Rectangle(300, 100, 500, 400);

  public static Rectangle defaultNewFrameBounds() {
    return swing(new F0<Rectangle>() {

      public Rectangle get() {
        try {
          defaultNewFrameBounds_r.translate(60, 20);
          if (!screenRectangle().contains(defaultNewFrameBounds_r))
            defaultNewFrameBounds_r.setLocation(30 + random(30), 20 + random(20));
          return new Rectangle(defaultNewFrameBounds_r);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "defaultNewFrameBounds_r.translate(60, 20);\r\n    if (!screenRectangle().contai...";
      }
    });
  }

  public static int withRightMargin_defaultWidth = 6;

  public static JPanel withRightMargin(Component c) {
    return withRightMargin(withRightMargin_defaultWidth, c);
  }

  public static JPanel withRightMargin(final int w, final Component c) {
    return swing(new F0<JPanel>() {

      public JPanel get() {
        try {
          JPanel p = new JPanel(new BorderLayout());
          p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, w));
          p.add(c);
          return p;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JPanel p = new JPanel(new BorderLayout);\r\n    p.setBorder(BorderFactory.creat...";
      }
    });
  }

  public static JFrame minFrameWidth(JFrame frame, int w) {
    if (frame != null && frame.getWidth() < w)
      frame.setSize(w, frame.getHeight());
    return frame;
  }

  public static JFrame minFrameWidth(int w, JFrame frame) {
    return minFrameWidth(frame, w);
  }

  public static boolean frameTooSmall(JFrame frame) {
    return frame.getWidth() < 100 || frame.getHeight() < 50;
  }

  public static String indentx(String s) {
    return indentx(indent_default, s);
  }

  public static String indentx(int n, String s) {
    return dropSuffix(repeat(' ', n), indent(n, s));
  }

  public static String indentx(String indent, String s) {
    return dropSuffix(indent, indent(indent, s));
  }

  public static int systemHashCode(Object o) {
    return identityHashCode(o);
  }

  public static String getInjectionID() {
    return (String) call(getJavaX(), "getInjectionID", getMainClass());
  }

  public static TableWithTooltips tableWithTooltips() {
    return (TableWithTooltips) swing(new F0<Object>() {

      public Object get() {
        try {
          return new TableWithTooltips();
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "new TableWithTooltips";
      }
    });
  }

  public static class TableWithTooltips extends JTable {

    public String getToolTipText(MouseEvent e) {
      String tip = null;
      Point p = e.getPoint();
      int rowIndex = rowAtPoint(p);
      int colIndex = columnAtPoint(p);
      try {
        return str(getValueAt(rowIndex, colIndex));
      } catch (Throwable _e) {
        return null;
      }
    }
  }

  public static long imageServerCheckMD5(String md5) {
    assertTrue(isMD5(md5));
    String s = loadPage("http://ai1.space/images/raw/checkmd5/" + md5);
    return parseLongOpt(s);
  }

  public static void revalidate(final Component c) {
    if (c == null || !c.isShowing())
      return;
    {
      swing(new Runnable() {

        public void run() {
          try {
            c.revalidate();
            c.repaint();
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "// magic combo to actually relayout and repaint\r\n    c.revalidate();\r\n    c.r...";
        }
      });
    }
  }

  public static URLConnection setURLConnectionDefaultTimeouts(URLConnection con, long timeout) {
    if (con.getConnectTimeout() == 0) {
      con.setConnectTimeout(toInt(timeout));
      if (con.getConnectTimeout() != timeout)
        print("Warning: URL connect timeout not set by JDK.");
    }
    if (con.getReadTimeout() == 0) {
      con.setReadTimeout(toInt(timeout));
      if (con.getReadTimeout() != timeout)
        print("Warning: URL read timeout not set by JDK.");
    }
    return con;
  }

  public static Timer installTimer(JComponent component, Object r, long delay) {
    return installTimer(component, r, delay, delay);
  }

  public static Timer installTimer(RootPaneContainer frame, long delay, Object r) {
    return installTimer(frame.getRootPane(), r, delay, delay);
  }

  public static Timer installTimer(JComponent component, long delay, Object r) {
    return installTimer(component, r, delay, delay);
  }

  public static Timer installTimer(JComponent component, long delay, long firstDelay, Object r) {
    return installTimer(component, r, delay, firstDelay);
  }

  public static Timer installTimer(final JComponent component, final Object r, final long delay, final long firstDelay) {
    return installTimer(component, r, delay, firstDelay, true);
  }

  public static Timer installTimer(final JComponent component, final Object r, final long delay, final long firstDelay, final boolean repeats) {
    return (Timer) swingAndWait(new F0<Object>() {

      public Object get() {
        try {
          final Var<Timer> timer = new Var();
          timer.set(new Timer(toInt(delay), new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent _evt) {
              try {
                AutoCloseable __720 = tempActivity(r);
                try {
                  try {
                    if (!allPaused())
                      if (isFalse(callF(r)))
                        cancelTimer(timer.get());
                  } catch (Throwable __e) {
                    printStackTrace2(__e);
                  }
                } finally {
                  _close(__720);
                }
              } catch (Throwable __e) {
                messageBox(__e);
              }
            }
          }));
          timer.get().setInitialDelay(toInt(firstDelay));
          timer.get().setRepeats(repeats);
          bindTimerToComponent(timer.get(), component);
          return timer.get();
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "final new Var<Timer> timer;\r\n    timer.set(new Timer(toInt(delay), actionList...";
      }
    });
  }

  public static Timer installTimer(RootPaneContainer frame, long delay, long firstDelay, Object r) {
    return installTimer(frame.getRootPane(), delay, firstDelay, r);
  }

  public static String showFormSubmitButtonName() {
    return "Submit";
  }

  public static void fillTableWithStrings(final JTable table, List<List<String>> rows, List<String> colNames) {
    fillTableWithStrings(table, rows, toStringArray(colNames));
  }

  public static void fillTableWithStrings(final JTable table, List<List<String>> rows, String... colNames) {
    final DefaultTableModel model = fillTableWithStrings_makeModel(rows, colNames);
    swingNowOrLater(new Runnable() {

      public void run() {
        try {
          setTableModel(table, model);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "setTableModel(table, model);";
      }
    });
  }

  public static DefaultTableModel fillTableWithStrings_makeModel(List<List<String>> rows, String... colNames) {
    Object[][] data = new Object[rows.size()][];
    int w = 0;
    for (int i = 0; i < rows.size(); i++) {
      List<String> l = rows.get(i);
      Object[] r = new Object[l.size()];
      for (int j = 0; j < l.size(); j++) r[j] = l.get(j);
      data[i] = r;
      w = Math.max(w, l.size());
    }
    Object[] columnNames = new Object[w];
    for (int i = 0; i < w; i++) columnNames[i] = i < l(colNames) ? colNames[i] : "?";
    return new DefaultTableModel(data, columnNames);
  }

  public static volatile boolean readLine_noReadLine;

  public static String readLine_lastInput;

  public static String readLine_prefix = "[] ";

  public static String readLine() {
    if (readLine_noReadLine)
      return null;
    String s = readLineHidden();
    if (s != null) {
      readLine_lastInput = s;
      print(readLine_prefix + s);
    }
    return s;
  }

  public static boolean headless() {
    return isHeadless();
  }

  public static String makeResponder_callAnswerMethod(Object bot, String s, List<String> history) {
    String answer = (String) callOpt(bot, "answer", s, history);
    if (answer == null)
      answer = (String) callOpt(bot, "answer", s);
    return answer;
  }

  public static Responder makeResponder(final Object bot) {
    if (bot instanceof Responder)
      return (Responder) bot;
    if (bot instanceof String) {
      String f = (String) (bot);
      return new Responder() {

        public String answer(String s, List<String> history) {
          String answer = (String) callOptMC((String) bot, s, history);
          if (answer == null)
            answer = (String) callOptMC((String) bot, s);
          return answer;
        }
      };
    }
    return new Responder() {

      public String answer(String s, List<String> history) {
        return makeResponder_callAnswerMethod(bot, s, history);
      }
    };
  }

  public static String[] dropFirst(int n, String[] a) {
    return drop(n, a);
  }

  public static String[] dropFirst(String[] a) {
    return drop(1, a);
  }

  public static Object[] dropFirst(Object[] a) {
    return drop(1, a);
  }

  public static <A> List<A> dropFirst(List<A> l) {
    return dropFirst(1, l);
  }

  public static <A> List<A> dropFirst(Iterable<A> i) {
    return dropFirst(toList(i));
  }

  public static <A> List<A> dropFirst(int n, List<A> l) {
    return n <= 0 ? l : new ArrayList(l.subList(Math.min(n, l.size()), l.size()));
  }

  public static <A> List<A> dropFirst(List<A> l, int n) {
    return dropFirst(n, l);
  }

  public static String dropFirst(int n, String s) {
    return substring(s, n);
  }

  public static String dropFirst(String s) {
    return substring(s, 1);
  }

  public static JPanel centerAndNorth(final Component c, final Component n) {
    return swing(new F0<JPanel>() {

      public JPanel get() {
        try {
          JPanel panel = new JPanel(new BorderLayout());
          panel.add(BorderLayout.CENTER, wrap(c));
          panel.add(BorderLayout.NORTH, wrap(n));
          return panel;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JPanel panel = new JPanel(new BorderLayout);\r\n    panel.add(BorderLayout.CENT...";
      }
    });
  }

  public static <A> A listGet(List<A> l, int idx) {
    return l != null && idx >= 0 && idx < l(l) ? l.get(idx) : null;
  }

  public static String jlabel_textAsHTML_center(String text) {
    return hhtml(hdiv(replace(htmlencode(text), "\n", "<br>"), "style", "text-align: center;"));
  }

  public static String javaTok_substringC(String s, int i, int j) {
    return s.substring(i, j);
  }

  public static String loadTextFilePossiblyGZipped(String fileName) {
    return loadTextFilePossiblyGZipped(fileName, null);
  }

  public static String loadTextFilePossiblyGZipped(String fileName, String defaultContents) {
    File gz = new File(fileName + ".gz");
    return gz.exists() ? loadGZTextFile(gz) : loadTextFile(fileName, defaultContents);
  }

  public static String loadTextFilePossiblyGZipped(File fileName) {
    return loadTextFilePossiblyGZipped(fileName, null);
  }

  public static String loadTextFilePossiblyGZipped(File fileName, String defaultContents) {
    return loadTextFilePossiblyGZipped(fileName.getPath(), defaultContents);
  }

  public static URLConnection setURLConnectionTimeouts(URLConnection con, long timeout) {
    con.setConnectTimeout(toInt(timeout));
    con.setReadTimeout(toInt(timeout));
    if (con.getConnectTimeout() != timeout || con.getReadTimeout() != timeout)
      print("Warning: Timeouts not set by JDK.");
    return con;
  }

  public static void setOpt_raw(Object o, String field, Object value) {
    try {
      if (o == null)
        return;
      if (o instanceof Class)
        setOpt_raw((Class) o, field, value);
      else {
        Field f = setOpt_raw_findField(o.getClass(), field);
        if (f != null) {
          f.setAccessible(true);
          smartSet(f, o, value);
        }
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void setOpt_raw(Class c, String field, Object value) {
    try {
      if (c == null)
        return;
      Field f = setOpt_raw_findStaticField(c, field);
      if (f != null) {
        f.setAccessible(true);
        smartSet(f, null, value);
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static Field setOpt_raw_findStaticField(Class<?> c, String field) {
    Class _c = c;
    do {
      for (Field f : _c.getDeclaredFields()) if (f.getName().equals(field) && (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0)
        return f;
      _c = _c.getSuperclass();
    } while (_c != null);
    return null;
  }

  public static Field setOpt_raw_findField(Class<?> c, String field) {
    Class _c = c;
    do {
      for (Field f : _c.getDeclaredFields()) if (f.getName().equals(field))
        return f;
      _c = _c.getSuperclass();
    } while (_c != null);
    return null;
  }

  public static byte[] boolArrayToBytes(boolean[] a) {
    byte[] b = new byte[(l(a) + 7) / 8];
    for (int i = 0; i < l(a); i++) if (a[i])
      b[i / 8] |= 1 << (i & 7);
    return b;
  }

  public static String shortenClassName(String name) {
    if (name == null)
      return null;
    int i = lastIndexOf(name, "$");
    if (i < 0)
      i = lastIndexOf(name, ".");
    return i < 0 ? name : substring(name, i + 1);
  }

  public static <A extends JComponent> A jMinWidth(int w, A c) {
    Dimension size = c.getMinimumSize();
    c.setMinimumSize(new Dimension(w, size.height));
    return jPreferWidth(w, c);
  }

  public static void _close(AutoCloseable c) {
    close(c);
  }

  public static String className(Object o) {
    return getClassName(o);
  }

  public static List<String> parse3(String s) {
    return dropPunctuation(javaTokPlusPeriod(s));
  }

  public static String getInnerMessage(Throwable e) {
    if (e == null)
      return null;
    return getInnerException(e).getMessage();
  }

  public static JComponent wrapForSmartAdd(Object o) {
    if (o == null)
      return new JPanel();
    if (o instanceof String)
      return jlabel((String) o);
    return wrap(o);
  }

  public static Component jrigid() {
    return javax.swing.Box.createRigidArea(new Dimension(0, 0));
  }

  public static List<Class> getClasses(Object[] array) {
    List<Class> l = new ArrayList();
    for (Object o : array) l.add(_getClass(o));
    return l;
  }

  public static int iround(double d) {
    return (int) Math.round(d);
  }

  public static boolean sexyTable_drag = true;

  public static JTable sexyTable() {
    final JTable table = sexyTableWithoutDrag();
    if (sexyTable_drag)
      tableEnableTextDrag(table);
    return table;
  }

  public static Object pcallFunction(Object f, Object... args) {
    try {
      return callFunction(f, args);
    } catch (Throwable __e) {
      printStackTrace2(__e);
    }
    return null;
  }

  public static void ensureDBNotRunning(String name) {
    if (hasBot(name)) {
      try {
        String id = fsI(dropAfterSpace(name));
        String framesBot = id + " Frames";
        print("Trying to activate frames of running DB: " + id);
        if (isOK(sendOpt(framesBot, "activate frames")) && isMainProgram())
          cleanKill();
      } catch (Throwable __e) {
        printStackTrace2(__e);
      }
      throw fail("Already running: " + name);
    }
  }

  public static String smartJoin(String[] args) {
    if (args.length == 1)
      return args[0];
    String[] a = new String[args.length];
    for (int i = 0; i < a.length; i++) a[i] = !isJavaIdentifier(args[i]) && !isQuoted(args[i]) ? quote(args[i]) : args[i];
    return join(" ", a);
  }

  public static String smartJoin(List<String> args) {
    return smartJoin(toStringArray(args));
  }

  public static final boolean loadPageThroughProxy_enabled = false;

  public static String loadPageThroughProxy(String url) {
    return null;
  }

  public static void hotwire_copyOver(Class c) {
    synchronized (StringBuffer.class) {
      for (String field : litlist("print_log", "print_silent", "androidContext")) {
        Object o = getOpt(mc(), field);
        if (o != null)
          setOpt(c, field, o);
      }
      Object mainBot = getMainBot();
      if (mainBot != null)
        setOpt(c, "mainBot", mainBot);
      setOpt(c, "creator_class", new WeakReference(mc()));
    }
  }

  public static <A, B> List<A> keysWithoutHidden(Map<A, B> map) {
    return filter(keys(map), new F1<Object, Object>() {

      public Object get(Object o) {
        try {
          return !isStringStartingWith(o, "[hidden] ");
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "!isStringStartingWith(o, \"[hidden] \")";
      }
    });
  }

  public static ThreadLocal<List<Object>> holdInstance_l = new ThreadLocal();

  public static AutoCloseable holdInstance(Object o) {
    if (o == null)
      return null;
    listThreadLocalAdd(holdInstance_l, o);
    return new AutoCloseable() {

      public void close() {
        listThreadLocalPopLast(holdInstance_l);
      }
    };
  }

  public static Throwable unwrapTrivialExceptionWraps(Throwable e) {
    if (e == null)
      return e;
    while (e.getClass() == RuntimeException.class && e.getCause() != null && eq(e.getMessage(), str(e.getCause()))) e = e.getCause();
    return e;
  }

  public static Map<Class, HashMap<String, Method>> callOpt_noArgs_cache = newDangerousWeakHashMap();

  public static Object callOpt_noArgs(Object o, String method) {
    try {
      if (o == null)
        return null;
      if (o instanceof Class)
        return callOpt(o, method);
      Class c = o.getClass();
      HashMap<String, Method> map;
      synchronized (callOpt_noArgs_cache) {
        map = callOpt_noArgs_cache.get(c);
        if (map == null)
          map = callOpt_noArgs_makeCache(c);
      }
      Method m = map.get(method);
      return m != null ? m.invoke(o) : null;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static HashMap<String, Method> callOpt_noArgs_makeCache(Class c) {
    HashMap<String, Method> map = new HashMap();
    Class _c = c;
    do {
      for (Method m : c.getDeclaredMethods()) if (m.getParameterTypes().length == 0) {
        m.setAccessible(true);
        String name = m.getName();
        if (!map.containsKey(name))
          map.put(name, m);
      }
      _c = _c.getSuperclass();
    } while (_c != null);
    callOpt_noArgs_cache.put(c, map);
    return map;
  }

  public static RuntimeException asRuntimeException(Throwable t) {
    if (t instanceof Error)
      _handleError((Error) t);
    return t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
  }

  public static String makePostData(Map<Object, Object> map) {
    List<String> l = new ArrayList();
    for (Map.Entry<Object, Object> e : map.entrySet()) {
      String key = (String) (e.getKey());
      Object val = e.getValue();
      if (val != null) {
        String value = str(val);
        l.add(urlencode(key) + "=" + urlencode(escapeMultichars(value)));
      }
    }
    return join("&", l);
  }

  public static String makePostData(Object... params) {
    return makePostData(litorderedmap(params));
  }

  public static void moveThisThreadToChild(Object child) {
    Thread t = currentThread();
    callOpt(child, "_registerThread", t);
    _unregisterThread(t);
  }

  public static boolean isInstanceX(Class type, Object arg) {
    if (type == boolean.class)
      return arg instanceof Boolean;
    if (type == int.class)
      return arg instanceof Integer;
    if (type == long.class)
      return arg instanceof Long;
    if (type == float.class)
      return arg instanceof Float;
    if (type == short.class)
      return arg instanceof Short;
    if (type == char.class)
      return arg instanceof Character;
    if (type == byte.class)
      return arg instanceof Byte;
    if (type == double.class)
      return arg instanceof Double;
    return type.isInstance(arg);
  }

  public static Object invokeMethod(Method m, Object o, Object... args) {
    try {
      try {
        return m.invoke(o, args);
      } catch (InvocationTargetException e) {
        throw rethrow(getExceptionCause(e));
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e.getMessage() + " - was calling: " + m + ", args: " + joinWithSpace(classNames(args)));
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static int tableRows(JTable table) {
    return table.getRowCount();
  }

  public static void sleepSeconds(double s) {
    if (s > 0)
      sleep(round(s * 1000));
  }

  public static <A, B> Collection<B> values(Map<A, B> map) {
    return map == null ? emptyList() : map.values();
  }

  public static void assertFalse(Object o) {
    if (!(eq(o, false)))
      throw fail(str(o));
  }

  public static boolean assertFalse(boolean b) {
    if (b)
      throw fail("oops");
    return b;
  }

  public static boolean assertFalse(String msg, boolean b) {
    if (b)
      throw fail(msg);
    return b;
  }

  public static JSplitPane jhsplit(Component l, Component r) {
    return setSplitPaneLater(swingConstruct(JSplitPane.class, JSplitPane.HORIZONTAL_SPLIT, wrap(l), wrap(r)), 0.5);
  }

  public static JTable dataToTable_uneditable(Object data, final JTable table) {
    return dataToTable_uneditable(table, data);
  }

  public static JTable dataToTable_uneditable(final JTable table, final Object data) {
    {
      swing(new Runnable() {

        public void run() {
          try {
            dataToTable(table, data, true);
            makeTableUneditable(table);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "dataToTable(table, data, true);\r\n    makeTableUneditable(table);";
        }
      });
    }
    return table;
  }

  public static JTable dataToTable_uneditable(final Object data) {
    return dataToTable_uneditable(showTable(), data);
  }

  public static JTable dataToTable_uneditable(Object data, String title) {
    return dataToTable_uneditable(showTable(title), data);
  }

  public static long parseSnippetID(String snippetID) {
    long id = Long.parseLong(shortenSnippetID(snippetID));
    if (id == 0)
      throw fail("0 is not a snippet ID");
    return id;
  }

  public static long sysNow() {
    return System.nanoTime() / 1000000;
  }

  public static Throwable innerException(Throwable e) {
    return getInnerException(e);
  }

  public static int imageIcon_cacheSize = 10;

  public static boolean imageIcon_verbose;

  public static Map<String, ImageIcon> imageIcon_cache;

  public static Lock imageIcon_lock = lock();

  public static ImageIcon imageIcon(String imageID) {
    try {
      Lock __835 = imageIcon_lock;
      lock(__835);
      try {
        if (imageIcon_cache == null)
          imageIcon_cache = new MRUCache(imageIcon_cacheSize);
        imageID = fsI(imageID);
        ImageIcon ii = imageIcon_cache.get(imageID);
        if (ii == null) {
          if (imageIcon_verbose)
            print("Loading image icon: " + imageID);
          ii = new ImageIcon(loadBinarySnippet(imageID).toURI().toURL());
        } else
          imageIcon_cache.remove(imageID);
        imageIcon_cache.put(imageID, ii);
        return ii;
      } finally {
        unlock(__835);
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static ImageIcon imageIcon(BufferedImage img) {
    return new ImageIcon(img);
  }

  public static ImageIcon imageIcon(RGBImage img) {
    return imageIcon(img.getBufferedImage());
  }

  public static Object[] toObjectArray(Collection c) {
    List l = asList(c);
    return l.toArray(new Object[l.size()]);
  }

  public static String processID_cached;

  public static String getPID() {
    if (processID_cached == null) {
      String name = ManagementFactory.getRuntimeMXBean().getName();
      processID_cached = name.replaceAll("@.*", "");
    }
    return processID_cached;
  }

  public static JPanel smartAddWithLayout(JPanel panel, Object layout, List parts) {
    for (Object o : parts) panel.add(wrapForSmartAdd(o), layout);
    return panel;
  }

  public static JPanel smartAddWithLayout(JPanel panel, Object layout, Object... parts) {
    return smartAddWithLayout(panel, layout, asList(parts));
  }

  public static int lUtf8(String s) {
    return l(utf8(s));
  }

  public static Map<String, Class> classForName_cache = synchroHashMap();

  public static Class classForName(String name) {
    try {
      Class c = classForName_cache.get(name);
      if (c == null)
        classForName_cache.put(name, c = Class.forName(name));
      return c;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static <A extends JComponent> A makeBold(final A c) {
    if (c != null) {
      swing(new Runnable() {

        public void run() {
          try {
            c.setFont(c.getFont().deriveFont(Font.BOLD));
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "c.setFont(c.getFont().deriveFont(Font.BOLD));";
        }
      });
    }
    return c;
  }

  public static <A, B> Map<A, B> weakHashMap() {
    return newWeakHashMap();
  }

  public static int isAndroid_flag;

  public static boolean isAndroid() {
    if (isAndroid_flag == 0)
      isAndroid_flag = System.getProperty("java.vendor").toLowerCase().indexOf("android") >= 0 ? 1 : -1;
    return isAndroid_flag > 0;
  }

  public static Throwable getExceptionCause(Throwable e) {
    Throwable c = e.getCause();
    return c != null ? c : e;
  }

  public static String sendOpt(String bot, String text, Object... args) {
    return sendToLocalBotOpt(bot, text, args);
  }

  public static String[] dropLast(String[] a, int n) {
    n = Math.min(n, a.length);
    String[] b = new String[a.length - n];
    System.arraycopy(a, 0, b, 0, b.length);
    return b;
  }

  public static <A> List<A> dropLast(List<A> l) {
    return subList(l, 0, l(l) - 1);
  }

  public static <A> List<A> dropLast(int n, List<A> l) {
    return subList(l, 0, l(l) - n);
  }

  public static <A> List<A> dropLast(Iterable<A> l) {
    return dropLast(asList(l));
  }

  public static String dropLast(String s) {
    return substring(s, 0, l(s) - 1);
  }

  public static String dropLast(String s, int n) {
    return substring(s, 0, l(s) - n);
  }

  public static String dropLast(int n, String s) {
    return dropLast(s, n);
  }

  public static String shortenSnippetID(String snippetID) {
    if (snippetID.startsWith("#"))
      snippetID = snippetID.substring(1);
    String httpBlaBla = "http://tinybrain.de/";
    if (snippetID.startsWith(httpBlaBla))
      snippetID = snippetID.substring(httpBlaBla.length());
    return "" + parseLong(snippetID);
  }

  public static <A> HashSet<A> litset(A... items) {
    return lithashset(items);
  }

  public static <A, B> LinkedHashMap<A, B> asLinkedHashMap(Map<A, B> map) {
    if (map instanceof LinkedHashMap)
      return (LinkedHashMap) map;
    LinkedHashMap<A, B> m = new LinkedHashMap();
    if (map != null)
      synchronized (collectionMutex(map)) {
        m.putAll(map);
      }
    return m;
  }

  public static <A> Set<A> synchroTreeSet() {
    return Collections.synchronizedSet(new TreeSet<A>());
  }

  public static String dropSuffixIgnoreCase(String suffix, String s) {
    return ewic(s, suffix) ? s.substring(0, l(s) - l(suffix)) : s;
  }

  public static String programIDWithCase() {
    return nempty(caseID()) ? programID() + "/" + quoteUnlessIdentifierOrInteger(caseID()) : programID();
  }

  public static void close(AutoCloseable c) {
    try {
      if (c != null)
        c.close();
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String escapeMultichars(String s) {
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < l(s); i++) if (isExtendedUnicodeCharacter(s, i)) {
      buf.append("[xchar " + intToHex(s.codePointAt(i)) + "]");
      ++i;
    } else
      buf.append(s.charAt(i));
    return str(buf);
  }

  public static boolean isCurlyBraced(String s) {
    List<String> tok = tok_combineCurlyBrackets_keep(javaTok(s));
    return l(tok) == 3 && startsWithAndEndsWith(tok.get(1), "{", "}");
  }

  public static boolean startsWithIgnoreCase(String a, String b) {
    return regionMatchesIC(a, 0, b, 0, b.length());
  }

  public static <A, B> Map<A, B> newDangerousWeakHashMap() {
    return _registerDangerousWeakMap(synchroMap(new WeakHashMap()));
  }

  public static <A, B> Map<A, B> newDangerousWeakHashMap(Object initFunction) {
    return _registerDangerousWeakMap(synchroMap(new WeakHashMap()), initFunction);
  }

  public static Class javax() {
    return getJavaX();
  }

  public static boolean isQuoted(String s) {
    if (isNormalQuoted(s))
      return true;
    return isMultilineQuoted(s);
  }

  public static Thread _unregisterThread(Thread t) {
    _registerThread_threads.remove(t);
    return t;
  }

  public static void _unregisterThread() {
    _unregisterThread(currentThread());
  }

  public static boolean hasMethod(Object o, String method, Object... args) {
    return findMethod(o, method, args) != null;
  }

  public static boolean forbiddenPort(int port) {
    return port == 5037;
  }

  public static String dbBotName(String progID) {
    return fsI(progID) + " Concepts";
  }

  public static String prependIfNempty(String prefix, String s) {
    return empty(s) ? s : prefix + s;
  }

  public static boolean isOK(String s) {
    s = trim(s);
    return swic(s, "ok ") || eqic(s, "ok") || matchStart("ok", s);
  }

  public static List<String> javaTokWithExisting(String s, List<String> existing) {
    ++javaTok_n;
    int nExisting = javaTok_opt && existing != null ? existing.size() : 0;
    ArrayList<String> tok = existing != null ? new ArrayList(nExisting) : new ArrayList();
    int l = s.length();
    int i = 0, n = 0;
    while (i < l) {
      int j = i;
      char c, d;
      while (j < l) {
        c = s.charAt(j);
        d = j + 1 >= l ? '\0' : s.charAt(j + 1);
        if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
          ++j;
        else if (c == '/' && d == '*') {
          do ++j; while (j < l && !s.substring(j, Math.min(j + 2, l)).equals("*/"));
          j = Math.min(j + 2, l);
        } else if (c == '/' && d == '/') {
          do ++j; while (j < l && "\r\n".indexOf(s.charAt(j)) < 0);
        } else
          break;
      }
      if (n < nExisting && javaTokWithExisting_isCopyable(existing.get(n), s, i, j))
        tok.add(existing.get(n));
      else
        tok.add(javaTok_substringN(s, i, j));
      ++n;
      i = j;
      if (i >= l)
        break;
      c = s.charAt(i);
      d = i + 1 >= l ? '\0' : s.charAt(i + 1);
      if (c == '\'' && Character.isJavaIdentifierStart(d) && i + 2 < l && "'\\".indexOf(s.charAt(i + 2)) < 0) {
        j += 2;
        while (j < l && Character.isJavaIdentifierPart(s.charAt(j))) ++j;
      } else if (c == '\'' || c == '"') {
        char opener = c;
        ++j;
        while (j < l) {
          if (s.charAt(j) == opener) {
            ++j;
            break;
          } else if (s.charAt(j) == '\\' && j + 1 < l)
            j += 2;
          else
            ++j;
        }
      } else if (Character.isJavaIdentifierStart(c))
        do ++j; while (j < l && (Character.isJavaIdentifierPart(s.charAt(j)) || "'".indexOf(s.charAt(j)) >= 0));
      else if (Character.isDigit(c)) {
        do ++j; while (j < l && Character.isDigit(s.charAt(j)));
        if (j < l && s.charAt(j) == 'L')
          ++j;
      } else if (c == '[' && d == '[') {
        do ++j; while (j + 1 < l && !s.substring(j, j + 2).equals("]]"));
        j = Math.min(j + 2, l);
      } else if (c == '[' && d == '=' && i + 2 < l && s.charAt(i + 2) == '[') {
        do ++j; while (j + 2 < l && !s.substring(j, j + 3).equals("]=]"));
        j = Math.min(j + 3, l);
      } else
        ++j;
      if (n < nExisting && javaTokWithExisting_isCopyable(existing.get(n), s, i, j))
        tok.add(existing.get(n));
      else
        tok.add(javaTok_substringC(s, i, j));
      ++n;
      i = j;
    }
    if ((tok.size() % 2) == 0)
      tok.add("");
    javaTok_elements += tok.size();
    return tok;
  }

  public static boolean javaTokWithExisting_isCopyable(String t, String s, int i, int j) {
    return t.length() == j - i && s.regionMatches(i, t, 0, j - i);
  }

  public static List<String> javaTokPlusPeriod(String s) {
    List<String> tok = new ArrayList<String>();
    int l = s.length();
    int i = 0;
    while (i < l) {
      int j = i;
      char c;
      String cc;
      while (j < l) {
        c = s.charAt(j);
        cc = s.substring(j, Math.min(j + 2, l));
        if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
          ++j;
        else if (cc.equals("/*")) {
          do ++j; while (j < l && !s.substring(j, Math.min(j + 2, l)).equals("*/"));
          j = Math.min(j + 2, l);
        } else if (cc.equals("//")) {
          do ++j; while (j < l && "\r\n".indexOf(s.charAt(j)) < 0);
        } else
          break;
      }
      tok.add(s.substring(i, j));
      i = j;
      if (i >= l)
        break;
      c = s.charAt(i);
      cc = s.substring(i, Math.min(i + 2, l));
      if (c == '“' || c == '”')
        c = '"';
      if (c == '\'' || c == '"') {
        char opener = c;
        ++j;
        while (j < l) {
          char _c = s.charAt(j);
          if (_c == '“' || _c == '”')
            _c = '"';
          if (_c == opener) {
            ++j;
            break;
          } else if (s.charAt(j) == '\\' && j + 1 < l)
            j += 2;
          else
            ++j;
        }
        if (j - 1 >= i + 1) {
          tok.add(opener + s.substring(i + 1, j - 1) + opener);
          i = j;
          continue;
        }
      } else if (Character.isJavaIdentifierStart(c))
        do ++j; while (j < l && (Character.isJavaIdentifierPart(s.charAt(j)) || s.charAt(j) == '\''));
      else if (Character.isDigit(c))
        do ++j; while (j < l && Character.isDigit(s.charAt(j)));
      else if (cc.equals("[[")) {
        do ++j; while (j + 1 < l && !s.substring(j, j + 2).equals("]]"));
        j = Math.min(j + 2, l);
      } else if (cc.equals("[=") && i + 2 < l && s.charAt(i + 2) == '[') {
        do ++j; while (j + 2 < l && !s.substring(j, j + 3).equals("]=]"));
        j = Math.min(j + 3, l);
      } else if (s.substring(j, Math.min(j + 3, l)).equals("..."))
        j += 3;
      else if (c == '$' || c == '#')
        do ++j; while (j < l && Character.isDigit(s.charAt(j)));
      else
        ++j;
      tok.add(s.substring(i, j));
      i = j;
    }
    if ((tok.size() % 2) == 0)
      tok.add("");
    return tok;
  }

  public static List emptyList() {
    return new ArrayList();
  }

  public static List emptyList(int capacity) {
    return new ArrayList(capacity);
  }

  public static List emptyList(Iterable l) {
    return l instanceof Collection ? emptyList(((Collection) l).size()) : emptyList();
  }

  public static Object[] asObjectArray(List l) {
    return toObjectArray(l);
  }

  public static Map synchroHashMap() {
    return Collections.synchronizedMap(new HashMap());
  }

  public static String urlencode(String x) {
    try {
      return URLEncoder.encode(unnull(x), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<String> classNames(Collection l) {
    return getClassNames(l);
  }

  public static List<String> classNames(Object[] l) {
    return getClassNames(Arrays.asList(l));
  }

  public static File javaxCodeDir_dir;

  public static File javaxCodeDir() {
    return javaxCodeDir_dir != null ? javaxCodeDir_dir : new File(userHome(), "JavaX-Code");
  }

  public static int identityHashCode(Object o) {
    return System.identityHashCode(o);
  }

  public static Method findMethodNamed(Object obj, String method) {
    if (obj == null)
      return null;
    if (obj instanceof Class)
      return findMethodNamed((Class) obj, method);
    return findMethodNamed(obj.getClass(), method);
  }

  public static Method findMethodNamed(Class c, String method) {
    while (c != null) {
      for (Method m : c.getDeclaredMethods()) if (m.getName().equals(method)) {
        m.setAccessible(true);
        return m;
      }
      c = c.getSuperclass();
    }
    return null;
  }

  public static void lock(Lock lock) {
    try {
      ping();
      lock.lockInterruptibly();
      ping();
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void lock(Lock lock, String msg) {
    print("Locking: " + msg);
    lock(lock);
  }

  public static void lock(Lock lock, String msg, long timeout) {
    print("Locking: " + msg);
    lockOrFail(lock, timeout);
  }

  public static ReentrantLock lock() {
    return fairLock();
  }

  public static String tok_unCurlyBracket(String s) {
    return isCurlyBraced(s) ? join(dropFirstThreeAndLastThree(javaTok(s))) : s;
  }

  public static String lower(String s) {
    return s == null ? null : s.toLowerCase();
  }

  public static char lower(char c) {
    return Character.toLowerCase(c);
  }

  public static void bindTimerToComponent(final Timer timer, JFrame f) {
    bindTimerToComponent(timer, f.getRootPane());
  }

  public static void bindTimerToComponent(final Timer timer, JComponent c) {
    if (c.isShowing())
      timer.start();
    c.addAncestorListener(new AncestorListener() {

      public void ancestorAdded(AncestorEvent event) {
        timer.start();
      }

      public void ancestorRemoved(AncestorEvent event) {
        timer.stop();
      }

      public void ancestorMoved(AncestorEvent event) {
      }
    });
  }

  public static boolean endsWithIgnoreCase(String a, String b) {
    int la = l(a), lb = l(b);
    return la >= lb && regionMatchesIC(a, la - lb, b, 0, lb);
  }

  public static boolean endsWithIgnoreCase(String a, String b, Matches m) {
    if (!endsWithIgnoreCase(a, b))
      return false;
    m.m = new String[] { substring(a, 0, l(a) - l(b)) };
    return true;
  }

  public static void cleanKill() {
    cleanKillVM();
  }

  public static void cancelTimer(javax.swing.Timer timer) {
    if (timer != null)
      timer.stop();
  }

  public static void cancelTimer(java.util.Timer timer) {
    if (timer != null)
      timer.cancel();
  }

  public static List map(Iterable l, Object f) {
    return map(f, l);
  }

  public static List map(Object f, Iterable l) {
    List x = emptyList(l);
    if (l != null)
      for (Object o : l) x.add(callF(f, o));
    return x;
  }

  public static List map(F1 f, Iterable l) {
    List x = emptyList(l);
    if (l != null)
      for (Object o : l) x.add(callF(f, o));
    return x;
  }

  public static List map(Object f, Object[] l) {
    return map(f, asList(l));
  }

  public static List map(Object[] l, Object f) {
    return map(f, l);
  }

  public static List map(Object f, Map map) {
    return map(map, f);
  }

  public static List map(Map map, Object f) {
    List x = new ArrayList();
    if (map != null)
      for (Object _e : map.entrySet()) {
        Map.Entry e = (Map.Entry) _e;
        x.add(callF(f, e.getKey(), e.getValue()));
      }
    return x;
  }

  public static String getSnippetTitle(String id) {
    try {
      if (id == null)
        return null;
      if (!isSnippetID(id))
        return "?";
      long parsedID = parseSnippetID(id);
      String url;
      if (isImageServerSnippet(parsedID))
        url = "http://ai1.space/images/raw/title/" + parsedID;
      else
        url = tb_mainServer() + "/tb-int/getfield.php?id=" + parsedID + "&field=title" + standardCredentials();
      return trim(loadPageSilently(url));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String getSnippetTitle(long id) {
    return getSnippetTitle(fsI(id));
  }

  public static <A> ArrayList<A> toList(A[] a) {
    return asList(a);
  }

  public static ArrayList<Integer> toList(int[] a) {
    return asList(a);
  }

  public static <A> ArrayList<A> toList(Set<A> s) {
    return asList(s);
  }

  public static <A> ArrayList<A> toList(Iterable<A> s) {
    return asList(s);
  }

  public static <A> A listThreadLocalPopLast(ThreadLocal<List<A>> tl) {
    List<A> l = tl.get();
    if (l == null)
      return null;
    A a = popLast(l);
    if (empty(l))
      tl.set(null);
    return a;
  }

  public static void tableEnableTextDrag(final JTable table) {
    TransferHandler th = new TransferHandler() {

      @Override
      public int getSourceActions(JComponent c) {
        return COPY;
      }

      @Override
      public Transferable createTransferable(JComponent c) {
        Object o = selectedTableCell(table);
        return new StringSelection(str(o));
      }
    };
    tableEnableDrag(table, th);
  }

  public static byte[] intToBytes(int i) {
    return new byte[] { (byte) (i >>> 24), (byte) (i >>> 16), (byte) (i >>> 8), (byte) i };
  }

  public static <A> void listThreadLocalAdd(ThreadLocal<List<A>> tl, A a) {
    List<A> l = tl.get();
    if (l == null)
      tl.set(l = new ArrayList());
    l.add(a);
  }

  public static List<Object> getMultiPorts() {
    return (List) callOpt(getJavaX(), "getMultiPorts");
  }

  public static String emptyToNull(String s) {
    return eq(s, "") ? null : s;
  }

  public static JTextArea jTextArea() {
    return jTextArea("");
  }

  public static JTextArea jTextArea(final String text) {
    return swing(new F0<JTextArea>() {

      public JTextArea get() {
        try {
          return new JTextArea(text);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "new JTextArea(text)";
      }
    });
  }

  public static <A> void scanForComponents(Component c, Class<A> theClass, List<A> l) {
    if (theClass.isInstance(c))
      l.add((A) c);
    if (c instanceof Container)
      for (Component comp : ((Container) c).getComponents()) scanForComponents(comp, theClass, l);
  }

  public static File javaxDataDir_dir;

  public static File javaxDataDir() {
    return javaxDataDir_dir != null ? javaxDataDir_dir : new File(userHome(), "JavaX-Data");
  }

  public static File javaxDataDir(String sub) {
    return newFile(javaxDataDir(), sub);
  }

  public static int indent_default = 2;

  public static String indent(int indent) {
    return repeat(' ', indent);
  }

  public static String indent(int indent, String s) {
    return indent(repeat(' ', indent), s);
  }

  public static String indent(String indent, String s) {
    return indent + s.replace("\n", "\n" + indent);
  }

  public static String indent(String s) {
    return indent(indent_default, s);
  }

  public static List<String> indent(String indent, List<String> lines) {
    List<String> l = new ArrayList();
    for (String s : lines) l.add(indent + s);
    return l;
  }

  public static String repeat(char c, int n) {
    n = Math.max(n, 0);
    char[] chars = new char[n];
    for (int i = 0; i < n; i++) chars[i] = c;
    return new String(chars);
  }

  public static <A> List<A> repeat(A a, int n) {
    n = Math.max(n, 0);
    List<A> l = new ArrayList(n);
    for (int i = 0; i < n; i++) l.add(a);
    return l;
  }

  public static <A> List<A> repeat(int n, A a) {
    return repeat(a, n);
  }

  public static List<String> dropPunctuation_keep = litlist("*", "<", ">");

  public static List<String> dropPunctuation(List<String> tok) {
    tok = new ArrayList<String>(tok);
    for (int i = 1; i < tok.size(); i += 2) {
      String t = tok.get(i);
      if (t.length() == 1 && !Character.isLetter(t.charAt(0)) && !Character.isDigit(t.charAt(0)) && !dropPunctuation_keep.contains(t)) {
        tok.set(i - 1, tok.get(i - 1) + tok.get(i + 1));
        tok.remove(i);
        tok.remove(i);
        i -= 2;
      }
    }
    return tok;
  }

  public static String dropPunctuation(String s) {
    return join(dropPunctuation(nlTok(s)));
  }

  public static int toMS_int(double seconds) {
    return toInt_checked((long) (seconds * 1000));
  }

  public static int charDiff(char a, char b) {
    return (int) a - (int) b;
  }

  public static int charDiff(String a, char b) {
    return charDiff(stringToChar(a), b);
  }

  public static void startMultiPort() {
    List mp = getMultiPorts();
    if (mp != null && mp.isEmpty()) {
      nohupJavax("#1001639");
      throw fail("Upgrading JavaX, please restart this program afterwards.");
    }
  }

  public static <A> Set<A> synchroHashSet() {
    return Collections.synchronizedSet(new HashSet<A>());
  }

  public static String hhtml(Object contents) {
    return containerTag("html", contents);
  }

  public static void logQuotedWithTime(String s) {
    logQuotedWithTime(standardLogFile(), s);
  }

  public static void logQuotedWithTime(File logFile, String s) {
    logQuoted(logFile, logQuotedWithTime_format(s));
  }

  public static void logQuotedWithTime(String logFile, String s) {
    logQuoted(logFile, logQuotedWithTime_format(s));
  }

  public static String logQuotedWithTime_format(String s) {
    return formatGMTWithDate_24(now()) + " " + s;
  }

  public static String dropAfterSpace(String s) {
    return s == null ? null : substring(s, 0, smartIndexOf(s, ' '));
  }

  public static Set<String> listFields(Object c) {
    TreeSet<String> fields = new TreeSet();
    for (Field f : _getClass(c).getDeclaredFields()) fields.add(f.getName());
    return fields;
  }

  public static <A extends JComponent> A jPreferWidth(int w, A c) {
    Dimension size = c.getPreferredSize();
    c.setPreferredSize(new Dimension(w, size.height));
    return c;
  }

  public static String uploadToImageServer_rawBytes(byte[] imgData, String name) {
    String page = postPage("http://ai1.space/images/raw/upload", "data", bytesToHex(imgData), "name", name);
    print(page);
    Matcher m = Pattern.compile("/raw/([0-9]+)").matcher(page);
    if (!m.find())
      return null;
    return "http://ai1.lol/images/raw/" + m.group(1);
  }

  public static <A> A assertNotNull(A a) {
    assertTrue(a != null);
    return a;
  }

  public static <A> A assertNotNull(String msg, A a) {
    assertTrue(msg, a != null);
    return a;
  }

  public static Dimension getMinimumSize(final Component c) {
    return c == null ? null : swing(new F0<Dimension>() {

      public Dimension get() {
        try {
          return c.getMinimumSize();
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "c.getMinimumSize()";
      }
    });
  }

  public static boolean equalsIgnoreCase(String a, String b) {
    return eqic(a, b);
  }

  public static boolean equalsIgnoreCase(char a, char b) {
    return eqic(a, b);
  }

  public static boolean allPaused() {
    return ping_pauseAll;
  }

  public static File loadBinarySnippet(String snippetID) {
    try {
      long id = parseSnippetID(snippetID);
      File f = DiskSnippetCache_getLibrary(id);
      if (fileSize(f) == 0)
        f = loadDataSnippetToFile(snippetID);
      return f;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static Object mainBot;

  public static Object getMainBot() {
    return mainBot;
  }

  public static Class getMainClass() {
    return main.class;
  }

  public static Class getMainClass(Object o) {
    try {
      return (o instanceof Class ? (Class) o : o.getClass()).getClassLoader().loadClass("main");
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static int gzInputStream_defaultBufferSize = 65536;

  public static GZIPInputStream gzInputStream(File f) {
    try {
      return gzInputStream(new FileInputStream(f));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static GZIPInputStream gzInputStream(File f, int bufferSize) {
    try {
      return new GZIPInputStream(new FileInputStream(f), bufferSize);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static GZIPInputStream gzInputStream(InputStream in) {
    try {
      return new GZIPInputStream(in, gzInputStream_defaultBufferSize);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void renewConsoleFrame() {
    setConsoleFrame(renewFrame(consoleFrame()));
  }

  public static JScrollPane jscroll(final Component c) {
    return swing(new F0<JScrollPane>() {

      public JScrollPane get() {
        try {
          return new JScrollPane(c);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "new JScrollPane(c)";
      }
    });
  }

  public static String readLineHidden() {
    try {
      if (get(javax(), "readLine_reader") == null)
        set(javax(), "readLine_reader", new BufferedReader(new InputStreamReader(System.in, "UTF-8")));
      try {
        return ((BufferedReader) get(javax(), "readLine_reader")).readLine();
      } finally {
        consoleClearInput();
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void makeTableUneditable(JTable table) {
    for (int c = 0; c < table.getColumnCount(); c++) {
      Class<?> col_class = table.getColumnClass(c);
      table.setDefaultEditor(col_class, null);
    }
  }

  public static void unlock(Lock lock, String msg) {
    print("Unlocking: " + msg);
    lock.unlock();
  }

  public static void unlock(Lock lock) {
    lock.unlock();
  }

  public static <A> A last(List<A> l) {
    return empty(l) ? null : l.get(l.size() - 1);
  }

  public static char last(String s) {
    return empty(s) ? '#' : s.charAt(l(s) - 1);
  }

  public static int last(int[] a) {
    return l(a) != 0 ? a[l(a) - 1] : 0;
  }

  public static <A> A last(A[] a) {
    return l(a) != 0 ? a[l(a) - 1] : null;
  }

  public static <A> A last(Iterator<A> it) {
    A a = null;
    while (it.hasNext()) {
      ping();
      a = it.next();
    }
    return a;
  }

  public static String classNameToVM(String name) {
    return name.replace(".", "$");
  }

  public static void printException(Throwable e) {
    printStackTrace(e);
  }

  public static void updateLookAndFeelOnAllWindows_noRenew() {
    for (Window window : Window.getWindows()) SwingUtilities.updateComponentTreeUI(window);
  }

  public static String beautifyStructure(String s) {
    return structure_addTokenMarkers(s);
  }

  public static String[] drop(int n, String[] a) {
    n = Math.min(n, a.length);
    String[] b = new String[a.length - n];
    System.arraycopy(a, n, b, 0, b.length);
    return b;
  }

  public static Object[] drop(int n, Object[] a) {
    n = Math.min(n, a.length);
    Object[] b = new Object[a.length - n];
    System.arraycopy(a, n, b, 0, b.length);
    return b;
  }

  public static void standardTitlePopupMenu(final JFrame frame) {
    if (isSubstanceLAF())
      titlePopupMenu(frame, new VF1<JPopupMenu>() {

        public void get(JPopupMenu menu) {
          try {
            boolean alwaysOnTop = frame.isAlwaysOnTop();
            menu.add(jmenuItem("Restart Program", "restart"));
            menu.add(jmenuItem("Duplicate Program", "duplicateThisProgram"));
            menu.add(jmenuItem("Show Console", "showConsole"));
            menu.add(jCheckBoxMenuItem("Always On Top", alwaysOnTop, new Runnable() {

              public void run() {
                try {
                  toggleAlwaysOnTop(frame);
                } catch (Exception __e) {
                  throw rethrow(__e);
                }
              }

              public String toString() {
                return "toggleAlwaysOnTop(frame)";
              }
            }));
            menu.add(jMenuItem("Shoot Window", new Runnable() {

              public void run() {
                try {
                  shootWindowGUI_external(frame, 500);
                } catch (Exception __e) {
                  throw rethrow(__e);
                }
              }

              public String toString() {
                return "shootWindowGUI_external(frame, 500)";
              }
            }));
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "bool alwaysOnTop = frame.isAlwaysOnTop();\r\n        menu.add(jmenuItem(\"Restar...";
        }
      });
  }

  public static <A> List<A> synchroList() {
    return Collections.synchronizedList(new ArrayList<A>());
  }

  public static <A> List<A> synchroList(List<A> l) {
    return Collections.synchronizedList(l);
  }

  public static File getProgramFile(String progID, String fileName) {
    if (new File(fileName).isAbsolute())
      return new File(fileName);
    return new File(getProgramDir(progID), fileName);
  }

  public static File getProgramFile(String fileName) {
    return getProgramFile(getProgramID(), fileName);
  }

  public static boolean eqic(String a, String b) {
    if ((a == null) != (b == null))
      return false;
    if (a == null)
      return true;
    return a.equalsIgnoreCase(b);
  }

  public static boolean eqic(char a, char b) {
    if (a == b)
      return true;
    char u1 = Character.toUpperCase(a);
    char u2 = Character.toUpperCase(b);
    if (u1 == u2)
      return true;
    return Character.toLowerCase(u1) == Character.toLowerCase(u2);
  }

  public static Map htmlencode(Map o) {
    HashMap bla = new HashMap();
    for (Object key : keys(o)) {
      Object value = o.get(key);
      bla.put(htmlencode(key), htmlencode(value));
    }
    return bla;
  }

  public static String htmlencode(Object o) {
    return htmlencode(string(o));
  }

  public static String htmlencode(String s) {
    if (s == null)
      return "";
    StringBuilder out = new StringBuilder(Math.max(16, s.length()));
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
        out.append("&#");
        out.append((int) c);
        out.append(';');
      } else
        out.append(c);
    }
    return out.toString();
  }

  public static int lastIndexOf(String a, String b) {
    return a == null || b == null ? -1 : a.lastIndexOf(b);
  }

  public static int lastIndexOf(String a, char b) {
    return a == null ? -1 : a.lastIndexOf(b);
  }

  public static volatile Throwable lastException_lastException;

  public static Throwable lastException() {
    return lastException_lastException;
  }

  public static void lastException(Throwable e) {
    lastException_lastException = e;
  }

  public static String struct_noStringSharing(Object o) {
    structure_Data d = new structure_Data();
    d.noStringSharing = true;
    return structure(o, d);
  }

  public static String joinWithSpace(Collection<String> c) {
    return join(" ", c);
  }

  public static String joinWithSpace(String... c) {
    return join(" ", c);
  }

  public static Object callOptMC(String method, Object... args) {
    return callOpt(mc(), method, args);
  }

  public static String javaTok_substringN(String s, int i, int j) {
    if (i == j)
      return "";
    if (j == i + 1 && s.charAt(i) == ' ')
      return " ";
    return s.substring(i, j);
  }

  public static String getComputerID_quick() {
    return computerID();
  }

  public static <A, B> void put(Map<A, B> map, A a, B b) {
    if (map != null)
      map.put(a, b);
  }

  public static boolean isStringStartingWith(Object o, String prefix) {
    return o instanceof String && ((String) o).startsWith(prefix);
  }

  public static String hdiv(Object contents, Object... params) {
    return div(contents, params);
  }

  public static boolean substanceLookAndFeelEnabled() {
    return startsWith(getLookAndFeel(), "org.pushingpixels.");
  }

  public static <A> A optPar(ThreadLocal<A> tl, A defaultValue) {
    A a = tl.get();
    if (a != null) {
      tl.set(null);
      return a;
    }
    return defaultValue;
  }

  public static <A> A optPar(ThreadLocal<A> tl) {
    return optPar(tl, null);
  }

  public static <A> List<A> replaceSublist(List<A> l, List<A> x, List<A> y) {
    if (x == null)
      return l;
    int i = 0;
    while (true) {
      i = indexOfSubList(l, x, i);
      if (i < 0)
        break;
      for (int j = 0; j < l(x); j++) l.remove(i);
      l.addAll(i, y);
      i += l(y);
    }
    return l;
  }

  public static <A> List<A> replaceSublist(List<A> l, int fromIndex, int toIndex, List<A> y) {
    while (toIndex > fromIndex) l.remove(--toIndex);
    l.addAll(fromIndex, y);
    return l;
  }

  public static int packFrame_minw = 150, packFrame_minh = 50;

  public static <A extends Component> A packFrame(final A c) {
    {
      swing(new Runnable() {

        public void run() {
          try {
            Window w = getWindow(c);
            if (w != null) {
              w.pack();
              int maxW = getScreenWidth() - 50, maxH = getScreenHeight() - 50;
              w.setSize(min(maxW, max(w.getWidth(), packFrame_minw)), min(maxH, max(w.getHeight(), packFrame_minh)));
            }
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "Window w = getWindow(c);\r\n    if (w != null) {\r\n      w.pack();\r\n      int ma...";
        }
      });
    }
    return c;
  }

  public static JFrame packFrame(ButtonGroup g) {
    return packFrame(getFrame(g));
  }

  public static List<String> codeTokensOnly(List<String> tok) {
    List<String> l = new ArrayList();
    for (int i = 1; i < tok.size(); i += 2) l.add(tok.get(i));
    return l;
  }

  public static void upgradeJavaXAndRestart() {
    run("#1001639");
    restart();
    sleep();
  }

  public static List<Pair> _registerDangerousWeakMap_preList;

  public static <A> A _registerDangerousWeakMap(A map) {
    return _registerDangerousWeakMap(map, null);
  }

  public static <A> A _registerDangerousWeakMap(A map, Object init) {
    callF(init, map);
    if (init instanceof String) {
      final String f = (String) (init);
      init = new VF1<Map>() {

        public void get(Map map) {
          try {
            callMC(f, map);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "callMC(f, map)";
        }
      };
    }
    if (javax() == null) {
      if (_registerDangerousWeakMap_preList == null)
        _registerDangerousWeakMap_preList = synchroList();
      _registerDangerousWeakMap_preList.add(pair(map, init));
      return map;
    }
    try {
      call(javax(), "_registerDangerousWeakMap", map, init);
    } catch (Throwable e) {
      printException(e);
      upgradeJavaXAndRestart();
    }
    return map;
  }

  public static void _onLoad_registerDangerousWeakMap() {
    assertNotNull(javax());
    if (_registerDangerousWeakMap_preList == null)
      return;
    for (Pair p : _registerDangerousWeakMap_preList) _registerDangerousWeakMap(p.a, p.b);
    _registerDangerousWeakMap_preList = null;
  }

  public static byte[] toPNG(BufferedImage img) {
    return convertToPNG(img);
  }

  public static String addSuffix(String s, String suffix) {
    return s.endsWith(suffix) ? s : s + suffix;
  }

  public static byte[] utf8(String s) {
    return toUtf8(s);
  }

  public static String _userHome;

  public static String userHome() {
    if (_userHome == null) {
      if (isAndroid())
        _userHome = "/storage/sdcard0/";
      else
        _userHome = System.getProperty("user.home");
    }
    return _userHome;
  }

  public static File userHome(String path) {
    return new File(userDir(), path);
  }

  public static Map emptyMap() {
    return new HashMap();
  }

  public static String baseClassName(String className) {
    return substring(className, className.lastIndexOf('.') + 1);
  }

  public static String baseClassName(Object o) {
    return baseClassName(getClassName(o));
  }

  public static Map<String, String> hippoSingulars() {
    return pairsToMap((List<Pair<String, String>>) scanStructureLog("#1011041", "singulars"));
  }

  public static List filter(Iterable c, Object pred) {
    List x = new ArrayList();
    if (c != null)
      for (Object o : c) if (isTrue(callF(pred, o)))
        x.add(o);
    return x;
  }

  public static List filter(Object pred, Iterable c) {
    return filter(c, pred);
  }

  public static <A, B extends A> List filter(Iterable<B> c, F1<A, Boolean> pred) {
    List x = new ArrayList();
    if (c != null)
      for (B o : c) if (pred.get(o).booleanValue())
        x.add(o);
    return x;
  }

  public static <A, B extends A> List filter(F1<A, Boolean> pred, Iterable<B> c) {
    return filter(c, pred);
  }

  public static int indexOfIgnoreCase(List<String> a, String b) {
    return indexOfIgnoreCase(a, b, 0);
  }

  public static int indexOfIgnoreCase(List<String> a, String b, int i) {
    int n = l(a);
    for (; i < n; i++) if (eqic(a.get(i), b))
      return i;
    return -1;
  }

  public static int indexOfIgnoreCase(String a, String b) {
    return indexOfIgnoreCase_manual(a, b);
  }

  public static Class<?> getClass(String name) {
    try {
      return Class.forName(name);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  public static Class getClass(Object o) {
    return o instanceof Class ? (Class) o : o.getClass();
  }

  public static Class getClass(Object realm, String name) {
    try {
      try {
        return getClass(realm).getClassLoader().loadClass(classNameToVM(name));
      } catch (ClassNotFoundException e) {
        return null;
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static List<Component> allChildren(Component c) {
    return childrenOfType(c, Component.class);
  }

  public static <A> List<A> replace(List<A> l, A a, A b) {
    for (int i = 0; i < l(l); i++) if (eq(l.get(i), a))
      l.set(i, b);
    return l;
  }

  public static String replace(String s, String a, String b) {
    return s == null ? null : a == null || b == null ? s : s.replace(a, b);
  }

  public static String emptySymbol_value;

  public static String emptySymbol() {
    if (emptySymbol_value == null)
      emptySymbol_value = symbol("");
    return emptySymbol_value;
  }

  public static <A, B> void mapPut(Map<A, B> map, A key, B value) {
    if (map != null && key != null && value != null)
      map.put(key, value);
  }

  public static <A, B> void multiMapPut(Map<A, List<B>> map, A a, B b) {
    List<B> l = map.get(a);
    if (l == null)
      map.put(a, l = new ArrayList());
    l.add(b);
  }

  public static int findCodeTokens(List<String> tok, String... tokens) {
    return findCodeTokens(tok, 1, false, tokens);
  }

  public static int findCodeTokens(List<String> tok, boolean ignoreCase, String... tokens) {
    return findCodeTokens(tok, 1, ignoreCase, tokens);
  }

  public static int findCodeTokens(List<String> tok, int startIdx, boolean ignoreCase, String... tokens) {
    return findCodeTokens(tok, startIdx, ignoreCase, tokens, null);
  }

  public static List<String> findCodeTokens_specials = litlist("*", "<quoted>", "<id>", "<int>", "\\*");

  public static boolean findCodeTokens_debug;

  public static int findCodeTokens_indexed, findCodeTokens_unindexed;

  public static int findCodeTokens_bails, findCodeTokens_nonbails;

  public static int findCodeTokens(List<String> tok, int startIdx, boolean ignoreCase, String[] tokens, Object condition) {
    if (findCodeTokens_debug) {
      if (eq(getClassName(tok), "main$IndexedList2"))
        findCodeTokens_indexed++;
      else
        findCodeTokens_unindexed++;
    }
    if (!findCodeTokens_specials.contains(tokens[0]) && !tok.contains(tokens[0])) {
      ++findCodeTokens_bails;
      return -1;
    }
    ++findCodeTokens_nonbails;
    outer: for (int i = startIdx | 1; i + tokens.length * 2 - 2 < tok.size(); i += 2) {
      for (int j = 0; j < tokens.length; j++) {
        String p = tokens[j], t = tok.get(i + j * 2);
        boolean match;
        if (eq(p, "*"))
          match = true;
        else if (eq(p, "<quoted>"))
          match = isQuoted(t);
        else if (eq(p, "<id>"))
          match = isIdentifier(t);
        else if (eq(p, "<int>"))
          match = isInteger(t);
        else if (eq(p, "\\*"))
          match = eq("*", t);
        else
          match = ignoreCase ? eqic(p, t) : eq(p, t);
        if (!match)
          continue outer;
      }
      if (condition == null || checkTokCondition(condition, tok, i - 1))
        return i;
    }
    return -1;
  }

  public static JTextField onEnter(final JTextField tf, final Object action) {
    if (action == null || tf == null)
      return tf;
    tf.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent _evt) {
        try {
          tf.selectAll();
          callF(action);
        } catch (Throwable __e) {
          messageBox(__e);
        }
      }
    });
    return tf;
  }

  public static JButton onEnter(JButton btn, final Object action) {
    if (action == null || btn == null)
      return btn;
    btn.addActionListener(actionListener(action));
    return btn;
  }

  public static JList onEnter(JList list, Object action) {
    list.addKeyListener(enterKeyListener(action));
    return list;
  }

  public static JComboBox onEnter(final JComboBox cb, final Object action) {
    JTextField text = (JTextField) cb.getEditor().getEditorComponent();
    onEnter(text, action);
    return cb;
  }

  public static Rectangle screenRectangle() {
    return new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
  }

  public static String loadGZTextFile(File file) {
    try {
      if (!file.isFile())
        return null;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      InputStream fis = new FileInputStream(file);
      GZIPInputStream gis = newGZIPInputStream(fis);
      try {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = gis.read(buffer)) != -1) {
          baos.write(buffer, 0, len);
        }
      } finally {
        fis.close();
      }
      baos.close();
      return fromUtf8(baos.toByteArray());
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static Object collectionMutex(Object o) {
    String c = className(o);
    if (eq(c, "java.util.TreeMap$KeySet"))
      c = className(o = getOpt(o, "m"));
    else if (eq(c, "java.util.HashMap$KeySet"))
      c = className(o = get_raw(o, "this$0"));
    if (eqOneOf(c, "java.util.TreeMap$AscendingSubMap", "java.util.TreeMap$DescendingSubMap"))
      c = className(o = get_raw(o, "m"));
    if (o instanceof WeakHashSet)
      c = className(o = ((WeakHashSet) o).mutex());
    return o;
  }

  public static boolean containsNewLine(String s) {
    return contains(s, '\n');
  }

  public static String lastWord(String s) {
    return lastJavaToken(s);
  }

  public static AbstractAction abstractAction(String name, final Object runnable) {
    return new AbstractAction(name) {

      public void actionPerformed(ActionEvent evt) {
        pcallF(runnable);
      }
    };
  }

  public static boolean hasBot(String searchPattern) {
    DialogIO io = findBot(searchPattern);
    if (io != null) {
      io.close();
      return true;
    } else
      return false;
  }

  public static boolean isLetter(char c) {
    return Character.isLetter(c);
  }

  public static long parseLongOpt(String s) {
    return isInteger(s) ? parseLong(s) : 0;
  }

  public static Dimension getScreenSize() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }

  public static String formatSnippetIDOpt(String s) {
    return isSnippetID(s) ? formatSnippetID(s) : s;
  }

  public static String hmsWithColons() {
    return hmsWithColons(now());
  }

  public static String hmsWithColons(long time) {
    return new SimpleDateFormat("HH:mm:ss").format(time);
  }

  public static Android3 methodsBot2(String name, final Object receiver, final List<String> exposedMethods) {
    return methodsBot2(name, receiver, exposedMethods, null);
  }

  public static Android3 methodsBot2(String name, final Object receiver, final List<String> exposedMethods, final Lock lock) {
    Android3 android = new Android3();
    android.greeting = name;
    android.console = false;
    android.responder = new Responder() {

      public String answer(String s, List<String> history) {
        return exposeMethods2(receiver, s, exposedMethods, lock);
      }
    };
    return makeBot(android);
  }

  public static Map<Component, String> componentID_map = weakHashMap();

  public static String componentID(Component c) {
    return c == null ? null : componentID_map.get(c);
  }

  public static String loadTextFile(String fileName) {
    return loadTextFile(fileName, null);
  }

  public static String loadTextFile(File fileName, String defaultContents) {
    ping();
    try {
      if (fileName == null || !fileName.exists())
        return defaultContents;
      FileInputStream fileInputStream = new FileInputStream(fileName);
      InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
      return loadTextFile(inputStreamReader);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String loadTextFile(File fileName) {
    return loadTextFile(fileName, null);
  }

  public static String loadTextFile(String fileName, String defaultContents) {
    return fileName == null ? defaultContents : loadTextFile(newFile(fileName), defaultContents);
  }

  public static String loadTextFile(Reader reader) throws IOException {
    StringBuilder builder = new StringBuilder();
    try {
      char[] buffer = new char[1024];
      int n;
      while (-1 != (n = reader.read(buffer))) builder.append(buffer, 0, n);
    } finally {
      reader.close();
    }
    return builder.toString();
  }

  public static void onEnterIfTextField(Component c, Object action) {
    if (action == null)
      return;
    if (c instanceof JTextField)
      onEnter((JTextField) c, action);
    else if (c instanceof JComboBox)
      onEnter((JComboBox) c, action);
  }

  public static void clear(Collection c) {
    if (c != null)
      c.clear();
  }

  public static long round(double d) {
    return Math.round(d);
  }

  public static char lastChar(String s) {
    return empty(s) ? '\0' : s.charAt(l(s) - 1);
  }

  public static List<String> tok_combineCurlyBrackets_keep(List<String> tok) {
    List<String> l = new ArrayList();
    for (int i = 0; i < l(tok); i++) {
      String t = tok.get(i);
      if (odd(i) && eq(t, "{")) {
        int j = findEndOfCurlyBracketPart(tok, i);
        l.add(joinSubList(tok, i, j));
        i = j - 1;
      } else
        l.add(t);
    }
    return l;
  }

  public static int toInt_checked(long l) {
    if (l != (int) l)
      throw fail("Too large for int: " + l);
    return (int) l;
  }

  public static boolean contains(Collection c, Object o) {
    return c != null && c.contains(o);
  }

  public static boolean contains(Object[] x, Object o) {
    if (x != null)
      for (Object a : x) if (eq(a, o))
        return true;
    return false;
  }

  public static boolean contains(String s, char c) {
    return s != null && s.indexOf(c) >= 0;
  }

  public static boolean contains(String s, String b) {
    return s != null && s.indexOf(b) >= 0;
  }

  public static boolean contains(BitSet bs, int i) {
    return bs != null && bs.get(i);
  }

  public static boolean isImageServerSnippet(long id) {
    return id >= 1100000 && id < 1200000;
  }

  public static void titlePopupMenu(final Component c, final Object menuMaker) {
    swingNowOrLater(new Runnable() {

      public void run() {
        try {
          if (!isSubstanceLAF())
            print("Can't add title right click!");
          else {
            JComponent titleBar = getTitlePaneComponent(getFrame(c));
            componentPopupMenu(titleBar, menuMaker);
          }
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "if (!isSubstanceLAF())\r\n      print(\"Can't add title right click!\");\r\n    els...";
      }
    });
  }

  public static void duplicateThisProgram() {
    nohupJavax(trim(programID() + " " + smartJoin((String[]) get(getJavaX(), "fullArgs"))));
  }

  public static void set(Object o, String field, Object value) {
    if (o instanceof Class)
      set((Class) o, field, value);
    else
      try {
        Field f = set_findField(o.getClass(), field);
        f.setAccessible(true);
        smartSet(f, o, value);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
  }

  public static void set(Class c, String field, Object value) {
    try {
      Field f = set_findStaticField(c, field);
      f.setAccessible(true);
      smartSet(f, null, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Field set_findStaticField(Class<?> c, String field) {
    Class _c = c;
    do {
      for (Field f : _c.getDeclaredFields()) if (f.getName().equals(field) && (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0)
        return f;
      _c = _c.getSuperclass();
    } while (_c != null);
    throw new RuntimeException("Static field '" + field + "' not found in " + c.getName());
  }

  public static Field set_findField(Class<?> c, String field) {
    Class _c = c;
    do {
      for (Field f : _c.getDeclaredFields()) if (f.getName().equals(field))
        return f;
      _c = _c.getSuperclass();
    } while (_c != null);
    throw new RuntimeException("Field '" + field + "' not found in " + c.getName());
  }

  public static Map<String, Integer> findBot_cache = synchroHashMap();

  public static int findBot_timeout = 5000;

  public static DialogIO findBot(String searchPattern) {
    String subBot = null;
    int i = searchPattern.indexOf('/');
    if (i >= 0 && (isJavaIdentifier(searchPattern.substring(0, i)) || isInteger(searchPattern.substring(0, i)))) {
      subBot = searchPattern.substring(i + 1);
      searchPattern = searchPattern.substring(0, i);
      if (!isInteger(searchPattern))
        searchPattern = "Multi-Port at " + searchPattern + ".";
    }
    if (isInteger(searchPattern))
      return talkToSubBot(subBot, talkTo(parseInt(searchPattern)));
    if (eq(searchPattern, "remote"))
      return talkToSubBot(subBot, talkTo("second.tinybrain.de", 4999));
    Integer port = findBot_cache.get(searchPattern);
    if (port != null)
      try {
        DialogIO io = talkTo("localhost", port);
        io.waitForLine();
        String line = io.readLineNoBlock();
        if (indexOfIgnoreCase(line, searchPattern) == 0) {
          call(io, "pushback", line);
          return talkToSubBot(subBot, io);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    List<ProgramScan.Program> bots = quickBotScan();
    for (ProgramScan.Program p : bots) {
      if (indexOfIgnoreCase(p.helloString, searchPattern) == 0) {
        findBot_cache.put(searchPattern, p.port);
        return talkToSubBot(subBot, talkTo("localhost", p.port));
      }
    }
    for (ProgramScan.Program p : bots) {
      String botName = firstPartOfHelloString(p.helloString);
      boolean isVM = startsWithIgnoreCase(p.helloString, "This is a JavaX VM.");
      boolean shouldRecurse = startsWithIgnoreCase(botName, "Multi-Port") || isVM;
      if (shouldRecurse)
        try {
          Map<Number, String> subBots = (Map) unstructure(sendToLocalBotQuietly(p.port, "list bots"));
          for (Number vport : subBots.keySet()) {
            String name = subBots.get(vport);
            if (startsWithIgnoreCase(name, searchPattern))
              return talkToSubBot(vport.longValue(), talkTo("localhost", p.port));
          }
        } catch (Throwable __e) {
          print(exceptionToStringShort(__e));
        }
    }
    return null;
  }

  public static String div(Object contents, Object... params) {
    return hfulltag("div", contents, params);
  }

  public static BigInteger div(BigInteger a, BigInteger b) {
    return a.divide(b);
  }

  public static BigInteger div(BigInteger a, int b) {
    return a.divide(bigint(b));
  }

  public static boolean checkTokCondition(Object condition, List<String> tok, int i) {
    if (condition instanceof TokCondition)
      return ((TokCondition) condition).get(tok, i);
    return checkCondition(condition, tok, i);
  }

  public static void tableEnableDrag(final JTable table, TransferHandler th) {
    if (table.getDragEnabled()) {
      print("Table drag already enabled");
      return;
    }
    table.setDragEnabled(true);
    table.setTransferHandler(th);
    table.addMouseListener(new MouseAdapter() {

      @Override
      public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1 && e.getClickCount() == 1)
          table.getTransferHandler().exportAsDrag(table, e, TransferHandler.COPY);
      }
    });
  }

  public static KeyListener enterKeyListener(final Object action) {
    return new KeyAdapter() {

      public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER)
          pcallF(action);
      }
    };
  }

  public static <A> HashSet<A> lithashset(A... items) {
    HashSet<A> set = new HashSet();
    for (A a : items) set.add(a);
    return set;
  }

  public static byte[] toUtf8(String s) {
    try {
      return s.getBytes("UTF-8");
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void logQuoted(String logFile, String line) {
    logQuoted(getProgramFile(logFile), line);
  }

  public static void logQuoted(File logFile, String line) {
    appendToFile(logFile, quote(line) + "\n");
  }

  public static Method findMethod(Object o, String method, Object... args) {
    try {
      if (o == null)
        return null;
      if (o instanceof Class) {
        Method m = findMethod_static((Class) o, method, args, false);
        if (m == null)
          return null;
        m.setAccessible(true);
        return m;
      } else {
        Method m = findMethod_instance(o, method, args, false);
        if (m == null)
          return null;
        m.setAccessible(true);
        return m;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Method findMethod_static(Class c, String method, Object[] args, boolean debug) {
    Class _c = c;
    while (c != null) {
      for (Method m : c.getDeclaredMethods()) {
        if (debug)
          System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");
        ;
        if (!m.getName().equals(method)) {
          if (debug)
            System.out.println("Method name mismatch: " + method);
          continue;
        }
        if ((m.getModifiers() & Modifier.STATIC) == 0 || !findMethod_checkArgs(m, args, debug))
          continue;
        return m;
      }
      c = c.getSuperclass();
    }
    return null;
  }

  public static Method findMethod_instance(Object o, String method, Object[] args, boolean debug) {
    Class c = o.getClass();
    while (c != null) {
      for (Method m : c.getDeclaredMethods()) {
        if (debug)
          System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");
        ;
        if (m.getName().equals(method) && findMethod_checkArgs(m, args, debug))
          return m;
      }
      c = c.getSuperclass();
    }
    return null;
  }

  public static boolean findMethod_checkArgs(Method m, Object[] args, boolean debug) {
    Class<?>[] types = m.getParameterTypes();
    if (types.length != args.length) {
      if (debug)
        System.out.println("Bad parameter length: " + args.length + " vs " + types.length);
      return false;
    }
    for (int i = 0; i < types.length; i++) if (!(args[i] == null || isInstanceX(types[i], args[i]))) {
      if (debug)
        System.out.println("Bad parameter " + i + ": " + args[i] + " vs " + types[i]);
      return false;
    }
    return true;
  }

  public static <A> List<A> dropFirstThreeAndLastThree(List<A> l) {
    return dropFirstAndLast(3, l);
  }

  public static List<String> nlTok(String s) {
    return javaTokPlusPeriod(s);
  }

  public static String containerTag(String tag) {
    return containerTag(tag, "");
  }

  public static String containerTag(String tag, Object contents, Object... params) {
    String openingTag = hopeningTag(tag, params);
    String s = str(contents);
    return openingTag + s + "</" + tag + ">";
  }

  public static Object selectedTableCell(JTable t, int col) {
    return getTableCell(t, t.getSelectedRow(), col);
  }

  public static Object selectedTableCell(JTable t) {
    return selectedTableCell(t, t.getSelectedColumn());
  }

  public static String asString(Object o) {
    return o == null ? null : o.toString();
  }

  public static boolean isSubstanceLAF() {
    return substanceLookAndFeelEnabled();
  }

  public static String lastJavaToken(String s) {
    return last(javaTokC(s));
  }

  public static void setConsoleFrame(JFrame frame) {
    setOpt(get(getJavaX(), "console"), "frame", frame);
  }

  public static int getScreenWidth() {
    return getScreenSize().width;
  }

  public static void nohupJavax(final String javaxargs) {
    {
      Thread _t_0 = new Thread() {

        public void run() {
          try {
            call(hotwireOnce("#1008562"), "nohupJavax", javaxargs);
          } catch (Throwable __e) {
            printStackTrace2(__e);
          }
        }
      };
      startThread(_t_0);
    }
  }

  public static void nohupJavax(final String javaxargs, final String vmArgs) {
    {
      Thread _t_1 = new Thread() {

        public void run() {
          try {
            call(hotwireOnce("#1008562"), "nohupJavax", javaxargs, vmArgs);
          } catch (Throwable __e) {
            printStackTrace2(__e);
          }
        }
      };
      startThread(_t_1);
    }
  }

  public static boolean regionMatchesIC(String a, int offsetA, String b, int offsetB, int len) {
    return a != null && a.regionMatches(true, offsetA, b, offsetB, len);
  }

  public static boolean isIdentifier(String s) {
    return isJavaIdentifier(s);
  }

  public static List scanStructureLog(String progID, String fileName) {
    return scanStructureLog(getProgramFile(progID, fileName));
  }

  public static List scanStructureLog(String fileName) {
    return scanStructureLog(getProgramFile(fileName));
  }

  public static List scanStructureLog(File file) {
    List l = new ArrayList();
    for (String s : scanLog(file)) try {
      l.add(unstructure(s));
    } catch (Throwable __e) {
      printStackTrace2(__e);
    }
    return l;
  }

  public static String getLookAndFeel() {
    return getClassName(UIManager.getLookAndFeel());
  }

  public static JCheckBoxMenuItem jCheckBoxMenuItem(String text, boolean checked, final Object r) {
    JCheckBoxMenuItem mi = new JCheckBoxMenuItem(text, checked);
    mi.addActionListener(actionListener(r));
    return mi;
  }

  public static String quoteUnlessIdentifierOrInteger(String s) {
    return quoteIfNotIdentifierOrInteger(s);
  }

  public static void cleanKillVM() {
    ping();
    cleanKillVM_noSleep();
    sleep();
  }

  public static void cleanKillVM_noSleep() {
    call(getJavaX(), "cleanKill");
  }

  public static char stringToChar(String s) {
    if (l(s) != 1)
      throw fail("bad stringToChar: " + s);
    return firstChar(s);
  }

  public static int smartIndexOf(String s, String sub, int i) {
    if (s == null)
      return 0;
    i = s.indexOf(sub, min(i, l(s)));
    return i >= 0 ? i : l(s);
  }

  public static int smartIndexOf(String s, int i, char c) {
    return smartIndexOf(s, c, i);
  }

  public static int smartIndexOf(String s, char c, int i) {
    if (s == null)
      return 0;
    i = s.indexOf(c, min(i, l(s)));
    return i >= 0 ? i : l(s);
  }

  public static int smartIndexOf(String s, String sub) {
    return smartIndexOf(s, sub, 0);
  }

  public static int smartIndexOf(String s, char c) {
    return smartIndexOf(s, c, 0);
  }

  public static <A> int smartIndexOf(List<A> l, A sub) {
    return smartIndexOf(l, sub, 0);
  }

  public static <A> int smartIndexOf(List<A> l, int start, A sub) {
    return smartIndexOf(l, sub, start);
  }

  public static <A> int smartIndexOf(List<A> l, A sub, int start) {
    int i = indexOf(l, sub, start);
    return i < 0 ? l(l) : i;
  }

  public static void shootWindowGUI_external(JFrame frame) {
    call(hotwireOnce("#1007178"), "shootWindowGUI", frame);
  }

  public static void shootWindowGUI_external(final JFrame frame, int delay) {
    call(hotwireOnce("#1007178"), "shootWindowGUI", frame, delay);
  }

  public static String standardCredentials() {
    String user = standardCredentialsUser();
    String pass = standardCredentialsPass();
    if (nempty(user) && nempty(pass))
      return "&_user=" + urlencode(user) + "&_pass=" + urlencode(pass);
    return "";
  }

  public static volatile String caseID_caseID;

  public static String caseID() {
    return caseID_caseID;
  }

  public static void caseID(String id) {
    caseID_caseID = id;
  }

  public static File standardLogFile() {
    return getProgramFile("log");
  }

  public static String postPage(String url, Object... params) {
    return doPost(litmap(params), url);
  }

  public static int getScreenHeight() {
    return getScreenSize().height;
  }

  public static void lockOrFail(Lock lock, long timeout) {
    try {
      ping();
      if (!lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
        String s = "Couldn't acquire lock after " + timeout + " ms.";
        if (lock instanceof ReentrantLock) {
          ReentrantLock l = (ReentrantLock) (lock);
          s += " Hold count: " + l.getHoldCount() + ", owner: " + call(l, "getOwner");
        }
        throw fail(s);
      }
      ping();
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String symbol(String s) {
    return s;
  }

  public static String symbol(CharSequence s) {
    if (s == null)
      return null;
    return str(s);
  }

  public static String symbol(Object o) {
    return symbol((CharSequence) o);
  }

  public static <A> int indexOfSubList(List<A> x, List<A> y) {
    return indexOfSubList(x, y, 0);
  }

  public static <A> int indexOfSubList(List<A> x, List<A> y, int i) {
    outer: for (; i + l(y) <= l(x); i++) {
      for (int j = 0; j < l(y); j++) if (neq(x.get(i + j), y.get(j)))
        continue outer;
      return i;
    }
    return -1;
  }

  public static <A> int indexOfSubList(List<A> x, A[] y, int i) {
    outer: for (; i + l(y) <= l(x); i++) {
      for (int j = 0; j < l(y); j++) if (neq(x.get(i + j), y[j]))
        continue outer;
      return i;
    }
    return -1;
  }

  public static <A, B> HashMap<A, B> pairsToMap(Collection<? extends Pair<A, B>> l) {
    HashMap<A, B> map = new HashMap();
    if (l != null)
      for (Pair<A, B> p : l) map.put(p.a, p.b);
    return map;
  }

  public static JFrame renewFrame(final JFrame frame) {
    if (frame == null)
      return null;
    return (JFrame) swing(new F0<Object>() {

      public Object get() {
        try {
          Container content = frame.getContentPane();
          JFrame frame2 = makeFrame(frame.getTitle());
          frame2.setBounds(frame.getBounds());
          try {
            frame2.setIconImages(frame.getIconImages());
          } catch (Throwable __e) {
            printStackTrace2(__e);
          }
          frame2.setDefaultCloseOperation(frame.getDefaultCloseOperation());
          boolean active = isActiveFrame(frame);
          print("renew: " + active);
          frame2.setAutoRequestFocus(active);
          for (WindowListener wl : frame.getWindowListeners()) frame2.addWindowListener(wl);
          frame.setContentPane(new JPanel());
          frame2.setContentPane(content);
          frame2.setVisible(true);
          frame.dispose();
          return frame2;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "Container content = frame.getContentPane();\r\n    JFrame frame2 = makeFrame(fr...";
      }
    });
  }

  public static String _computerID;

  public static String computerID() {
    try {
      if (_computerID == null) {
        File file = new File(userHome(), ".tinybrain/computer-id");
        _computerID = loadTextFile(file.getPath(), null);
        if (_computerID == null) {
          _computerID = makeRandomID(12);
          saveTextFile(file.getPath(), _computerID);
        }
      }
      return _computerID;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String structure_addTokenMarkers(String s) {
    List<String> tok = javaTok(s);
    TreeSet<Integer> refs = new TreeSet();
    for (int i = 1; i < l(tok); i += 2) {
      String t = tok.get(i);
      if (t.startsWith("t") && isInteger(t.substring(1)))
        refs.add(parseInt(t.substring(1)));
    }
    if (empty(refs))
      return s;
    for (int i : refs) {
      int idx = i * 2 + 1;
      String t = "";
      if (endsWithLetterOrDigit(tok.get(idx - 1)))
        t = " ";
      tok.set(idx, t + "m" + i + " " + tok.get(idx));
    }
    return join(tok);
  }

  public static boolean exposeMethods2_debug;

  public static String exposeMethods2(Object receiver, String s, List<String> methodNames) {
    return exposeMethods2(receiver, s, methodNames, null);
  }

  public static String exposeMethods2(Object receiver, String s, List<String> methodNames, Lock lock) {
    Matches m = new Matches();
    if (exposeMethods2_debug)
      print("Received: " + s);
    if (match("call *", s, m)) {
      List l;
      if (isIdentifier(m.unq(0)))
        l = ll(m.unq(0));
      else
        l = (List) unstructure(m.unq(0));
      String method = getString(l, 0);
      if (!contains(methodNames, method))
        throw fail("Method not allowed: " + method);
      if (lock != null)
        lock.lock();
      try {
        if (exposeMethods2_debug)
          print("Calling: " + method);
        Object o = call(receiver, method, asObjectArray(subList(l, 1)));
        if (exposeMethods2_debug)
          print("Got: " + getClassName(o));
        return ok2(structure(o));
      } finally {
        if (lock != null)
          lock.unlock();
      }
    }
    if (match("list methods", s))
      return ok2(structure(methodNames));
    return null;
  }

  public static boolean startsWithAndEndsWith(String s, String prefix, String suffix) {
    return startsWith(s, prefix) && endsWith(s, suffix);
  }

  public static byte[] convertToPNG(BufferedImage img) {
    try {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      ImageIO.write(img, "png", stream);
      return stream.toByteArray();
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static boolean isExtendedUnicodeCharacter(String s, int idx) {
    return Character.charCount(s.codePointAt(idx)) > 1;
  }

  public static File loadDataSnippetToFile(String snippetID) {
    try {
      snippetID = fsI(snippetID);
      File f = DiskSnippetCache_file(parseSnippetID(snippetID));
      List<URL> urlsTried = new ArrayList();
      List<Throwable> errors = new ArrayList();
      try {
        URL url = addAndReturn(urlsTried, new URL(dataSnippetLink(snippetID)));
        print("Loading library: " + hideCredentials(url));
        try {
          loadBinaryPageToFile(openConnection(url), f);
          if (fileSize(f) == 0)
            throw fail();
        } catch (Throwable e) {
          errors.add(e);
          url = addAndReturn(urlsTried, new URL("http://data.tinybrain.de/blobs/" + psI(snippetID)));
          print("Trying other server: " + hideCredentials(url));
          loadBinaryPageToFile(openConnection(url), f);
          print("Got bytes: " + fileSize(f));
        }
        if (fileSize(f) == 0)
          throw fail();
        System.err.println("Bytes loaded: " + fileSize(f));
      } catch (Throwable e) {
        printStackTrace(e);
        errors.add(e);
        throw fail("Binary snippet " + snippetID + " not found or not public. URLs tried: " + allToString(urlsTried) + ", errors: " + allToString(errors));
      }
      return f;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String string(Object o) {
    return String.valueOf(o);
  }

  public static File newFile(File base, String... names) {
    for (String name : names) base = new File(base, name);
    return base;
  }

  public static File newFile(String name) {
    return name == null ? null : new File(name);
  }

  public static ReentrantLock fairLock() {
    return new ReentrantLock(true);
  }

  public static <A, B> Pair<A, B> pair(A a, B b) {
    return new Pair(a, b);
  }

  public static <A> Pair<A, A> pair(A a) {
    return new Pair(a, a);
  }

  public static void consoleClearInput() {
    consoleSetInput("");
  }

  public static String intToHex(int i) {
    return bytesToHex(intToBytes(i));
  }

  public static File DiskSnippetCache_file(long snippetID) {
    return new File(getGlobalCache(), "data_" + snippetID + ".jar");
  }

  public static File DiskSnippetCache_getLibrary(long snippetID) throws IOException {
    File file = DiskSnippetCache_file(snippetID);
    return file.exists() ? file : null;
  }

  public static void DiskSnippetCache_putLibrary(long snippetID, byte[] data) throws IOException {
    saveBinaryFile(DiskSnippetCache_file(snippetID), data);
  }

  public static byte[] loadDataSnippetImpl(String snippetID) throws IOException {
    byte[] data;
    try {
      URL url = new URL("http://eyeocr.sourceforge.net/filestore/filestore.php?cmd=serve&file=blob_" + parseSnippetID(snippetID) + "&contentType=application/binary");
      System.err.println("Loading library: " + url);
      try {
        data = loadBinaryPage(url.openConnection());
      } catch (RuntimeException e) {
        data = null;
      }
      if (data == null || data.length == 0) {
        url = new URL("http://data.tinybrain.de/blobs/" + parseSnippetID(snippetID));
        System.err.println("Loading library: " + url);
        data = loadBinaryPage(url.openConnection());
      }
      System.err.println("Bytes loaded: " + data.length);
    } catch (FileNotFoundException e) {
      throw new IOException("Binary snippet #" + snippetID + " not found or not public");
    }
    return data;
  }

  public static boolean matchStart(String pat, String s) {
    return matchStart(pat, s, null);
  }

  public static boolean matchStart(String pat, String s, Matches matches) {
    if (s == null)
      return false;
    List<String> tokpat = parse3(pat), toks = parse3(s);
    if (toks.size() < tokpat.size())
      return false;
    String[] m = match2(tokpat, toks.subList(0, tokpat.size()));
    if (m == null)
      return false;
    else {
      if (matches != null) {
        matches.m = new String[m.length + 1];
        arraycopy(m, matches.m);
        matches.m[m.length] = join(toks.subList(tokpat.size(), toks.size()));
      }
      return true;
    }
  }

  public static boolean isNormalQuoted(String s) {
    int l = l(s);
    if (!(l >= 2 && s.charAt(0) == '"' && lastChar(s) == '"'))
      return false;
    int j = 1;
    while (j < l) if (s.charAt(j) == '"')
      return j == l - 1;
    else if (s.charAt(j) == '\\' && j + 1 < l)
      j += 2;
    else
      ++j;
    return false;
  }

  public static boolean isMultilineQuoted(String s) {
    if (!startsWith(s, "["))
      return false;
    int i = 1;
    while (i < s.length() && s.charAt(i) == '=') ++i;
    return i < s.length() && s.charAt(i) == '[';
  }

  public static String formatGMTWithDate_24(long time) {
    return simpleDateFormat_GMT("yyyy/MM/dd").format(time) + " " + formatGMT_24(time);
  }

  public static List<String> getClassNames(Collection l) {
    List<String> out = new ArrayList();
    if (l != null)
      for (Object o : l) out.add(o == null ? null : getClassName(o));
    return out;
  }

  public static int indexOfIgnoreCase_manual(String a, String b) {
    int la = l(a), lb = l(b);
    if (la < lb)
      return -1;
    int n = la - lb;
    loop: for (int i = 0; i <= n; i++) {
      for (int j = 0; j < lb; j++) {
        char c1 = a.charAt(i + j), c2 = b.charAt(j);
        if (!eqic(c1, c2))
          continue loop;
      }
      return i;
    }
    return -1;
  }

  public static void restart() {
    Object j = getJavaX();
    call(j, "cleanRestart", get(j, "fullArgs"));
  }

  public static File getProgramDir() {
    return programDir();
  }

  public static File getProgramDir(String snippetID) {
    return programDir(snippetID);
  }

  public static JMenuItem jMenuItem(String text, Object r) {
    return jmenuItem(text, r);
  }

  public static long fileSize(String path) {
    return getFileSize(path);
  }

  public static long fileSize(File f) {
    return getFileSize(f);
  }

  public static void toggleAlwaysOnTop(JFrame frame) {
    frame.setAlwaysOnTop(!frame.isAlwaysOnTop());
  }

  public static String fromUtf8(byte[] bytes) {
    try {
      return new String(bytes, "UTF-8");
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String sendToLocalBotOpt(String bot, String text, Object... args) {
    if (bot == null)
      return null;
    text = format(text, args);
    DialogIO channel = findBot(bot);
    if (channel == null) {
      print(quote(bot) + " not found, skipping send: " + quote(text));
      return null;
    }
    try {
      channel.readLine();
      print(shorten(bot + "> " + text, 200));
      channel.sendLine(text);
      String s = channel.readLine();
      print(shorten(bot + "< " + s, 200));
      return s;
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    } finally {
      channel.close();
    }
  }

  public static File userDir() {
    return new File(userHome());
  }

  public static File userDir(String path) {
    return new File(userHome(), path);
  }

  public static JComponent getTitlePaneComponent(Window window) {
    if (!substanceLookAndFeelEnabled())
      return null;
    JRootPane rootPane = null;
    if (window instanceof JFrame)
      rootPane = ((JFrame) window).getRootPane();
    if (window instanceof JDialog)
      rootPane = ((JDialog) window).getRootPane();
    if (rootPane != null) {
      Object ui = rootPane.getUI();
      return (JComponent) call(ui, "getTitlePane");
    }
    return null;
  }

  public static String hfulltag(String tag) {
    return hfulltag(tag, "");
  }

  public static String hfulltag(String tag, Object contents, Object... params) {
    return hopeningTag(tag, params) + str(contents) + "</" + tag + ">";
  }

  public static class componentPopupMenu_Maker {

    public List menuMakers = new ArrayList();
  }

  public static Map<JComponent, componentPopupMenu_Maker> componentPopupMenu_map = new WeakHashMap();

  public static ThreadLocal<MouseEvent> componentPopupMenu_mouseEvent = new ThreadLocal();

  public static void componentPopupMenu(final JComponent component, final Object menuMaker) {
    {
      swing(new Runnable() {

        public void run() {
          try {
            componentPopupMenu_Maker maker = componentPopupMenu_map.get(component);
            if (maker == null) {
              componentPopupMenu_map.put(component, maker = new componentPopupMenu_Maker());
              component.addMouseListener(new componentPopupMenu_Adapter(maker));
            }
            maker.menuMakers.add(menuMaker);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "componentPopupMenu_Maker maker = componentPopupMenu_map.get(component);\r\n    ...";
        }
      });
    }
  }

  public static class componentPopupMenu_Adapter extends MouseAdapter {

    public componentPopupMenu_Maker maker;

    public componentPopupMenu_Adapter(componentPopupMenu_Maker maker) {
      this.maker = maker;
    }

    public void mousePressed(MouseEvent e) {
      displayMenu(e);
    }

    public void mouseReleased(MouseEvent e) {
      displayMenu(e);
    }

    public void displayMenu(MouseEvent e) {
      if (e.isPopupTrigger())
        displayMenu2(e);
    }

    public void displayMenu2(MouseEvent e) {
      JPopupMenu menu = new JPopupMenu();
      int emptyCount = menu.getComponentCount();
      componentPopupMenu_mouseEvent.set(e);
      for (Object menuMaker : maker.menuMakers) pcallF(menuMaker, menu);
      if (menu.getComponentCount() != emptyCount)
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
  }

  public static File saveTextFile(String fileName, String contents) throws IOException {
    CriticalAction action = beginCriticalAction("Saving file " + fileName + " (" + l(contents) + " chars)");
    try {
      File file = new File(fileName);
      File parentFile = file.getParentFile();
      if (parentFile != null)
        parentFile.mkdirs();
      String tempFileName = fileName + "_temp";
      File tempFile = new File(tempFileName);
      if (contents != null) {
        if (tempFile.exists())
          try {
            String saveName = tempFileName + ".saved." + now();
            copyFile(tempFile, new File(saveName));
          } catch (Throwable e) {
            printStackTrace(e);
          }
        FileOutputStream fileOutputStream = newFileOutputStream(tempFile.getPath());
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
        PrintWriter printWriter = new PrintWriter(outputStreamWriter);
        printWriter.print(contents);
        printWriter.close();
      }
      if (file.exists() && !file.delete())
        throw new IOException("Can't delete " + fileName);
      if (contents != null)
        if (!tempFile.renameTo(file))
          throw new IOException("Can't rename " + tempFile + " to " + file);
      return file;
    } finally {
      action.done();
    }
  }

  public static File saveTextFile(File fileName, String contents) {
    try {
      saveTextFile(fileName.getPath(), contents);
      return fileName;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String sendToLocalBotQuietly(String bot, String text, Object... args) {
    text = format3(text, args);
    DialogIO channel = newFindBot2(bot);
    if (channel == null)
      throw fail(quote(bot) + " not found");
    try {
      channel.readLine();
      channel.sendLine(text);
      String s = channel.readLine();
      return s;
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    } finally {
      channel.close();
    }
  }

  public static String sendToLocalBotQuietly(int port, String text, Object... args) {
    text = format3(text, args);
    DialogIO channel = talkTo(port);
    try {
      channel.readLine();
      channel.sendLine(text);
      String s = channel.readLine();
      return s;
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    } finally {
      if (channel != null)
        channel.close();
    }
  }

  public static void saveBinaryFile(String fileName, byte[] contents) throws IOException {
    File file = new File(fileName);
    File parentFile = file.getParentFile();
    if (parentFile != null)
      parentFile.mkdirs();
    String tempFileName = fileName + "_temp";
    FileOutputStream fileOutputStream = newFileOutputStream(tempFileName);
    fileOutputStream.write(contents);
    fileOutputStream.close();
    if (file.exists() && !file.delete())
      throw new IOException("Can't delete " + fileName);
    if (!new File(tempFileName).renameTo(file))
      throw new IOException("Can't rename " + tempFileName + " to " + fileName);
  }

  public static void saveBinaryFile(File fileName, byte[] contents) {
    try {
      saveBinaryFile(fileName.getPath(), contents);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getString(Map map, Object key) {
    return map == null ? null : (String) map.get(key);
  }

  public static String getString(List l, int idx) {
    return (String) get(l, idx);
  }

  public static String getString(Object o, Object key) {
    if (o instanceof Map)
      return getString((Map) o, key);
    if (key instanceof String)
      return (String) getOpt(o, (String) key);
    throw fail("Not a string key: " + getClassName(key));
  }

  public static String dataSnippetLink(String snippetID) {
    long id = parseSnippetID(snippetID);
    if (id >= 1100000 && id < 1200000)
      return "http://ai1.space/images/raw/" + id;
    if (id >= 1400000 && id < 1500000)
      return "http://butter.botcompany.de:8080/files/" + id + "?_pass=" + muricaPassword();
    if (id >= 1200000 && id < 1300000) {
      String pw = muricaPassword();
      if (empty(pw))
        throw fail("Please set 'murica password by running #1008829");
      return "http://butter.botcompany.de:8080/1008823/raw/" + id + "?_pass=" + pw;
    } else
      return "http://eyeocr.sourceforge.net/filestore/filestore.php?cmd=serve&file=blob_" + id + "&contentType=application/binary";
  }

  public static SimpleDateFormat simpleDateFormat_GMT(String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    return sdf;
  }

  public static long getFileSize(String path) {
    return path == null ? 0 : new File(path).length();
  }

  public static long getFileSize(File f) {
    return f == null ? 0 : f.length();
  }

  public static int findEndOfCurlyBracketPart(List<String> cnc, int i) {
    int j = i + 2, level = 1;
    while (j < cnc.size()) {
      if (eq(cnc.get(j), "{"))
        ++level;
      else if (eq(cnc.get(j), "}"))
        --level;
      if (level == 0)
        return j + 1;
      ++j;
    }
    return cnc.size();
  }

  public static String formatGMT_24(long time) {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    return sdf.format(time) + " GMT";
  }

  public static String quoteIfNotIdentifierOrInteger(String s) {
    if (s == null)
      return null;
    return isJavaIdentifier(s) || isInteger(s) ? s : quote(s);
  }

  public static boolean endsWithLetterOrDigit(String s) {
    return nempty(s) && isLetterOrDigit(lastCharacter(s));
  }

  public static void loadBinaryPageToFile(String url, File file) {
    try {
      print("Loading " + url);
      loadBinaryPageToFile(openConnection(new URL(url)), file);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void loadBinaryPageToFile(URLConnection con, File file) {
    try {
      setHeaders(con);
      loadBinaryPageToFile_noHeaders(con, file);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void loadBinaryPageToFile_noHeaders(URLConnection con, File file) {
    try {
      File ftemp = new File(f2s(file) + "_temp");
      FileOutputStream buf = newFileOutputStream(mkdirsFor(ftemp));
      try {
        InputStream inputStream = con.getInputStream();
        long len = 0;
        try {
          len = con.getContentLength();
        } catch (Throwable e) {
          printStackTrace(e);
        }
        String pat = "  {*}" + (len != 0 ? "/" + len : "") + " bytes loaded.";
        copyStreamWithPrints(inputStream, buf, pat);
        inputStream.close();
        buf.close();
        file.delete();
        renameFile_assertTrue(ftemp, file);
      } finally {
        if (buf != null)
          buf.close();
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static boolean isActiveFrame(Component c) {
    JFrame f = getFrame(c);
    return f != null && f.isFocused();
  }

  public static Lock appendToFile_lock = lock();

  public static boolean appendToFile_keepOpen;

  public static HashMap<String, Writer> appendToFile_writers = new HashMap();

  public static void appendToFile(String path, String s) {
    try {
      Lock __299 = appendToFile_lock;
      lock(__299);
      try {
        mkdirsForFile(new File(path));
        path = getCanonicalPath(path);
        Writer writer = appendToFile_writers.get(path);
        if (writer == null) {
          writer = new BufferedWriter(new OutputStreamWriter(newFileOutputStream(path, true), "UTF-8"));
          if (appendToFile_keepOpen)
            appendToFile_writers.put(path, writer);
        }
        writer.write(s);
        if (!appendToFile_keepOpen)
          writer.close();
      } finally {
        unlock(__299);
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void appendToFile(File path, String s) {
    if (path != null)
      appendToFile(path.getPath(), s);
  }

  public static void cleanMeUp_appendToFile() {
    Lock __300 = appendToFile_lock;
    lock(__300);
    try {
      closeAllWriters(values(appendToFile_writers));
      appendToFile_writers.clear();
    } finally {
      unlock(__300);
    }
  }

  public static char firstChar(String s) {
    return s.charAt(0);
  }

  public static String ok2(String s) {
    return "ok " + s;
  }

  public static ThreadLocal<Map<String, List<String>>> loadBinaryPage_responseHeaders = new ThreadLocal();

  public static ThreadLocal<Map<String, String>> loadBinaryPage_extraHeaders = new ThreadLocal();

  public static byte[] loadBinaryPage(String url) {
    try {
      print("Loading " + url);
      return loadBinaryPage(loadPage_openConnection(new URL(url)));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static byte[] loadBinaryPage(URLConnection con) {
    try {
      Map<String, String> extraHeaders = getAndClearThreadLocal(loadBinaryPage_extraHeaders);
      setHeaders(con);
      for (String key : keys(extraHeaders)) con.setRequestProperty(key, extraHeaders.get(key));
      return loadBinaryPage_noHeaders(con);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static byte[] loadBinaryPage_noHeaders(URLConnection con) {
    try {
      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      InputStream inputStream = con.getInputStream();
      loadBinaryPage_responseHeaders.set(con.getHeaderFields());
      long len = 0;
      try {
        len = con.getContentLength();
      } catch (Throwable e) {
        printStackTrace(e);
      }
      int n = 0;
      while (true) {
        int ch = inputStream.read();
        if (ch < 0)
          break;
        buf.write(ch);
        if (++n % 100000 == 0)
          println("  " + n + (len != 0 ? "/" + len : "") + " bytes loaded.");
      }
      inputStream.close();
      return buf.toByteArray();
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static <A> List<A> dropFirstAndLast(int n, List<A> l) {
    return new ArrayList(subList(l, n, l(l) - n));
  }

  public static <A> List<A> dropFirstAndLast(List<A> l) {
    return dropFirstAndLast(1, l);
  }

  public static String dropFirstAndLast(String s) {
    return substring(s, 1, l(s) - 1);
  }

  public static DialogIO talkToSubBot(final long vport, final DialogIO io) {
    return talkToSubBot(String.valueOf(vport), io);
  }

  public static DialogIO talkToSubBot(final String subBot, final DialogIO io) {
    if (subBot == null)
      return io;
    return new talkToSubBot_IO(subBot, io);
  }

  public static class talkToSubBot_IO extends DialogIO {

    public String subBot;

    public DialogIO io;

    public talkToSubBot_IO(String subBot, DialogIO io) {
      this.io = io;
      this.subBot = subBot;
    }

    public boolean isStillConnected() {
      return io.isStillConnected();
    }

    public String readLineImpl() {
      return io.readLineImpl();
    }

    public boolean isLocalConnection() {
      return io.isLocalConnection();
    }

    public Socket getSocket() {
      return io.getSocket();
    }

    public void close() {
      io.close();
    }

    public void sendLine(String line) {
      io.sendLine(format3("please forward to bot *: *", subBot, line));
    }
  }

  public static void arraycopy(Object[] a, Object[] b) {
    int n = min(a.length, b.length);
    for (int i = 0; i < n; i++) b[i] = a[i];
  }

  public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int n) {
    System.arraycopy(src, srcPos, dest, destPos, n);
  }

  public static <B, A extends B> A addAndReturn(Collection<B> c, A a) {
    if (c != null)
      c.add(a);
    return a;
  }

  public static List<String> scanLog(String progID, String fileName) {
    return scanLog(getProgramFile(progID, fileName));
  }

  public static List<String> scanLog(String fileName) {
    return scanLog(getProgramFile(fileName));
  }

  public static List<String> scanLog(File file) {
    List<String> l = new ArrayList();
    for (File f : concatLists(earlierPartsOfLogFile(file), ll(file))) for (String s : toLines(file)) if (isProperlyQuoted(s))
      l.add(unquote(s));
    return l;
  }

  public static File getGlobalCache() {
    File file = new File(javaxCachesDir(), "Binary Snippets");
    file.mkdirs();
    return file;
  }

  public static String firstPartOfHelloString(String s) {
    int i = s.lastIndexOf('/');
    return i < 0 ? s : rtrim(s.substring(0, i));
  }

  public static File programDir_mine;

  public static File programDir() {
    return programDir(getProgramID());
  }

  public static File programDir(String snippetID) {
    boolean me = sameSnippetID(snippetID, programID());
    if (programDir_mine != null && me)
      return programDir_mine;
    File dir = new File(javaxDataDir(), formatSnippetID(snippetID));
    if (me) {
      String c = caseID();
      if (nempty(c))
        dir = newFile(dir, c);
    }
    return dir;
  }

  public static File programDir(String snippetID, String subPath) {
    return new File(programDir(snippetID), subPath);
  }

  public static boolean checkCondition(Object condition, Object... args) {
    return isTrue(callF(condition, args));
  }

  public static void consoleSetInput(final String text) {
    if (headless())
      return;
    setTextAndSelectAll(consoleInputField(), text);
    focusConsole();
  }

  public static List<ProgramScan.Program> quickBotScan() {
    return ProgramScan.quickBotScan();
  }

  public static List<ProgramScan.Program> quickBotScan(int[] preferredPorts) {
    return ProgramScan.quickBotScan(preferredPorts);
  }

  public static List<ProgramScan.Program> quickBotScan(String searchPattern) {
    List<ProgramScan.Program> l = new ArrayList<ProgramScan.Program>();
    for (ProgramScan.Program p : ProgramScan.quickBotScan()) if (indexOfIgnoreCase(p.helloString, searchPattern) == 0)
      l.add(p);
    return l;
  }

  public static String standardCredentialsUser() {
    return trim(loadTextFile(new File(userHome(), ".tinybrain/username")));
  }

  public static DialogIO talkTo(int port) {
    return talkTo("localhost", port);
  }

  public static int talkTo_defaultTimeout = 10000;

  public static int talkTo_timeoutForReads = 0;

  public static ThreadLocal<Map<String, DialogIO>> talkTo_byThread = new ThreadLocal();

  public static DialogIO talkTo(String ip, int port) {
    try {
      String full = ip + ":" + port;
      Map<String, DialogIO> map = talkTo_byThread.get();
      if (map != null && map.containsKey(full))
        return map.get(full);
      if (isLocalhost(ip) && port == vmPort())
        return talkToThisVM();
      return new talkTo_IO(ip, port);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static class talkTo_IO extends DialogIO {

    public String ip;

    public int port;

    public Socket s;

    public Writer w;

    public BufferedReader in;

    public talkTo_IO(String ip, int port) {
      this.port = port;
      this.ip = ip;
      try {
        s = new Socket();
        try {
          if (talkTo_timeoutForReads != 0)
            s.setSoTimeout(talkTo_timeoutForReads);
          s.connect(new InetSocketAddress(ip, port), talkTo_defaultTimeout);
        } catch (Throwable e) {
          throw fail("Tried talking to " + ip + ":" + port, e);
        }
        w = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
        in = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
      } catch (Exception __e) {
        throw rethrow(__e);
      }
    }

    public boolean isLocalConnection() {
      return s.getInetAddress().isLoopbackAddress();
    }

    public boolean isStillConnected() {
      return !(eos || s.isClosed());
    }

    public void sendLine(String line) {
      try {
        w.write(line + "\n");
        w.flush();
      } catch (Exception __e) {
        throw rethrow(__e);
      }
    }

    public String readLineImpl() {
      try {
        return in.readLine();
      } catch (Exception __e) {
        throw rethrow(__e);
      }
    }

    public void close() {
      try {
        if (!noClose)
          s.close();
      } catch (IOException e) {
      }
    }

    public Socket getSocket() {
      return s;
    }
  }

  public static String joinSubList(List<String> l, int i, int j) {
    return join(subList(l, i, j));
  }

  public static String joinSubList(List<String> l, int i) {
    return join(subList(l, i));
  }

  public static List<String> allToString(Collection c) {
    List<String> l = new ArrayList();
    for (Object o : c) l.add(str(o));
    return l;
  }

  public static String[] allToString(Object[] c) {
    String[] l = new String[l(c)];
    for (int i = 0; i < l(c); i++) l[i] = str(c[i]);
    return l;
  }

  public static String hopeningTag(String tag, Object... params) {
    StringBuilder buf = new StringBuilder();
    buf.append("<" + tag);
    for (int i = 0; i < l(params); i += 2) {
      String name = (String) get(params, i);
      Object val = get(params, i + 1);
      if (nempty(name) && val != null) {
        String s = str(val);
        if (!empty(s))
          buf.append(" " + name + "=" + htmlQuote(s));
      }
    }
    buf.append(">");
    return str(buf);
  }

  public static String format(String pat, Object... args) {
    return format3(pat, args);
  }

  public static BigInteger bigint(String s) {
    return new BigInteger(s);
  }

  public static BigInteger bigint(long l) {
    return BigInteger.valueOf(l);
  }

  public static Class hotwireOnce(String programID) {
    return hotwireCached(programID, false);
  }

  public static String standardCredentialsPass() {
    return trim(loadTextFile(new File(userHome(), ".tinybrain/userpass")));
  }

  public static JTextField setTextAndSelectAll(final JTextField tf, final String text) {
    {
      swing(new Runnable() {

        public void run() {
          try {
            tf.setText(text);
            tf.selectAll();
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "tf.setText(text);\r\n    tf.selectAll();";
        }
      });
    }
    return tf;
  }

  public static int vmPort() {
    return myVMPort();
  }

  public static String format3(String pat, Object... args) {
    if (args.length == 0)
      return pat;
    List<String> tok = javaTokPlusPeriod(pat);
    int argidx = 0;
    for (int i = 1; i < tok.size(); i += 2) if (tok.get(i).equals("*"))
      tok.set(i, format3_formatArg(argidx < args.length ? args[argidx++] : "null"));
    return join(tok);
  }

  public static String format3_formatArg(Object arg) {
    if (arg == null)
      return "null";
    if (arg instanceof String) {
      String s = (String) arg;
      return isIdentifier(s) || isNonNegativeInteger(s) ? s : quote(s);
    }
    if (arg instanceof Integer || arg instanceof Long)
      return String.valueOf(arg);
    return quote(structure(arg));
  }

  public static String htmlQuote(String s) {
    return "\"" + htmlencode_forParams(s) + "\"";
  }

  public static DialogIO talkToThisVM() {
    return new talkToThisVM_IO();
  }

  public static class talkToThisVM_IO extends DialogIO {

    public List<String> answers = ll(thisVMGreeting());

    public boolean isLocalConnection() {
      return true;
    }

    public boolean isStillConnected() {
      return true;
    }

    public int getPort() {
      return vmPort();
    }

    public void sendLine(String line) {
      answers.add(or2(sendToThisVM_newThread(line), "?"));
    }

    public String readLineImpl() {
      try {
        return popFirst(answers);
      } catch (Exception __e) {
        throw rethrow(__e);
      }
    }

    public void close() {
    }

    public Socket getSocket() {
      return null;
    }
  }

  public static boolean isLetterOrDigit(char c) {
    return Character.isLetterOrDigit(c);
  }

  public static File mkdirsFor(File file) {
    return mkdirsForFile(file);
  }

  public static volatile boolean muricaPassword_pretendNotAuthed;

  public static String muricaPassword() {
    if (muricaPassword_pretendNotAuthed)
      return null;
    return trim(loadTextFile(muricaPasswordFile()));
  }

  public static JTextField consoleInputField() {
    Object console = get(getJavaX(), "console");
    return (JTextField) getOpt(console, "tfInput");
  }

  public static File javaxCachesDir_dir;

  public static File javaxCachesDir() {
    return javaxCachesDir_dir != null ? javaxCachesDir_dir : new File(userHome(), "JavaX-Caches");
  }

  public static File javaxCachesDir(String sub) {
    return newFile(javaxCachesDir(), sub);
  }

  public static boolean sameSnippetID(String a, String b) {
    if (!isSnippetID(a) || !isSnippetID(b))
      return false;
    return parseSnippetID(a) == parseSnippetID(b);
  }

  public static char lastCharacter(String s) {
    return empty(s) ? 0 : s.charAt(l(s) - 1);
  }

  public static Map<String, Integer> newFindBot2_cache = synchroHashMap();

  public static boolean newFindBot2_verbose;

  public static DialogIO newFindBot2(String name) {
    Integer port = newFindBot2_cache.get(name);
    if (port != null) {
      if (newFindBot2_verbose)
        print("newFindBot2: testing " + name + " => " + port);
      DialogIO io = talkTo(port);
      String q = format("has bot *", name);
      String s = io.ask(q);
      if (match("yes", s)) {
        io = talkToSubBot(name, io);
        call(io, "pushback", "?");
        return io;
      }
      newFindBot2_cache.remove(name);
      if (newFindBot2_verbose)
        print("newFindBot2: dropping " + name + " => " + port);
    }
    DialogIO io = findBot(name);
    if (io != null) {
      newFindBot2_cache.put(name, io.getPort());
      if (newFindBot2_verbose)
        print("newFindBot2: remembering " + name + " => " + port);
    }
    return io;
  }

  public static List<CriticalAction> beginCriticalAction_inFlight = synchroList();

  public static class CriticalAction {

    public String description;

    public CriticalAction() {
    }

    public CriticalAction(String description) {
      this.description = description;
    }

    public void done() {
      beginCriticalAction_inFlight.remove(this);
    }
  }

  public static CriticalAction beginCriticalAction(String description) {
    ping();
    CriticalAction c = new CriticalAction(description);
    beginCriticalAction_inFlight.add(c);
    return c;
  }

  public static void cleanMeUp_beginCriticalAction() {
    int n = 0;
    while (nempty(beginCriticalAction_inFlight)) {
      int m = l(beginCriticalAction_inFlight);
      if (m != n) {
        n = m;
        try {
          print("Waiting for " + n(n, "critical actions") + ": " + join(", ", collect(beginCriticalAction_inFlight, "description")));
        } catch (Throwable __e) {
          printStackTrace2(__e);
        }
      }
      sleepInCleanUp(10);
    }
  }

  public static TreeMap<String, Class> hotwireCached_cache = new TreeMap();

  public static Lock hotwireCached_lock = lock();

  public static Class hotwireCached(String programID) {
    return hotwireCached(programID, true);
  }

  public static Class hotwireCached(String programID, boolean runMain) {
    return hotwireCached(programID, runMain, false);
  }

  public static Class hotwireCached(String programID, boolean runMain, boolean dependent) {
    Lock __1577 = hotwireCached_lock;
    lock(__1577);
    try {
      programID = formatSnippetID(programID);
      Class c = hotwireCached_cache.get(programID);
      if (c == null) {
        c = hotwire(programID);
        if (dependent)
          makeDependent(c);
        if (runMain)
          callMain(c);
        hotwireCached_cache.put(programID, c);
      }
      return c;
    } finally {
      unlock(__1577);
    }
  }

  public static boolean isProperlyQuoted(String s) {
    return s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"") && (!s.endsWith("\\\"") || s.endsWith("\\\\\""));
  }

  public static String getCanonicalPath(String path) {
    try {
      return new File(path).getCanonicalPath();
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static FileOutputStream newFileOutputStream(File path) throws IOException {
    return newFileOutputStream(path.getPath());
  }

  public static FileOutputStream newFileOutputStream(String path) throws IOException {
    return newFileOutputStream(path, false);
  }

  public static FileOutputStream newFileOutputStream(String path, boolean append) throws IOException {
    mkdirsForFile(path);
    FileOutputStream f = new FileOutputStream(path, append);
    callJavaX("registerIO", f, path, true);
    return f;
  }

  public static void focusConsole(String s) {
    setConsoleInput(s);
    focusConsole();
  }

  public static void focusConsole() {
    JComponent tf = consoleInputFieldOrComboBox();
    if (tf != null) {
      tf.requestFocus();
    }
  }

  public static void copyStreamWithPrints(InputStream in, OutputStream out, String pat) {
    try {
      byte[] buf = new byte[65536];
      int total = 0;
      while (true) {
        int n = in.read(buf);
        if (n <= 0)
          return;
        out.write(buf, 0, n);
        if ((total + n) / 100000 > total / 100000)
          print(pat.replace("{*}", str(roundDownTo(total, 100000))));
        total += n;
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void closeAllWriters(Collection<? extends Writer> l) {
    for (Writer w : unnull(l)) try {
      w.close();
    } catch (Throwable __e) {
      printStackTrace2(__e);
    }
  }

  public static void renameFile_assertTrue(File a, File b) {
    try {
      if (!a.exists())
        throw fail("Source file not found: " + f2s(a));
      if (b.exists())
        throw fail("Target file exists: " + f2s(b));
      mkdirsForFile(b);
      if (!a.renameTo(b))
        throw fail("Can't rename " + f2s(a) + " to " + f2s(b));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String f2s(File f) {
    return f == null ? null : f.getAbsolutePath();
  }

  public static <A> A println(A a) {
    return print(a);
  }

  public static List<File> earlierPartsOfLogFile(File file) {
    String name = file.getName() + ".part";
    try {
      Matches m = new Matches();
      TreeMap<Integer, File> map = new TreeMap();
      for (File p : listFiles(file.getParent())) try {
        String n = p.getName();
        if (startsWith(n, name, m))
          map.put(parseFirstInt(m.rest()), p);
      } catch (Throwable __e) {
        printStackTrace2(__e);
      }
      return valuesList(map);
    } catch (Throwable e) {
      printException(e);
      return ll();
    }
  }

  public static File mkdirsForFile(File file) {
    File dir = file.getParentFile();
    if (dir != null)
      dir.mkdirs();
    return file;
  }

  public static String mkdirsForFile(String path) {
    mkdirsForFile(new File(path));
    return path;
  }

  public static void copyFile(File src, File dest) {
    try {
      FileInputStream inputStream = new FileInputStream(src.getPath());
      FileOutputStream outputStream = newFileOutputStream(dest.getPath());
      try {
        copyStream(inputStream, outputStream);
        inputStream.close();
      } finally {
        outputStream.close();
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static IterableIterator<String> toLines(File f) {
    return linesFromFile(f);
  }

  public static List<String> toLines(String s) {
    List<String> lines = new ArrayList<String>();
    if (s == null)
      return lines;
    int start = 0;
    while (true) {
      int i = toLines_nextLineBreak(s, start);
      if (i < 0) {
        if (s.length() > start)
          lines.add(s.substring(start));
        break;
      }
      lines.add(s.substring(start, i));
      if (s.charAt(i) == '\r' && i + 1 < s.length() && s.charAt(i + 1) == '\n')
        i += 2;
      else
        ++i;
      start = i;
    }
    return lines;
  }

  public static int toLines_nextLineBreak(String s, int start) {
    for (int i = start; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == '\r' || c == '\n')
        return i;
    }
    return -1;
  }

  public static boolean isLocalhost(String ip) {
    return isLoopbackIP(ip) || eqic(ip, "localhost");
  }

  public static File muricaPasswordFile() {
    return new File(javaxSecretDir(), "murica/muricaPasswordFile");
  }

  public static String thisVMGreeting() {
    List record_list = (List) (get(getJavaX(), "record_list"));
    Object android = first(record_list);
    return getString(android, "greeting");
  }

  public static void setConsoleInput(String text) {
    consoleSetInput(text);
  }

  public static boolean isNonNegativeInteger(String s) {
    return s != null && Pattern.matches("\\d+", s);
  }

  public static void sleepInCleanUp(long ms) {
    try {
      if (ms < 0)
        return;
      Thread.sleep(ms);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void makeDependent(Object c) {
    if (c == null)
      return;
    assertTrue("Not a class", c instanceof Class);
    hotwire_classes.add(new WeakReference(c));
    Object local_log = getOpt(mc(), "local_log");
    if (local_log != null)
      setOpt(c, "local_log", local_log);
    Object print_byThread = getOpt(mc(), "print_byThread");
    if (print_byThread != null)
      setOpt(c, "print_byThread", print_byThread);
  }

  public static File[] listFiles(File dir) {
    File[] files = dir.listFiles();
    return files == null ? new File[0] : files;
  }

  public static File[] listFiles(String dir) {
    return listFiles(new File(dir));
  }

  public static <A, B> List<B> valuesList(Map<A, B> map) {
    return cloneListSynchronizingOn(values(map), map);
  }

  public static int myVMPort() {
    List records = (List) (get(getJavaX(), "record_list"));
    Object android = records.get(records.size() - 1);
    return (Integer) get(android, "port");
  }

  public static JComponent consoleInputFieldOrComboBox() {
    Object console = get(getJavaX(), "console");
    JComboBox cb = (JComboBox) (getOpt(console, "cbInput"));
    if (cb != null)
      return cb;
    return (JTextField) getOpt(console, "tfInput");
  }

  public static int parseFirstInt(String s) {
    return parseInt(jextract("<int>", s));
  }

  public static <A> A callJavaX(String method, Object... args) {
    return (A) callOpt(getJavaX(), method, args);
  }

  public static String htmlencode_forParams(String s) {
    if (s == null)
      return "";
    StringBuilder out = new StringBuilder(Math.max(16, s.length()));
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c > 127 || c == '"' || c == '<' || c == '>') {
        out.append("&#");
        out.append((int) c);
        out.append(';');
      } else
        out.append(c);
    }
    return out.toString();
  }

  public static int roundDownTo(int x, int n) {
    return x / n * n;
  }

  public static long roundDownTo(long x, long n) {
    return x / n * n;
  }

  public static boolean isLoopbackIP(String ip) {
    return eq(ip, "127.0.0.1");
  }

  public static IterableIterator<String> linesFromFile(File f) {
    try {
      if (!f.exists())
        return emptyIterableIterator();
      if (ewic(f.getName(), ".gz"))
        return linesFromReader(utf8bufferedReader(newGZIPInputStream(f)));
      return linesFromReader(utf8bufferedReader(f));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void copyStream(InputStream in, OutputStream out) {
    try {
      byte[] buf = new byte[65536];
      while (true) {
        int n = in.read(buf);
        if (n <= 0)
          return;
        out.write(buf, 0, n);
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String sendToThisVM_newThread(String s, Object... args) {
    final String _s = format(s, args);
    try {
      return (String) evalInNewThread(new F0<Object>() {

        public Object get() {
          try {
            return callStaticAnswerMethod(getJavaX(), _s);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "callStaticAnswerMethod(getJavaX(), _s)";
        }
      });
    } catch (Throwable e) {
      e = getInnerException(e);
      printStackTrace(e);
      return str(e);
    }
  }

  public static <A> A popFirst(List<A> l) {
    if (empty(l))
      return null;
    A a = first(l);
    l.remove(0);
    return a;
  }

  public static IterableIterator emptyIterableIterator_instance = new IterableIterator() {

    public Object next() {
      throw fail();
    }

    public boolean hasNext() {
      return false;
    }
  };

  public static <A> IterableIterator<A> emptyIterableIterator() {
    return emptyIterableIterator_instance;
  }

  public static List<WeakReference<Class>> hotwire_classes = synchroList();

  public static Class<?> hotwireDependent(String src) {
    Class c = hotwire(src);
    makeDependent(c);
    return c;
  }

  public static File javaxSecretDir_dir;

  public static File javaxSecretDir() {
    return javaxSecretDir_dir != null ? javaxSecretDir_dir : new File(userHome(), "JavaX-Secret");
  }

  public static <A> ArrayList<A> cloneListSynchronizingOn(Collection<A> l, Object mutex) {
    if (l == null)
      return new ArrayList();
    synchronized (mutex) {
      return new ArrayList<A>(l);
    }
  }

  public static IterableIterator<String> linesFromReader(Reader r) {
    final BufferedReader br = bufferedReader(r);
    return iteratorFromFunction_f0(new F0<String>() {

      public String get() {
        try {
          return readLineFromReaderWithClose(br);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "readLineFromReaderWithClose(br)";
      }
    });
  }

  public static Object evalInNewThread(final Object f) {
    final Flag flag = new Flag();
    final Var var = new Var();
    final Var<Throwable> exception = new Var();
    {
      Thread _t_0 = new Thread() {

        public void run() {
          try {
            try {
              var.set(callF(f));
            } catch (Throwable e) {
              exception.set(e);
            }
            flag.raise();
          } catch (Throwable __e) {
            printStackTrace2(__e);
          }
        }
      };
      startThread(_t_0);
    }
    flag.waitUntilUp();
    if (exception.has())
      throw rethrow(exception.get());
    return var.get();
  }

  public static BufferedReader utf8bufferedReader(InputStream in) {
    try {
      return new BufferedReader(new InputStreamReader(in, "UTF-8"));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static BufferedReader utf8bufferedReader(File f) {
    try {
      return utf8bufferedReader(newFileInputStream(f));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static String readLineFromReaderWithClose(BufferedReader r) {
    try {
      String s = r.readLine();
      if (s == null)
        r.close();
      return s;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static <A> IterableIterator<A> iteratorFromFunction_f0(final F0<A> f) {
    class IFF2 extends IterableIterator<A> {

      public A a;

      public boolean done;

      public boolean hasNext() {
        getNext();
        return !done;
      }

      public A next() {
        getNext();
        if (done)
          throw fail();
        A _a = a;
        a = null;
        return _a;
      }

      public void getNext() {
        if (done || a != null)
          return;
        a = f.get();
        done = a == null;
      }
    }
    ;
    return new IFF2();
  }

  public static BufferedReader bufferedReader(Reader r) {
    return r instanceof BufferedReader ? (BufferedReader) r : new BufferedReader(r);
  }

  public static FileInputStream newFileInputStream(File path) throws IOException {
    return newFileInputStream(path.getPath());
  }

  public static FileInputStream newFileInputStream(String path) throws IOException {
    FileInputStream f = new FileInputStream(path);
    return f;
  }

  public abstract static class VF1<A> {

    public abstract void get(A a);
  }

  public abstract static class VF2<A, B> {

    public abstract void get(A a, B b);
  }

  public static class ProgramScan {

    public static int threads = isWindows() ? 500 : 10;

    public static int timeout = 5000;

    public static String ip = "127.0.0.1";

    public static int quickScanFrom = 10000, quickScanTo = 10999;

    public static int maxNumberOfVMs_android = 4;

    public static int maxNumberOfVMs_nonAndroid = 50;

    public static int maxNumberOfVMs;

    public static boolean verbose;

    public static class Program {

      public int port;

      public String helloString;

      public Program(int port, String helloString) {
        this.helloString = helloString;
        this.port = port;
      }
    }

    public static List<Program> scan() {
      try {
        return scan(1, 65535);
      } catch (Exception __e) {
        throw rethrow(__e);
      }
    }

    public static List<Program> scan(int fromPort, int toPort) {
      return scan(fromPort, toPort, new int[0]);
    }

    public static List<Program> scan(int fromPort, int toPort, int[] preferredPorts) {
      try {
        Set<Integer> preferredPortsSet = new HashSet<Integer>(asList(preferredPorts));
        int scanSize = toPort - fromPort + 1;
        String name = toPort < 10000 ? "bot" : "program";
        int threads = isWindows() ? min(500, scanSize) : min(scanSize, 10);
        final ExecutorService es = Executors.newFixedThreadPool(threads);
        if (verbose)
          print(firstToUpper(name) + "-scanning " + ip + " with timeout " + timeout + " ms in " + threads + " threads.");
        startTiming();
        List<Future<Program>> futures = new ArrayList();
        List<Integer> ports = new ArrayList();
        for (int port : preferredPorts) {
          futures.add(checkPort(es, ip, port, timeout));
          ports.add(port);
        }
        for (int port = fromPort; port <= toPort; port++) if (!preferredPortsSet.contains(port) && !forbiddenPort(port)) {
          futures.add(checkPort(es, ip, port, timeout));
          ports.add(port);
        }
        es.shutdown();
        List<Program> programs = new ArrayList();
        long time = now();
        int i = 0;
        for (final Future<Program> f : futures) {
          if (verbose)
            print("Waiting for port " + get(ports, i++) + " at time " + (now() - time));
          Program p = f.get();
          if (p != null)
            programs.add(p);
        }
        if (verbose)
          print("Found " + programs.size() + " " + name + "(s) on " + ip);
        return programs;
      } catch (Exception __e) {
        throw rethrow(__e);
      }
    }

    public static Future<Program> checkPort(final ExecutorService es, final String ip, final int port, final int timeout) {
      return es.submit(new Callable<Program>() {

        @Override
        public Program call() {
          try {
            Socket socket = new Socket();
            try {
              socket.setSoTimeout(timeout);
              socket.connect(new InetSocketAddress(ip, port), timeout);
              BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
              String hello = or(in.readLine(), "?");
              return new Program(port, hello);
            } finally {
              socket.close();
            }
          } catch (Exception ex) {
            return null;
          }
        }
      });
    }

    public static List<Program> quickScan() {
      return scan(quickScanFrom, quickScanTo);
    }

    public static List<Program> quickBotScan() {
      return quickBotScan(new int[0]);
    }

    public static List<Program> quickBotScan(int[] preferredPorts) {
      if (maxNumberOfVMs == 0)
        maxNumberOfVMs = isAndroid() ? maxNumberOfVMs_android : maxNumberOfVMs_nonAndroid;
      return scan(4999, 5000 + maxNumberOfVMs - 1, preferredPorts);
    }
  }

  public abstract static class DialogIO {

    public String line;

    public boolean eos, loud, noClose;

    public abstract String readLineImpl();

    public abstract boolean isStillConnected();

    public abstract void sendLine(String line);

    public abstract boolean isLocalConnection();

    public abstract Socket getSocket();

    public abstract void close();

    public int getPort() {
      Socket s = getSocket();
      return s == null ? 0 : s.getPort();
    }

    public boolean helloRead;

    public int shortenOutputTo = 500;

    public String readLineNoBlock() {
      String l = line;
      line = null;
      return l;
    }

    public boolean waitForLine() {
      try {
        if (line != null)
          return true;
        line = readLineImpl();
        if (line == null)
          eos = true;
        return line != null;
      } catch (Exception __e) {
        throw rethrow(__e);
      }
    }

    public String readLine() {
      waitForLine();
      helloRead = true;
      return readLineNoBlock();
    }

    public String ask(String s, Object... args) {
      if (loud)
        return askLoudly(s, args);
      if (!helloRead)
        readLine();
      if (args.length != 0)
        s = format3(s, args);
      sendLine(s);
      return readLine();
    }

    public String askLoudly(String s, Object... args) {
      if (!helloRead)
        readLine();
      if (args.length != 0)
        s = format3(s, args);
      print("> " + shorten(s, shortenOutputTo));
      sendLine(s);
      String answer = readLine();
      print("< " + shorten(answer, shortenOutputTo));
      return answer;
    }

    public void pushback(String l) {
      if (line != null)
        throw fail();
      line = l;
      helloRead = false;
    }
  }

  public abstract static class DialogHandler {

    public abstract void run(DialogIO io);
  }

  public static class Matches {

    public String[] m;

    public Matches() {
    }

    public Matches(String... m) {
      this.m = m;
    }

    public String get(int i) {
      return i < m.length ? m[i] : null;
    }

    public String unq(int i) {
      return unquote(get(i));
    }

    public String fsi(int i) {
      return formatSnippetID(unq(i));
    }

    public String fsi() {
      return fsi(0);
    }

    public String tlc(int i) {
      return unq(i).toLowerCase();
    }

    public boolean bool(int i) {
      return "true".equals(unq(i));
    }

    public String rest() {
      return m[m.length - 1];
    }

    public int psi(int i) {
      return Integer.parseInt(unq(i));
    }
  }

  public static class SingleThread {

    public boolean running;

    public void run(Object r) {
      go(r);
    }

    public synchronized boolean go(final Object runnable) {
      if (running)
        return false;
      running = true;
      {
        Thread _t_0 = new Thread("Single Thread") {

          public void run() {
            try {
              try {
                callF(runnable);
              } finally {
                _done();
              }
            } catch (Throwable __e) {
              printStackTrace2(__e);
            }
          }
        };
        startThread(_t_0);
      }
      return true;
    }

    public synchronized void _done() {
      running = false;
    }

    public boolean running() {
      return running;
    }
  }

  public static class Var<A> implements IVar<A> {

    public A v;

    public Var() {
    }

    public Var(A v) {
      this.v = v;
    }

    public synchronized void set(A a) {
      if (v != a) {
        v = a;
        notifyAll();
      }
    }

    public synchronized A get() {
      return v;
    }

    public synchronized boolean has() {
      return v != null;
    }

    public synchronized void clear() {
      v = null;
    }

    public String toString() {
      return str(get());
    }
  }

  public static class BaseBase {

    public String globalID = aGlobalID();

    public String text;

    public String textForRender() {
      return text;
    }
  }

  public static boolean traits_multiLine = true;

  public static class Base extends BaseBase {

    public List<String> traits = new ArrayList();

    public boolean hasTrait(String t) {
      return containsIC(traits(), t);
    }

    public List<String> traits() {
      if (nempty(text) && neq(first(traits), text))
        traits.add(0, text);
      return traits;
    }

    public void addTraits(List<String> l) {
      setAddAll(traits(), l);
    }

    public void addTrait(String t) {
      if (nempty(t))
        setAdd(traits(), t);
    }

    public String textForRender() {
      List<String> traits = traits();
      if (traits_multiLine)
        return lines(traits);
      if (l(traits) <= 1)
        return first(traits);
      return first(traits) + " [" + join(", ", dropFirst(traits)) + "]";
    }

    public void setText(String text) {
      this.text = text;
      traits = ll(text);
    }
  }

  public static class CirclesAndLines {

    public List<Circle> circles = new ArrayList();

    public List<Line> lines = new ArrayList();

    public Class<? extends Arrow> arrowClass = Arrow.class;

    public Class<? extends Circle> circleClass = Circle.class;

    public String title;

    public String globalID = aGlobalID();

    public long created = nowUnlessLoading();

    public transient Lock lock = fairLock();

    public transient String defaultImageID = "#1007372";

    public transient double imgZoom = 1;

    public transient Pt translate;

    public Circle hoverCircle;

    public transient Object onUserMadeArrow, onUserMadeCircle, onLayoutChange;

    public transient Object onFullLayoutChange, onDeleteCircle, onDeleteLine;

    public transient Object onRenameCircle, onRenameLine, onStructureChange;

    public transient BufferedImage imageForUserMadeNodes;

    public static int maxDistanceToLine = 20;

    public transient String backgroundImageID = defaultBackgroundImageID;

    public static String defaultBackgroundImageID = "#1007195";

    public static Color defaultLineColor = Color.white;

    public static boolean debugRender;

    public static Object staticPopupExtender;

    public transient double scale = 1;

    public transient boolean recordHistory = true;

    public List history;

    public Circle circle_autoVis(String text, String visualizationText, double x, double y) {
      return addAndReturn(circles, nu(circleClass, "x", x, "y", y, "text", text, "quickvis", visualizationText, "img", processImage(quickVisualizeOr(visualizationText, defaultImageID))));
    }

    public String makeVisualizationText(String text) {
      return possibleGlobalID(text) ? "" : text;
    }

    public Circle circle_autoVis(String text, double x, double y) {
      return circle_autoVis(text, makeVisualizationText(text), x, y);
    }

    public Circle circle(BufferedImage img, double x, double y, String text) {
      return addAndReturn(circles, nu(circleClass, "x", x, "y", y, "text", text, "img", processImage(img)));
    }

    public Circle circle(String text, BufferedImage img, double x, double y) {
      return circle(img, x, y, text);
    }

    public Circle circle(String text, double x, double y) {
      return addAndReturn(circles, nu(circleClass, "x", x, "y", y, "text", text, "img", processImage(imageForUserMadeNodes())));
    }

    public Circle addCircle(String imageID, double x, double y) {
      return addCircle(imageID, x, y, "");
    }

    public Circle addCircle(String imageID, double x, double y, String text) {
      return addAndReturn(circles, nu(circleClass, "x", x, "y", y, "text", text, "img", processImage(loadImage2(imageID))));
    }

    public Arrow findArrow(Circle a, Circle b) {
      for (Line l : getWhere(lines, "a", a, "b", b)) if (l instanceof Arrow)
        return (Arrow) l;
      return null;
    }

    public Line addLine(Circle a, Circle b) {
      Line line = findWhere(lines, "a", a, "b", b);
      if (line == null)
        lines.add(line = nu(Line.class, "a", a, "b", b));
      return line;
    }

    public Arrow arrow(Circle a, String text, Circle b) {
      return addArrow(a, b, text);
    }

    public Arrow addArrow(Circle a, Circle b) {
      return addArrow(a, b, "");
    }

    public Arrow addArrow(Circle a, Circle b, String text) {
      return addAndReturn(lines, nu(arrowClass, "a", a, "b", b, "text", text));
    }

    public BufferedImage makeImage(int w, int h) {
      BufferedImage bg = renderTiledBackground(backgroundImageID, w, h, ptX(translate), ptY(translate));
      if (!lock.tryLock())
        return null;
      try {
        if (scale != 1)
          createGraphics_modulate(bg, new VF1<Graphics2D>() {

            public void get(Graphics2D g) {
              try {
                g.scale(scale, scale);
              } catch (Exception __e) {
                throw rethrow(__e);
              }
            }

            public String toString() {
              return "g.scale(scale, scale);";
            }
          });
        if (debugRender)
          print("Have " + n(lines, "line"));
        HashMap<Pair<Circle, Circle>, Line> hasLine = new HashMap();
        HashMap<Line, Boolean> flipMap = new HashMap();
        for (Line l : lines) {
          hasLine.put(pair(l.a, l.b), l);
          Line x = hasLine.get(pair(l.b, l.a));
          if (x != null) {
            if (debugRender)
              print("flipMap " + l.a.text + " / " + l.b.text);
            flipMap.put(x, false);
            flipMap.put(l, false);
          }
        }
        for (Line l : lines) {
          DoublePt a = translateDoublePt(translate, l.a.doublePt(w, h, this));
          DoublePt b = translateDoublePt(translate, l.b.doublePt(w, h, this));
          if (debugRender)
            print("Line " + a + " " + b);
          if (l instanceof Arrow)
            drawThoughtArrow(bg, l.a.img(this), iround(a.x), iround(a.y), l.b.img(this), iround(b.x), iround(b.y), l.color);
          else
            drawThoughtLine(bg, l.a.img(this), iround(a.x), iround(a.y), l.b.img(this), iround(b.x), iround(b.y), l.color);
          String text = l.textForRender();
          if (nempty(text)) {
            drawOutlineTextAlongLine_flip.set(flipMap.get(l));
            drawThoughtLineText_multiLine(bg, l.a.img(this), iround(a.x), iround(a.y), l.b.img(this), iround(b.x), iround(b.y), text, Color.white);
          }
        }
        for (Circle c : circles) {
          DoublePt p = translateDoublePt(translate, c.doublePt(w, h, this));
          drawThoughtCircle(bg, c.img(this), p.x, p.y);
        }
        for (Circle c : circles) {
          DoublePt p = translateDoublePt(translate, c.doublePt(w, h, this));
          String text = c.textForRender();
          if (nempty(text))
            drawThoughtCircleText(bg, c.img(this), p, text);
          if (c == hoverCircle)
            drawThoughtCirclePlus(bg, c.img(this), p.x, p.y);
        }
      } finally {
        lock.unlock();
        createGraphics_modulate(bg, null);
      }
      return bg;
    }

    public Canvas showAsFrame(int w, int h) {
      Canvas canvas = showAsFrame();
      frameInnerSize(canvas, w, h);
      centerFrame(getFrame(canvas));
      return canvas;
    }

    public Canvas showAsFrame() {
      return (Canvas) swing(new F0<Object>() {

        public Object get() {
          try {
            Canvas canvas = makeCanvas();
            showCenterFrame(canvas);
            return canvas;
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "Canvas canvas = makeCanvas();\r\n      showCenterFrame(canvas);\r\n      ret canvas;";
        }
      });
    }

    public Canvas makeCanvas() {
      final Object makeImg = new Object() {

        public Object get(Integer w, Integer h) {
          try {
            return makeImage(w, h);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "makeImage(w, h)";
        }
      };
      final Canvas canvas = jcanvas(makeImg);
      disableImageSurfaceSelector(canvas);
      new CircleDragger(this, canvas, new Runnable() {

        public void run() {
          try {
            updateCanvas(canvas, makeImg);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "updateCanvas(canvas, makeImg)";
        }
      });
      componentPopupMenu(canvas, new VF1<JPopupMenu>() {

        public void get(JPopupMenu menu) {
          try {
            Pt p = pointFromEvent(canvas, componentPopupMenu_mouseEvent.get());
            JMenu imageMenu = jmenu("Image");
            moveAllMenuItems(menu, imageMenu);
            addMenuItem(menu, "New Circle...", new Runnable() {

              public void run() {
                try {
                  newCircle(canvas);
                } catch (Exception __e) {
                  throw rethrow(__e);
                }
              }

              public String toString() {
                return "newCircle(canvas)";
              }
            });
            final Line l = findLine(canvas, p);
            if (l != null) {
              addMenuItem(menu, "Rename Relation...", new Runnable() {

                public void run() {
                  try {
                    renameLine(canvas, l);
                  } catch (Exception __e) {
                    throw rethrow(__e);
                  }
                }

                public String toString() {
                  return "renameLine(canvas, l)";
                }
              });
              addMenuItem(menu, "Delete Relation", new Runnable() {

                public void run() {
                  try {
                    deleteLine(l);
                    canvas.update();
                  } catch (Exception __e) {
                    throw rethrow(__e);
                  }
                }

                public String toString() {
                  return "deleteLine(l);\r\n          canvas.update();";
                }
              });
            }
            final Circle c = findCircle(canvas, p);
            if (c != null) {
              addMenuItem(menu, "Rename Circle...", new Runnable() {

                public void run() {
                  try {
                    renameCircle(canvas, c);
                  } catch (Exception __e) {
                    throw rethrow(__e);
                  }
                }

                public String toString() {
                  return "renameCircle(canvas, c)";
                }
              });
              addMenuItem(menu, "Delete Circle", new Runnable() {

                public void run() {
                  try {
                    deleteCircle(c);
                    canvas.update();
                  } catch (Exception __e) {
                    throw rethrow(__e);
                  }
                }

                public String toString() {
                  return "deleteCircle(c);\r\n          canvas.update();";
                }
              });
              if (c.img != null || c.quickvis != null)
                addMenuItem(menu, "Delete Image", new Runnable() {

                  public void run() {
                    try {
                      c.img = null;
                      c.quickvis = null;
                      canvas.update();
                    } catch (Exception __e) {
                      throw rethrow(__e);
                    }
                  }

                  public String toString() {
                    return "c.img = null;\r\n            c.quickvis = null;\r\n            canvas.update();";
                  }
                });
              if (neqic(c.text, c.quickvis))
                addMenuItem(menu, "Visualize", new Runnable() {

                  public void run() {
                    try {
                      {
                        Thread _t_0 = new Thread("Visualizing") {

                          public void run() {
                            try {
                              quickVisualize(c.text);
                              print("Quickvis done");
                              {
                                swing(new Runnable() {

                                  public void run() {
                                    try {
                                      c.img = null;
                                      c.quickvis = c.text;
                                      canvas.update();
                                      pcallF(onRenameCircle, c);
                                      schange();
                                    } catch (Exception __e) {
                                      throw rethrow(__e);
                                    }
                                  }

                                  public String toString() {
                                    return "c.img = null;\r\n                c.quickvis = c.text;\r\n                canvas.u...";
                                  }
                                });
                              }
                            } catch (Throwable __e) {
                              printStackTrace2(__e);
                            }
                          }
                        };
                        startThread(_t_0);
                      }
                    } catch (Exception __e) {
                      throw rethrow(__e);
                    }
                  }

                  public String toString() {
                    return "thread \"Visualizing\" {\r\n              quickVisualize(c.text);\r\n              ...";
                  }
                });
            }
            addMenuItem(menu, "Copy structure to clipboard", new Runnable() {

              public void run() {
                try {
                  copyTextToClipboard(cal_simplifiedStructure(CirclesAndLines.this));
                } catch (Exception __e) {
                  throw rethrow(__e);
                }
              }

              public String toString() {
                return "copyTextToClipboard(cal_simplifiedStructure(CirclesAndLines.this))";
              }
            });
            addMenuItem(menu, "Paste structure", new Runnable() {

              public void run() {
                try {
                  String text = getTextFromClipboard();
                  if (nempty(text)) {
                    copyCAL(cal_unstructure(text), CirclesAndLines.this);
                    canvas.update();
                    schange();
                  }
                } catch (Exception __e) {
                  throw rethrow(__e);
                }
              }

              public String toString() {
                return "String text = getTextFromClipboard();\r\n        if (nempty(text)) {\r\n         ...";
              }
            });
            pcallF(staticPopupExtender, CirclesAndLines.this, canvas, menu);
            addMenuItem(menu, imageMenu);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "// POPUP MENU START\r\n      Pt p = pointFromEvent(canvas, componentPopupMenu_m...";
        }
      });
      return canvas;
    }

    public void newCircle(final Canvas canvas) {
      final JTextField text = jtextfield();
      showFormTitled("New Circle", "Text", text, runnableThread(new Runnable() {

        public void run() {
          try {
            {
              JWindow _loading_window = showLoadingAnimation();
              try {
                String theText = getTextTrim(text);
                makeCircle(theText);
                canvas.update();
              } finally {
                disposeWindow(_loading_window);
              }
            }
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "{ JWindow _loading_window = showLoadingAnimation(); try {\r\n      String theTe...";
        }
      }));
    }

    public Canvas show() {
      return showAsFrame();
    }

    public Canvas show(int w, int h) {
      return showAsFrame(w, h);
    }

    public Circle findCircle(String text) {
      for (Circle c : circles) if (eq(c.text, text))
        return c;
      for (Circle c : circles) if (eqic(c.text, text))
        return c;
      return null;
    }

    public void renameCircle(final Canvas canvas, final Circle c) {
      final JTextField tf = jtextfield(c.text);
      showFormTitled("Rename circle", "Old name", jlabel(c.text), "New name", tf, new Runnable() {

        public void run() {
          try {
            c.setText(getTextTrim(tf));
            canvas.update();
            pcallF(onRenameCircle, c);
            schange();
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "c.setText(getTextTrim(tf));\r\n        canvas.update();\r\n        pcallF(onRenam...";
        }
      });
    }

    public void renameLine(final Canvas canvas, final Line l) {
      final JTextField tf = jtextfield(l.text);
      showFormTitled("Rename relation", "Old name", jlabel(l.text), "New name", tf, new Runnable() {

        public void run() {
          try {
            l.setText(getTextTrim(tf));
            canvas.update();
            pcallF(onRenameLine, l);
            schange();
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "l.setText(getTextTrim(tf));\r\n        canvas.update();\r\n        pcallF(onRenam...";
        }
      });
    }

    public void clear() {
      clearAll(circles, lines);
    }

    public Circle findCircle(ImageSurface canvas, Pt p) {
      p = untranslatePt(translate, p);
      Lowest<Circle> best = new Lowest();
      for (Circle c : circles) if (c.contains(this, canvas, p))
        best.put(c, pointDistance(p, c.pt2(this, canvas)));
      return best.get();
    }

    public Circle findNearestCircle(ImageSurface canvas, Pt p) {
      Lowest<Circle> best = new Lowest();
      for (Circle c : circles) if (c.contains(this, canvas, p))
        best.put(c, pointDistance(p, c.pt2(this, canvas)));
      return best.get();
    }

    public BufferedImage processImage(BufferedImage img) {
      return scaleImage(img, imgZoom);
    }

    public void deleteCircle(Circle c) {
      for (Line l : cloneList(lines)) if (l.a == c || l.b == c)
        deleteLine(l);
      circles.remove(c);
      pcallF(onDeleteCircle, c);
      schange();
    }

    public void deleteLine(Line l) {
      lines.remove(l);
      pcallF(onDeleteLine, l);
      schange();
    }

    public void openPlusDialog(final Circle c, final ImageSurface canvas) {
      if (c == null)
        return;
      final JTextField tfFrom = jtextfield(c.text);
      final JTextField tfRel = jtextfield(web_defaultRelationName());
      final JComboBox tfTo = autoComboBox(collect(circles, "text"));
      showFormTitled("Add connection", "From node", tfFrom, "Connection name", tfRel, "To node", tfTo, new F0<Object>() {

        public Object get() {
          try {
            String sA = getTextTrim(tfFrom);
            Circle a = eq(sA, c.text) ? c : findOrMakeCircle(sA);
            if (a == null) {
              messageBox("Not found: " + getTextTrim(tfFrom));
              return false;
            }
            Circle b = findOrMakeCircle(getTextTrim(tfTo));
            if (b == null) {
              messageBox("Not found: " + getTextTrim(tfTo));
              return false;
            }
            if (a == b) {
              infoBox("Can't connect circle to itself for now");
              return false;
            }
            Arrow arrow = arrow(a, getTextTrim(tfRel), b);
            ((Canvas) canvas).update();
            pcallF(onUserMadeArrow, arrow);
            schange();
            return null;
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "S sA = getTextTrim(tfFrom);\r\n        Circle a = eq(sA, c.text) ? c : findOrMa...";
        }
      });
      awtLater(tfRel, 100, new Runnable() {

        public void run() {
          try {
            requestFocus(tfRel);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "requestFocus(tfRel)";
        }
      });
    }

    public void schange() {
      pcallF(onStructureChange);
      logQuoted("user-web-edits", now() + " " + cal_structure(this));
      markWebsPosted();
    }

    public Circle findOrMakeCircle(String text) {
      Circle c = findCircle(text);
      if (c != null)
        return c;
      return makeCircle(text);
    }

    public Circle makeCircle(String text) {
      Circle c = circle(imageForUserMadeNodes(), random(0.1, 0.9), random(0.1, 0.9), text);
      pcallF(onUserMadeCircle, c);
      schange();
      historyLog(lisp("Made circle", text));
      return c;
    }

    public BufferedImage imageForUserMadeNodes() {
      if (imageForUserMadeNodes == null)
        imageForUserMadeNodes = whiteImage(20, 20);
      return imageForUserMadeNodes;
    }

    public Line findLine(Canvas is, Pt p) {
      p = untranslatePt(translate, p);
      Lowest<Line> best = new Lowest();
      for (Line line : lines) {
        double d = distancePointToLineSegment(line.a.pt(this, is), line.b.pt(this, is), p);
        if (d <= maxDistanceToLine)
          best.put(line, d);
      }
      return best.get();
    }

    public Pt pointFromEvent(ImageSurface canvas, MouseEvent e) {
      return scalePt(canvas.pointFromEvent(e), 1 / scale);
    }

    public void historyLog(Object o) {
      if (!recordHistory)
        return;
      if (history == null)
        history = new ArrayList();
      history.add(o);
    }
  }

  public static class Circle extends Base {

    public transient BufferedImage img;

    public double x, y;

    public String quickvis;

    public BufferedImage img(CirclesAndLines cal) {
      if (img != null)
        return img;
      if (nempty(quickvis))
        img = quickVisualize(quickvis);
      return cal.imageForUserMadeNodes();
    }

    public Pt pt2(CirclesAndLines cal, ImageSurface is) {
      return pt2(is.getWidth(), is.getHeight(), cal);
    }

    public Pt pt(CirclesAndLines cal, ImageSurface is) {
      return pt(is.getWidth(), is.getHeight(), cal);
    }

    public Pt pt(int w, int h, CirclesAndLines cal) {
      return new Pt(iround(x * w / cal.scale), iround(y * h / cal.scale));
    }

    public Pt pt2(int w, int h, CirclesAndLines cal) {
      return new Pt(iround(x * w), iround(y * h));
    }

    public DoublePt doublePt(int w, int h, CirclesAndLines cal) {
      return new DoublePt(x * w / cal.scale, y * h / cal.scale);
    }

    public boolean contains(CirclesAndLines cal, ImageSurface is, Pt p) {
      return pointDistance(p, pt2(cal, is)) <= iround(thoughtCircleSize(img(cal)) * cal.scale) / 2 + 1;
    }
  }

  public static class Line extends Base {

    public Circle a, b;

    public transient Color color = CirclesAndLines.defaultLineColor;

    public Line setColor(Color color) {
      this.color = color;
      return this;
    }
  }

  public static class Arrow extends Line {
  }

  public static class CircleDragger extends MouseAdapter {

    public CirclesAndLines cal;

    public ImageSurface is;

    public Object update;

    public int dx, dy;

    public Circle circle;

    public Pt startPoint;

    public CircleDragger(CirclesAndLines cal, ImageSurface is, Object update) {
      this.update = update;
      this.is = is;
      this.cal = cal;
      if (containsInstance(is.tools, CircleDragger.class))
        return;
      is.tools.add(this);
      is.addMouseListener(this);
      is.addMouseMotionListener(this);
    }

    public void mouseMoved(MouseEvent e) {
      Pt p = is.pointFromEvent(e);
      Circle c = cal.findCircle(is, p);
      if (c != cal.hoverCircle) {
        cal.hoverCircle = c;
        callF(update);
      }
    }

    public void mousePressed(MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON1) {
        Pt p = is.pointFromEvent(e);
        startPoint = p;
        circle = cal.findCircle(is, p);
        if (circle != null) {
          dx = p.x - iround(circle.x * is.getWidth());
          dy = p.y - iround(circle.y * is.getHeight());
        } else {
          Pt t = unnull(cal.translate);
          dx = p.x - t.x;
          dy = p.y - t.y;
        }
      }
    }

    public void mouseDragged(MouseEvent e) {
      if (startPoint == null)
        return;
      Pt p = is.pointFromEvent(e);
      if (circle != null) {
        circle.x = (p.x - dx) / (double) is.getWidth();
        circle.y = (p.y - dy) / (double) is.getHeight();
        pcallF(cal.onLayoutChange, circle);
        callF(update);
      } else {
        cal.translate = new Pt(p.x - dx, p.y - dy);
        callF(update);
      }
    }

    public void mouseReleased(MouseEvent e) {
      mouseDragged(e);
      if (eq(is.pointFromEvent(e), startPoint))
        cal.openPlusDialog(circle, is);
      circle = null;
      startPoint = null;
    }
  }

  public abstract static class TokCondition {

    public abstract boolean get(List<String> tok, int i);
  }

  public abstract static class F0<A> {

    public abstract A get();
  }

  public abstract static class F1<A, B> {

    public abstract B get(A a);
  }

  public abstract static class IterableIterator<A> implements Iterator<A>, Iterable<A> {

    public Iterator<A> iterator() {
      return this;
    }

    public void remove() {
      unsupportedOperation();
    }
  }

  public static class Flag {

    public boolean up;

    public synchronized boolean raise() {
      if (!up) {
        up = true;
        notifyAll();
        return true;
      } else
        return false;
    }

    public synchronized void waitUntilUp() {
      while (!up) {
        try {
          wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    public synchronized void waitUntilUp(long timeout) {
      if (!up) {
        try {
          wait(timeout);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    public synchronized boolean isUp() {
      return up;
    }

    public String toString() {
      return isUp() ? "up" : "down";
    }

    public void waitForThisOr(Flag otherFlag) {
      try {
        while (!isUp() && !otherFlag.isUp()) Thread.sleep(50);
      } catch (Exception __e) {
        throw rethrow(__e);
      }
    }
  }

  public static class BetterLabel extends JLabel {

    public boolean autoToolTip = true;

    public BetterLabel() {
      componentPopupMenu(this, new VF1<JPopupMenu>() {

        public void get(JPopupMenu menu) {
          try {
            addMenuItem(menu, "Copy text to clipboard", new Runnable() {

              public void run() {
                try {
                  copyTextToClipboard(getText());
                } catch (Exception __e) {
                  throw rethrow(__e);
                }
              }

              public String toString() {
                return "copyTextToClipboard(getText());";
              }
            });
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "addMenuItem(menu, \"Copy text to clipboard\", r {\r\n        copyTextToClipboard(...";
        }
      });
    }

    public BetterLabel(String text) {
      this();
      setText(text);
    }

    public void setText(String text) {
      super.setText(text);
      if (autoToolTip)
        setToolTipText(text);
    }
  }

  public static class MRUCache<A, B> extends LinkedHashMap<A, B> {

    public int maxSize = 10;

    public MRUCache() {
    }

    public MRUCache(int maxSize) {
      this.maxSize = maxSize;
    }

    public boolean removeEldestEntry(Map.Entry eldest) {
      return size() > maxSize;
    }
  }

  public static class TransferableImage implements Transferable {

    public Image i;

    public TransferableImage(Image i) {
      this.i = i;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if (flavor.equals(DataFlavor.imageFlavor) && i != null) {
        return i;
      } else {
        throw new UnsupportedFlavorException(flavor);
      }
    }

    public DataFlavor[] getTransferDataFlavors() {
      DataFlavor[] flavors = new DataFlavor[1];
      flavors[0] = DataFlavor.imageFlavor;
      return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
      DataFlavor[] flavors = getTransferDataFlavors();
      for (int i = 0; i < flavors.length; i++) {
        if (flavor.equals(flavors[i])) {
          return true;
        }
      }
      return false;
    }
  }

  public static interface Producer<A> {

    public A next();
  }

  public static ThreadLocal<Boolean> DynamicObject_loading = new ThreadLocal();

  public static class DynamicObject {

    public String className;

    public LinkedHashMap<String, Object> fieldValues = new LinkedHashMap();

    public DynamicObject() {
    }

    public DynamicObject(String className) {
      this.className = className;
    }
  }

  public static class RGBImage implements MakesBufferedImage {

    public transient BufferedImage bufferedImage;

    public File file;

    public int width, height;

    public int[] pixels;

    public RGBImage() {
    }

    public RGBImage(BufferedImage image) {
      this(image, null);
    }

    public RGBImage(BufferedImage image, File file) {
      this.file = file;
      bufferedImage = image;
      width = image.getWidth();
      height = image.getHeight();
      pixels = new int[width * height];
      PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
      try {
        if (!pixelGrabber.grabPixels())
          throw new RuntimeException("Could not grab pixels");
        cleanPixels();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    public RGBImage(String file) throws IOException {
      this(new File(file));
    }

    public RGBImage(Dimension size, Color color) {
      this(size.width, size.height, color);
    }

    public RGBImage(Dimension size, RGB color) {
      this(size.width, size.height, color);
    }

    public final void cleanPixels() {
      for (int i = 0; i < pixels.length; i++) pixels[i] &= 0xFFFFFF;
    }

    public RGBImage(int width, int height, int[] pixels) {
      this.width = width;
      this.height = height;
      this.pixels = pixels;
    }

    public RGBImage(int w, int h, RGB[] pixels) {
      this.width = w;
      this.height = h;
      this.pixels = asInts(pixels);
    }

    public static int[] asInts(RGB[] pixels) {
      int[] ints = new int[pixels.length];
      for (int i = 0; i < pixels.length; i++) ints[i] = pixels[i] == null ? 0 : pixels[i].getColor().getRGB();
      return ints;
    }

    public RGBImage(int w, int h) {
      this(w, h, Color.black);
    }

    public RGBImage(int w, int h, RGB rgb) {
      this.width = w;
      this.height = h;
      this.pixels = new int[w * h];
      int col = rgb.asInt();
      if (col != 0)
        for (int i = 0; i < pixels.length; i++) pixels[i] = col;
    }

    public RGBImage(RGBImage image) {
      this(image.width, image.height, copyPixels(image.pixels));
    }

    public RGBImage(int width, int height, Color color) {
      this(width, height, new RGB(color));
    }

    public RGBImage(File file) throws IOException {
      this(javax.imageio.ImageIO.read(file));
    }

    public static int[] copyPixels(int[] pixels) {
      int[] copy = new int[pixels.length];
      System.arraycopy(pixels, 0, copy, 0, pixels.length);
      return copy;
    }

    public int getIntPixel(int x, int y) {
      if (inRange(x, y))
        return pixels[y * width + x];
      else
        return 0xFFFFFF;
    }

    public static RGB asRGB(int packed) {
      int r = (packed >> 16) & 0xFF;
      int g = (packed >> 8) & 0xFF;
      int b = packed & 0xFF;
      return new RGB(r / 255f, g / 255f, b / 255f);
    }

    public RGB getRGB(int x, int y) {
      if (inRange(x, y))
        return asRGB(pixels[y * width + x]);
      else
        return new RGB(0xFFFFFF);
    }

    public RGB getPixel(int x, int y) {
      return getRGB(x, y);
    }

    public RGB getPixel(Pt p) {
      return getPixel(p.x, p.y);
    }

    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }

    public int w() {
      return width;
    }

    public int h() {
      return height;
    }

    public BufferedImage getBufferedImage() {
      if (bufferedImage == null) {
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) bufferedImage.setRGB(x, y, pixels[y * width + x]);
      }
      return bufferedImage;
    }

    public RGBImage clip(Rect r) {
      return r == null ? null : clip(r.getRectangle());
    }

    public RGBImage clip(Rectangle r) {
      r = fixClipRect(r);
      int[] newPixels;
      try {
        newPixels = new int[r.width * r.height];
      } catch (RuntimeException e) {
        System.out.println(r);
        throw e;
      }
      for (int y = 0; y < r.height; y++) {
        System.arraycopy(pixels, (y + r.y) * width + r.x, newPixels, y * r.width, r.width);
      }
      return new RGBImage(r.width, r.height, newPixels);
    }

    public final Rectangle fixClipRect(Rectangle r) {
      r = r.intersection(new Rectangle(0, 0, width, height));
      if (r.isEmpty())
        r = new Rectangle(r.x, r.y, 0, 0);
      return r;
    }

    public File getFile() {
      return file;
    }

    public static RGBImage load(String fileName) {
      return load(new File(fileName));
    }

    public static RGBImage load(File file) {
      try {
        BufferedImage bufferedImage = javax.imageio.ImageIO.read(file);
        return new RGBImage(bufferedImage);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public int getInt(int x, int y) {
      return pixels[y * width + x];
    }

    public void save(File file) throws IOException {
      String name = file.getName().toLowerCase();
      String type;
      if (name.endsWith(".png"))
        type = "png";
      else if (name.endsWith(".jpg") || name.endsWith(".jpeg"))
        type = "jpeg";
      else
        throw new IOException("Unknown image extension: " + name);
      javax.imageio.ImageIO.write(getBufferedImage(), type, file);
    }

    public static RGBImage dummyImage() {
      return new RGBImage(1, 1, new int[] { 0xFFFFFF });
    }

    public int[] getPixels() {
      return pixels;
    }

    public void setPixel(int x, int y, RGB rgb) {
      if (x >= 0 && y >= 0 && x < width && y < height)
        pixels[y * width + x] = rgb.asInt();
    }

    public void setPixel(int x, int y, Color color) {
      setPixel(x, y, new RGB(color));
    }

    public void setPixel(int x, int y, int rgb) {
      if (x >= 0 && y >= 0 && x < width && y < height)
        pixels[y * width + x] = rgb;
    }

    public void setPixel(Pt p, RGB rgb) {
      setPixel(p.x, p.y, rgb);
    }

    public void setPixel(Pt p, Color color) {
      setPixel(p.x, p.y, color);
    }

    public RGBImage copy() {
      return new RGBImage(this);
    }

    public boolean inRange(int x, int y) {
      return x >= 0 && y >= 0 && x < width && y < height;
    }

    public Dimension getSize() {
      return new Dimension(width, height);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      RGBImage rgbImage = (RGBImage) o;
      if (height != rgbImage.height)
        return false;
      if (width != rgbImage.width)
        return false;
      if (!Arrays.equals(pixels, rgbImage.pixels))
        return false;
      return true;
    }

    @Override
    public int hashCode() {
      int result = width;
      result = 31 * result + height;
      result = 31 * result + Arrays.hashCode(pixels);
      return result;
    }

    public String getHex(int x, int y) {
      return getPixel(x, y).getHexString();
    }

    public RGBImage clip(int x, int y, int width, int height) {
      return clip(new Rectangle(x, y, width, height));
    }

    public RGBImage clipLine(int y) {
      return clip(0, y, width, 1);
    }

    public int numPixels() {
      return width * height;
    }
  }

  public static class Pair<A, B> implements Comparable<Pair<A, B>> {

    public A a;

    public B b;

    public Pair() {
    }

    public Pair(A a, B b) {
      this.b = b;
      this.a = a;
    }

    public int hashCode() {
      return hashCodeFor(a) + 2 * hashCodeFor(b);
    }

    public boolean equals(Object o) {
      if (o == this)
        return true;
      if (!(o instanceof Pair))
        return false;
      Pair t = (Pair) o;
      return eq(a, t.a) && eq(b, t.b);
    }

    public String toString() {
      return "<" + a + ", " + b + ">";
    }

    public int compareTo(Pair<A, B> p) {
      if (p == null)
        return 1;
      int i = ((Comparable<A>) a).compareTo(p.a);
      if (i != 0)
        return i;
      return ((Comparable<B>) b).compareTo(p.b);
    }
  }

  public static class Rect {

    public int x, y, w, h;

    public Rect() {
    }

    public Rect(Rectangle r) {
      x = r.x;
      y = r.y;
      w = r.width;
      h = r.height;
    }

    public Rect(int x, int y, int w, int h) {
      this.h = h;
      this.w = w;
      this.y = y;
      this.x = x;
    }

    public Rect(Pt p, int w, int h) {
      this.h = h;
      this.w = w;
      x = p.x;
      y = p.y;
    }

    public Rectangle getRectangle() {
      return new Rectangle(x, y, w, h);
    }

    public boolean equals(Object o) {
      return stdEq2(this, o);
    }

    public int hashCode() {
      return stdHash2(this);
    }

    public String toString() {
      return x + "," + y + " / " + w + "," + h;
    }

    public int x2() {
      return x + w;
    }

    public int y2() {
      return y + h;
    }

    public boolean contains(Pt p) {
      return contains(p.x, p.y);
    }

    public boolean contains(int _x, int _y) {
      return _x >= x && _y >= y && _x < x + w && _y < y + h;
    }

    public boolean empty() {
      return w <= 0 || h <= 0;
    }
  }

  public static class Pt {

    public int x, y;

    public Pt() {
    }

    public Pt(Point p) {
      x = p.x;
      y = p.y;
    }

    public Pt(int x, int y) {
      this.y = y;
      this.x = x;
    }

    public Point getPoint() {
      return new Point(x, y);
    }

    public boolean equals(Object o) {
      return stdEq2(this, o);
    }

    public int hashCode() {
      return stdHash2(this);
    }

    public String toString() {
      return x + ", " + y;
    }
  }

  public static class Lowest<A> {

    public A best;

    public double score;

    public transient Object onChange;

    public boolean isNewBest(double score) {
      return best == null || score < this.score;
    }

    public double bestScore() {
      return best == null ? Double.NaN : score;
    }

    public float floatScore() {
      return best == null ? Float.NaN : (float) score;
    }

    public float floatScoreOr(float defaultValue) {
      return best == null ? defaultValue : (float) score;
    }

    public boolean put(A a, double score) {
      if (a != null && isNewBest(score)) {
        best = a;
        this.score = score;
        pcallF(onChange);
        return true;
      }
      return false;
    }

    public A get() {
      return best;
    }

    public boolean has() {
      return best != null;
    }
  }

  public static class DoublePt {

    public double x, y;

    public DoublePt() {
    }

    public DoublePt(Point p) {
      x = p.x;
      y = p.y;
    }

    public DoublePt(double x, double y) {
      this.y = y;
      this.x = x;
    }

    public boolean equals(Object o) {
      return stdEq2(this, o);
    }

    public int hashCode() {
      return stdHash2(this);
    }

    public String toString() {
      return x + ", " + y;
    }
  }

  public static interface IVar<A> {

    public void set(A a);

    public A get();

    public boolean has();

    public void clear();
  }

  public static class ImageSurface extends Surface {

    public BufferedImage image;

    public double zoomX = 1, zoomY = 1, zoomFactor = 1.5;

    public Rectangle selection;

    public List tools = new ArrayList();

    public Object overlay;

    public Runnable onSelectionChange;

    public static boolean verbose;

    public boolean noMinimumSize = true;

    public String titleForUpload;

    public Object onZoom;

    public boolean specialPurposed;

    public boolean zoomable = true;

    public ImageSurface() {
      this(dummyImage());
    }

    public static BufferedImage dummyImage() {
      return new RGBImage(1, 1, new int[] { 0xFFFFFF }).getBufferedImage();
    }

    public ImageSurface(RGBImage image) {
      this(image != null ? image.getBufferedImage() : dummyImage());
    }

    public ImageSurface(BufferedImage image) {
      setImage(image);
      clearSurface = false;
      componentPopupMenu(this, new VF1<JPopupMenu>() {

        public void get(JPopupMenu menu) {
          try {
            Point p = pointFromEvent(componentPopupMenu_mouseEvent.get()).getPoint();
            fillPopupMenu(menu, p);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "Point p = pointFromEvent(componentPopupMenu_mouseEvent.get()).getPoint();\r\n  ...";
        }
      });
      new ImageSurfaceSelector(this);
      jHandleFileDrop(this, new VF1<File>() {

        public void get(File f) {
          try {
            setImage(loadBufferedImage(f));
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "setImage(loadBufferedImage(f))";
        }
      });
    }

    public ImageSurface(RGBImage image, double zoom) {
      this(image);
      setZoom(zoom);
    }

    public void fillPopupMenu(JPopupMenu menu, final Point point) {
      if (zoomable) {
        JMenuItem miZoomReset = new JMenuItem("Zoom 100%");
        miZoomReset.addActionListener(new ActionListener() {

          public void actionPerformed(ActionEvent evt) {
            setZoom(1.0);
            centerPoint(point);
          }
        });
        menu.add(miZoomReset);
        JMenuItem miZoomIn = new JMenuItem("Zoom in");
        miZoomIn.addActionListener(new ActionListener() {

          public void actionPerformed(ActionEvent evt) {
            zoomIn(zoomFactor);
            centerPoint(point);
          }
        });
        menu.add(miZoomIn);
        JMenuItem miZoomOut = new JMenuItem("Zoom out");
        miZoomOut.addActionListener(new ActionListener() {

          public void actionPerformed(ActionEvent evt) {
            zoomOut(zoomFactor);
            centerPoint(point);
          }
        });
        menu.add(miZoomOut);
        JMenuItem miZoomToWindow = new JMenuItem("Zoom to window");
        miZoomToWindow.addActionListener(new ActionListener() {

          public void actionPerformed(ActionEvent evt) {
            zoomToDisplaySize();
          }
        });
        menu.add(miZoomToWindow);
        addMenuItem(menu, "Show full screen", new Runnable() {

          public void run() {
            try {
              showFullScreen();
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "showFullScreen()";
          }
        });
        addMenuItem(menu, "Point: " + point.x + "," + point.y + " (image: " + image.getWidth() + "*" + image.getHeight() + ")", null);
        menu.addSeparator();
      }
      addMenuItem(menu, "Save image...", new Runnable() {

        public void run() {
          try {
            saveImage();
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "saveImage()";
        }
      });
      addMenuItem(menu, "Upload image...", new Runnable() {

        public void run() {
          try {
            uploadTheImage();
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "uploadTheImage()";
        }
      });
      addMenuItem(menu, "Copy image to clipboard", new Runnable() {

        public void run() {
          try {
            copyImageToClipboard(getImage());
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "copyImageToClipboard(getImage())";
        }
      });
      if (!specialPurposed)
        addMenuItem(menu, "Paste image from clipboard", new Runnable() {

          public void run() {
            try {
              loadFromClipboard();
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "loadFromClipboard()";
          }
        });
      if (selection != null)
        addMenuItem(menu, "Crop", new Runnable() {

          public void run() {
            try {
              crop();
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "crop()";
          }
        });
      if (!specialPurposed)
        addMenuItem(menu, "No image", new Runnable() {

          public void run() {
            try {
              noImage();
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "noImage()";
          }
        });
    }

    public void noImage() {
      setImage((BufferedImage) null);
    }

    public void crop() {
      if (selection == null)
        return;
      BufferedImage img = clipBufferedImage(getImage(), selection);
      selection = null;
      setImage(img);
    }

    public void loadFromClipboard() {
      BufferedImage img = getImageFromClipboard();
      if (img != null)
        setImage(img);
    }

    public void saveImage() {
      RGBImage image = new RGBImage(getImage(), null);
      JFileChooser fileChooser = new JFileChooser(getProgramDir());
      if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        try {
          image.save(fileChooser.getSelectedFile());
        } catch (IOException e) {
          popup(e);
        }
      }
    }

    public void render(int w, int h, Graphics2D g) {
      if (verbose)
        main.print("render");
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g.setColor(Color.white);
      if (image == null)
        g.fillRect(0, 0, w, h);
      else {
        int iw = getZoomedWidth(), ih = getZoomedHeight();
        boolean alpha = hasTransparency(image);
        if (alpha)
          g.fillRect(0, 0, w, h);
        g.drawImage(image, 0, 0, iw, ih, null);
        if (!alpha) {
          g.fillRect(iw, 0, w - iw, h);
          g.fillRect(0, ih, iw, h - ih);
        }
      }
      if (verbose)
        main.print("render overlay");
      if (overlay != null)
        pcallF(overlay, g);
      if (verbose)
        main.print("render selection");
      if (selection != null) {
        drawSelectionRect(g, selection, Color.green, Color.white);
      }
    }

    public void drawSelectionRect(Graphics2D g, Rectangle selection, Color green, Color white) {
      drawSelectionRect(g, selection, green, white, zoomX, zoomY);
    }

    public void drawSelectionRect(Graphics2D g, Rectangle selection, Color green, Color white, double zoomX, double zoomY) {
      g.setColor(green);
      int top = (int) (selection.y * zoomY);
      int bottom = (int) ((selection.y + selection.height) * zoomY);
      int left = (int) (selection.x * zoomX);
      int right = (int) ((selection.x + selection.width) * zoomX);
      g.drawRect(left - 1, top - 1, right - left + 1, bottom - top + 1);
      g.setColor(white);
      g.drawRect(left - 2, top - 2, right - left + 3, bottom - top + 3);
    }

    public void setZoom(double zoom) {
      setZoom(zoom, zoom);
    }

    public void setZoom(double zoomX, double zoomY) {
      this.zoomX = zoomX;
      this.zoomY = zoomY;
      revalidate();
      repaint();
      centerPoint(new Point(getImage().getWidth() / 2, getImage().getHeight() / 2));
      pcallF(onZoom);
    }

    public Dimension getMinimumSize() {
      if (noMinimumSize)
        return new Dimension(1, 1);
      int w = getZoomedWidth();
      int h = getZoomedHeight();
      Dimension min = super.getMinimumSize();
      return new Dimension(Math.max(w, min.width), Math.max(h, min.height));
    }

    public final int getZoomedHeight() {
      return (int) (image.getHeight() * zoomY);
    }

    public final int getZoomedWidth() {
      return (int) (image.getWidth() * zoomX);
    }

    public void setImage(RGBImage image) {
      setImage(image.getBufferedImage());
    }

    public void setImage(BufferedImage image) {
      this.image = image != null ? image : dummyImage();
      revalidate();
      repaint();
    }

    public BufferedImage getImage() {
      return image;
    }

    public double getZoomX() {
      return zoomX;
    }

    public double getZoomY() {
      return zoomY;
    }

    public Dimension getPreferredSize() {
      return new Dimension(getZoomedWidth(), getZoomedHeight());
    }

    public JScrollPane makeScrollPane() {
      JScrollPane scrollPane = new JScrollPane(this);
      scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
      return scrollPane;
    }

    public void zoomToWindow() {
      zoomToDisplaySize();
    }

    public void zoomToDisplaySize() {
      if (image == null)
        return;
      Dimension display = getDisplaySize();
      double xRatio = display.width / (double) image.getWidth();
      double yRatio = display.height / (double) image.getHeight();
      setZoom(Math.min(xRatio, yRatio));
      revalidate();
    }

    public final Dimension getDisplaySize() {
      Container c = getParent();
      while (c != null) {
        if (c instanceof JScrollPane)
          return c.getSize();
        c = c.getParent();
      }
      return getSize();
    }

    public void setSelection(Rectangle r) {
      if (neq(selection, r)) {
        selection = r;
        pcallF(onSelectionChange);
        repaint();
      }
    }

    public Rectangle getSelection() {
      return selection;
    }

    public RGBImage getRGBImage() {
      return new RGBImage(getImage());
    }

    public void centerPoint(Point p) {
      JScrollPane sp = enclosingScrollPane(this);
      if (sp == null)
        return;
      p = new Point((int) (p.x * getZoomX()), (int) (p.y * getZoomY()));
      final JViewport viewport = sp.getViewport();
      Dimension viewSize = viewport.getExtentSize();
      int x = max(0, p.x - viewSize.width / 2);
      int y = max(0, p.y - viewSize.height / 2);
      p = new Point(x, y);
      final Point _p = p;
      awtLater(new Runnable() {

        public void run() {
          try {
            viewport.setViewPosition(_p);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "viewport.setViewPosition(_p);";
        }
      });
    }

    public Pt pointFromEvent(MouseEvent e) {
      return pointFromComponentCoordinates(new Pt(e.getX(), e.getY()));
    }

    public Pt pointFromComponentCoordinates(Pt p) {
      return new Pt((int) (p.x / zoomX), (int) (p.y / zoomY));
    }

    public void uploadTheImage() {
      call(hotwire("#1007313"), "go", getImage(), titleForUpload);
    }

    public void showFullScreen() {
      showFullScreenImageSurface(getImage());
    }

    public void zoomIn(double f) {
      setZoom(getZoomX() * f, getZoomY() * f);
    }

    public void zoomOut(double f) {
      setZoom(getZoomX() / f, getZoomY() / f);
    }
  }

  public static class RGB {

    public float r, g, b;

    public RGB() {
    }

    public RGB(float r, float g, float b) {
      this.r = r;
      this.g = g;
      this.b = b;
    }

    public RGB(double r, double g, double b) {
      this.r = (float) r;
      this.g = (float) g;
      this.b = (float) b;
    }

    public RGB(int rgb) {
      this(new Color(rgb));
    }

    public RGB(double brightness) {
      this.r = this.g = this.b = max(0f, min(1f, (float) brightness));
    }

    public RGB(Color color) {
      this.r = color.getRed() / 255f;
      this.g = color.getGreen() / 255f;
      this.b = color.getBlue() / 255f;
    }

    public RGB(String hex) {
      int i = l(hex) - 6;
      r = Integer.parseInt(hex.substring(i, i + 2), 16) / 255f;
      g = Integer.parseInt(hex.substring(i + 2, i + 4), 16) / 255f;
      b = Integer.parseInt(hex.substring(i + 4, i + 6), 16) / 255f;
    }

    public float getComponent(int i) {
      return i == 0 ? r : i == 1 ? g : b;
    }

    public Color getColor() {
      return new Color(r, g, b);
    }

    public static RGB newSafe(float r, float g, float b) {
      return new RGB(Math.max(0, Math.min(1, r)), Math.max(0, Math.min(1, g)), Math.max(0, Math.min(1, b)));
    }

    public int asInt() {
      return getColor().getRGB() & 0xFFFFFF;
    }

    public int getInt() {
      return getColor().getRGB() & 0xFFFFFF;
    }

    public float getBrightness() {
      return (r + g + b) / 3.0f;
    }

    public String getHexString() {
      return Integer.toHexString(asInt() | 0xFF000000).substring(2).toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof RGB))
        return false;
      RGB rgb = (RGB) o;
      if (Float.compare(rgb.b, b) != 0)
        return false;
      if (Float.compare(rgb.g, g) != 0)
        return false;
      if (Float.compare(rgb.r, r) != 0)
        return false;
      return true;
    }

    @Override
    public int hashCode() {
      int result = (r != +0.0f ? Float.floatToIntBits(r) : 0);
      result = 31 * result + (g != +0.0f ? Float.floatToIntBits(g) : 0);
      result = 31 * result + (b != +0.0f ? Float.floatToIntBits(b) : 0);
      return result;
    }

    public boolean isBlack() {
      return r == 0f && g == 0f && b == 0f;
    }

    public boolean isWhite() {
      return r == 1f && g == 1f && b == 1f;
    }

    public String toString() {
      return getHexString();
    }
  }

  public static interface MakesBufferedImage {

    public BufferedImage getBufferedImage();
  }

  public static class ImageSurfaceSelector extends MouseAdapter {

    public ImageSurface is;

    public Point startingPoint;

    public boolean enabled = true;

    public static boolean verbose = false;

    public ImageSurfaceSelector(ImageSurface is) {
      this.is = is;
      if (containsInstance(is.tools, ImageSurfaceSelector.class))
        return;
      is.tools.add(this);
      is.addMouseListener(this);
      is.addMouseMotionListener(this);
    }

    public void mousePressed(MouseEvent evt) {
      if (verbose)
        print("mousePressed");
      if (evt.getButton() != MouseEvent.BUTTON1)
        return;
      if (enabled)
        startingPoint = getPoint(evt);
    }

    public void mouseDragged(MouseEvent e) {
      if (verbose)
        print("mouseDragged");
      if (startingPoint != null) {
        Point endPoint = getPoint(e);
        Rectangle r = new Rectangle(startingPoint, new Dimension(endPoint.x - startingPoint.x + 1, endPoint.y - startingPoint.y + 1));
        normalize(r);
        r.width = min(r.width, is.getImage().getWidth() - r.x);
        r.height = min(r.height, is.getImage().getHeight() - r.y);
        is.setSelection(r);
      }
      if (verbose)
        print("mouseDragged done");
    }

    public static void normalize(Rectangle r) {
      if (r.width < 0) {
        r.x += r.width;
        r.width = -r.width;
      }
      if (r.height < 0) {
        r.y += r.height;
        r.height = -r.height;
      }
    }

    public void mouseReleased(MouseEvent e) {
      if (verbose)
        print("mouseReleased");
      mouseDragged(e);
      if (getPoint(e).equals(startingPoint))
        is.setSelection(null);
      startingPoint = null;
    }

    public Point getPoint(MouseEvent e) {
      return new Point((int) (e.getX() / is.getZoomX()), (int) (e.getY() / is.getZoomY()));
    }
  }

  public abstract static class Surface extends JPanel {

    public boolean clearSurface = true;

    public boolean clearOnce;

    public Surface() {
      setDoubleBuffered(false);
    }

    public Graphics2D createGraphics2D(int width, int height, Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setBackground(getBackground());
      if (clearSurface || clearOnce) {
        g2.clearRect(0, 0, width, height);
        clearOnce = false;
      }
      return g2;
    }

    public abstract void render(int w, int h, Graphics2D g);

    public void paintImmediately(int x, int y, int w, int h) {
      RepaintManager repaintManager = null;
      boolean save = true;
      if (!isDoubleBuffered()) {
        repaintManager = RepaintManager.currentManager(this);
        save = repaintManager.isDoubleBufferingEnabled();
        repaintManager.setDoubleBufferingEnabled(false);
      }
      super.paintImmediately(x, y, w, h);
      if (repaintManager != null)
        repaintManager.setDoubleBufferingEnabled(save);
    }

    public void paint(Graphics g) {
      Dimension d = getSize();
      Graphics2D g2 = createGraphics2D(d.width, d.height, g);
      render(d.width, d.height, g2);
      g2.dispose();
    }
  }

  public static int concepts_internStringsLongerThan = 10;

  public static ThreadLocal<Boolean> concepts_unlisted = new ThreadLocal();

  public static interface Derefable {

    public Concept get();
  }

  public static interface IConceptIndex {

    public void update(Concept c);

    public void remove(Concept c);
  }

  public static interface IFieldIndex<A extends Concept, Val> {

    public List<A> getAll(Val val);
  }

  public static class Concepts {

    public Map<Long, Concept> concepts = synchroTreeMap();

    public HashMap<Class, Object> perClassData = new HashMap();

    public String programID;

    public long idCounter;

    public volatile long changes, changesWritten;

    public volatile java.util.Timer autoSaver;

    public volatile boolean savingConcepts, dontSave, noXFullGrab;

    public boolean initialSave = true;

    public int autoSaveInterval = -1000;

    public boolean useGZIP = true, quietSave;

    public ReentrantLock lock = new ReentrantLock(true);

    public ReentrantLock saverLock = new ReentrantLock(true);

    public long lastSaveTook, lastSaveWas;

    public float maxAutoSavePercentage = 10;

    public List<IConceptIndex> conceptIndices;

    public Map<Class<? extends Concept>, Map<String, IFieldIndex>> fieldIndices;

    public List saveActions = synchroList();

    public Concepts() {
    }

    public Concepts(String programID) {
      this.programID = programID;
    }

    public synchronized long internalID() {
      do {
        ++idCounter;
      } while (hasConcept(idCounter));
      return idCounter;
    }

    public void initProgramID() {
      if (programID == null)
        programID = getDBProgramID();
    }

    public Concepts load() {
      return load(false);
    }

    public Concepts safeLoad() {
      return load(true);
    }

    public Concepts load(boolean allDynamic) {
      initProgramID();
      try {
        if (tryToGrab(allDynamic))
          return this;
      } catch (Throwable e) {
        if (!exceptionMessageContains(e, "no xfullgrab"))
          printShortException(e);
        print("xfullgrab failed - loading DB of " + programID + " from disk");
      }
      return loadFromDisk(allDynamic);
    }

    public Concepts loadFromDisk() {
      return loadFromDisk(false);
    }

    public Concepts loadFromDisk(boolean allDynamic) {
      if (nempty(concepts))
        clearConcepts();
      long time = now();
      Map<Long, Concept> _concepts = concepts;
      readLocally2_allDynamic.set(allDynamic);
      readLocally2(this, programID, "concepts");
      Map<Long, Concept> __concepts = concepts;
      concepts = _concepts;
      concepts.putAll(__concepts);
      int l = readLocally_stringLength;
      int tokrefs = unstructure_tokrefs;
      assignConceptsToUs();
      done("Loaded " + n(l(concepts), "concepts"), time);
      if (fileSize(getProgramFile(programID, "idCounter.structure")) != 0)
        readLocally2(this, programID, "idCounter");
      else
        calcIdCounter();
      if (initialSave)
        allChanged();
      return this;
    }

    public Concepts loadConcepts() {
      return load();
    }

    public boolean tryToGrab(boolean allDynamic) {
      if (sameSnippetID(programID, getDBProgramID()))
        return false;
      RemoteDB db = connectToDBOpt(programID);
      try {
        if (db != null) {
          loadGrab(db.fullgrab(), allDynamic);
          return true;
        }
      } finally {
        if (db != null)
          db.close();
      }
      return false;
    }

    public Concepts load(String grab) {
      return loadGrab(grab, false);
    }

    public Concepts safeLoad(String grab) {
      return loadGrab(grab, true);
    }

    public Concepts loadGrab(String grab, boolean allDynamic) {
      clearConcepts();
      DynamicObject_loading.set(true);
      try {
        Map<Long, Concept> map = (Map) (allDynamic ? safeUnstructure(grab) : unstructure(grab));
        concepts.putAll(map);
        assignConceptsToUs();
        for (long l : map.keySet()) idCounter = max(idCounter, l);
      } finally {
        DynamicObject_loading.set(null);
      }
      allChanged();
      return this;
    }

    public void assignConceptsToUs() {
      for (Concept c : values(concepts)) {
        c._concepts = this;
        callOpt_noArgs(c, "_doneLoading2");
      }
    }

    public String progID() {
      return programID == null ? getDBProgramID() : programID;
    }

    public Concept getConcept(String id) {
      return empty(id) ? null : getConcept(parseLong(id));
    }

    public Concept getConcept(long id) {
      return (Concept) concepts.get((long) id);
    }

    public Concept getConcept(RC ref) {
      return ref == null ? null : getConcept(ref.longID());
    }

    public boolean hasConcept(long id) {
      return concepts.containsKey((long) id);
    }

    public void deleteConcept(long id) {
      Concept c = getConcept(id);
      if (c == null)
        print("Concept " + id + " not found");
      else
        c.delete();
    }

    public void calcIdCounter() {
      long id_ = 0;
      for (long id : keys(concepts)) id_ = max(id_, id);
      idCounter = id_ + 1;
      saveLocally2(this, programID, "idCounter");
    }

    public void saveConceptsIfDirty() {
      saveConcepts();
    }

    public void save() {
      saveConcepts();
    }

    public void saveConcepts() {
      if (dontSave)
        return;
      initProgramID();
      saverLock.lock();
      savingConcepts = true;
      long start = now(), time;
      try {
        String s = null;
        long _changes = changes;
        if (_changes == changesWritten)
          return;
        File f = getProgramFile(programID, useGZIP ? "concepts.structure.gz" : "concepts.structure");
        lock.lock();
        long fullTime = now();
        try {
          saveLocally2(this, programID, "idCounter");
          if (useGZIP) {
            saveGZStructureToFile(f, cloneMap(concepts));
            getProgramFile(programID, "concepts.structure").delete();
          } else
            s = structure(cloneMap(concepts));
        } finally {
          lock.unlock();
        }
        while (nempty(saveActions)) pcallF(popFirst(saveActions));
        changesWritten = _changes;
        if (!useGZIP) {
          time = now() - start;
          if (!quietSave)
            print("Saving " + toM(l(s)) + "M chars (" + time + " ms)");
          start = now();
          saveTextFile(f, javaTokWordWrap(s));
          getProgramFile(programID, "concepts.structure.gz").delete();
        }
        copyFile(f, getProgramFile(programID, "backups/concepts.structure" + (useGZIP ? ".gz" : "") + ".backup" + ymd() + "-" + formatInt(hours(), 2)));
        time = now() - start;
        if (!quietSave)
          print(programID + ": Saved " + toK(f.length()) + " K, " + n(concepts, "concepts") + " (" + time + " ms)");
        lastSaveWas = fullTime;
        lastSaveTook = now() - fullTime;
      } finally {
        savingConcepts = false;
        saverLock.unlock();
      }
    }

    public void _autoSaveConcepts() {
      if (autoSaveInterval < 0 && maxAutoSavePercentage != 0) {
        long pivotTime = Math.round(lastSaveWas + lastSaveTook * 100.0 / maxAutoSavePercentage);
        if (now() < pivotTime) {
          return;
        }
      }
      try {
        saveConcepts();
      } catch (Throwable e) {
        print("Concept save failed, will try again: " + e);
      }
    }

    public void clearConcepts() {
      concepts.clear();
      allChanged();
    }

    public synchronized void allChanged() {
      ++changes;
    }

    public synchronized void autoSaveConcepts() {
      if (autoSaver == null) {
        if (isTransient())
          throw fail("Can't persist transient database");
        autoSaver = doEvery_daemon(abs(autoSaveInterval), new Runnable() {

          public void run() {
            try {
              _autoSaveConcepts();
            } catch (Exception __e) {
              throw rethrow(__e);
            }
          }

          public String toString() {
            return "_autoSaveConcepts()";
          }
        });
      }
    }

    public void cleanMeUp() {
      boolean shouldSave = autoSaver != null;
      if (autoSaver != null) {
        autoSaver.cancel();
        autoSaver = null;
      }
      while (savingConcepts) sleepInCleanUp(10);
      if (shouldSave)
        saveConceptsIfDirty();
    }

    public Map<Long, String> getIDsAndNames() {
      Map<Long, String> map = new HashMap();
      Map<Long, Concept> cloned = cloneMap(concepts);
      for (long id : keys(cloned)) map.put(id, cloned.get(id).className);
      return map;
    }

    public void deleteConcepts(List l) {
      for (Object o : l) if (o instanceof Long)
        concepts.remove((Long) o);
      else if (o instanceof Concept)
        ((Concept) o).delete();
      else
        warn("Can't delete " + getClassName(o));
    }

    public <A extends Concept> A conceptOfType(Class<A> type) {
      return firstOfType(allConcepts(), type);
    }

    public <A extends Concept> List<A> conceptsOfType(Class<A> type) {
      return filterByType(allConcepts(), type);
    }

    public <A extends Concept> List<A> listConcepts(Class<A> type) {
      return conceptsOfType(type);
    }

    public <A extends Concept> List<A> list(Class<A> type) {
      return conceptsOfType(type);
    }

    public List<Concept> list(String type) {
      return conceptsOfType(type);
    }

    public List<Concept> conceptsOfType(String type) {
      return filterByDynamicType(allConcepts(), "main$" + type);
    }

    public boolean hasConceptOfType(Class<? extends Concept> type) {
      return hasType(allConcepts(), type);
    }

    public void persistConcepts() {
      loadConcepts();
      autoSaveConcepts();
    }

    public void conceptPersistence() {
      persistConcepts();
    }

    public Concepts persist() {
      persistConcepts();
      return this;
    }

    public void persist(int interval) {
      autoSaveInterval = interval;
      persist();
    }

    public <A extends Concept> A ensureHas(Class<A> c, Runnable r) {
      A a = conceptOfType(c);
      if (a == null) {
        r.run();
        a = conceptOfType(c);
        if (a == null)
          throw fail("Concept not made by " + r + ": " + shortClassName(c));
      }
      return a;
    }

    public void ensureHas(Class<? extends Concept> c1, Class<? extends Concept> c2, Object func) {
      for (Concept a : conceptsOfType(c1)) {
        Concept b = findBackRef(a, c2);
        if (b == null) {
          callF(func, a);
          b = findBackRef(a, c2);
          if (b == null)
            throw fail("Concept not made by " + func + ": " + shortClassName(c2));
        }
      }
    }

    public void forEvery(Class<? extends Concept> type, Object func) {
      for (Concept c : conceptsOfType(type)) callF(func, c);
    }

    public int deleteAll(Class<? extends Concept> type) {
      List<Concept> l = (List) conceptsOfType(type);
      for (Concept c : l) c.delete();
      return l(l);
    }

    public Collection<Concept> allConcepts() {
      synchronized (concepts) {
        return new ArrayList(values(concepts));
      }
    }

    public <A extends Concept> int countConcepts(Class<A> c, Object... params) {
      int n = 0;
      for (A x : list(c)) if (checkConceptFields(x, params))
        ++n;
      return n;
    }

    public int countConcepts(String c, Object... params) {
      int n = 0;
      for (Concept x : list(c)) if (checkConceptFields(x, params))
        ++n;
      return n;
    }

    public int countConcepts() {
      return l(concepts);
    }

    public synchronized void addConceptIndex(IConceptIndex index) {
      if (conceptIndices == null)
        conceptIndices = new ArrayList();
      conceptIndices.add(index);
    }

    public synchronized void addFieldIndex(Class<? extends Concept> c, String field, IFieldIndex index) {
      if (fieldIndices == null)
        fieldIndices = new HashMap();
      Map<String, IFieldIndex> map = fieldIndices.get(c);
      if (map == null)
        fieldIndices.put(c, map = new HashMap());
      map.put(field, index);
    }

    public synchronized IFieldIndex getFieldIndex(Class<? extends Concept> c, String field) {
      if (fieldIndices == null)
        return null;
      Map<String, IFieldIndex> map = fieldIndices.get(c);
      return map == null ? null : map.get(field);
    }

    public RC xnew(String name, Object... values) {
      return new RC(cnew(name, values));
    }

    public void xset(long id, String field, Object value) {
      xset(new RC(id), field, value);
    }

    public void xset(RC c, String field, Object value) {
      if (value instanceof RC)
        value = getConcept((RC) value);
      cset(getConcept(c), field, value);
    }

    public Object xget(long id, String field) {
      return xget(new RC(id), field);
    }

    public Object xget(RC c, String field) {
      return xgetPost(cget(getConcept(c), field));
    }

    public Object xgetPost(Object o) {
      o = deref(o);
      if (o instanceof Concept)
        return new RC((Concept) o);
      return o;
    }

    public void xdelete(long id) {
      xdelete(new RC(id));
    }

    public void xdelete(RC c) {
      getConcept(c).delete();
    }

    public void xdelete(List<RC> l) {
      for (RC c : l) xdelete(c);
    }

    public List<RC> xlist() {
      return map("toPassRef", allConcepts());
    }

    public List<RC> xlist(String className) {
      return map("toPassRef", conceptsOfType(className));
    }

    public boolean isTransient() {
      return eq(programID, "-");
    }

    public String xfullgrab() {
      if (noXFullGrab)
        throw fail("no xfullgrab (DB too large)");
      Lock __9 = lock();
      lock(__9);
      try {
        if (changes == changesWritten && !isTransient())
          return loadConceptsStructure(programID);
        return structure(cloneMap(concepts));
      } finally {
        unlock(__9);
      }
    }

    public void xshutdown() {
      cleanKillVM();
    }

    public long xchangeCount() {
      return changes;
    }

    public int xcount() {
      return countConcepts();
    }

    public void register(Concept c) {
      if (c._concepts == this)
        return;
      if (c._concepts != null)
        throw fail("Can't re-register");
      c._concepts = this;
      c.id = internalID();
      c.created = now();
      concepts.put((long) c.id, c);
      c.change();
    }
  }

  public static volatile Concepts mainConcepts = new Concepts();

  public static class Concept extends DynamicObject {

    public transient Concepts _concepts;

    public long id;

    public long created;

    public Concept(String className) {
      super(className);
      _created();
    }

    public Concept() {
      if (!_loading()) {
        _created();
      }
    }

    public Concept(boolean unlisted) {
      if (!unlisted)
        _created();
    }

    public List<Ref> refs;

    public List<Ref> backRefs;

    public static boolean loading() {
      return _loading();
    }

    public static boolean _loading() {
      return isTrue(DynamicObject_loading.get());
    }

    public void _created() {
      if (!isTrue(concepts_unlisted.get()))
        mainConcepts.register(this);
    }

    public class Ref<A extends Concept> {

      public A value;

      public Ref() {
        if (!isTrue(DynamicObject_loading.get()))
          refs = addDyn(refs, this);
      }

      public Ref(A value) {
        this.value = value;
        refs = addDyn(refs, this);
        index();
      }

      public Concept concept() {
        return Concept.this;
      }

      public A get() {
        return value;
      }

      public boolean has() {
        return value != null;
      }

      public void set(A a) {
        if (a == value)
          return;
        unindex();
        value = a;
        index();
      }

      public void set(Ref<A> ref) {
        set(ref.get());
      }

      public void clear() {
        set((A) null);
      }

      public void index() {
        if (value != null)
          value.backRefs = addDyn(value.backRefs, this);
        change();
      }

      public void unindex() {
        if (value != null)
          value.backRefs = removeDyn(value.backRefs, this);
      }

      public void change() {
        Concept.this.change();
      }
    }

    public class RefL<A extends Concept> extends AbstractList<A> {

      public List<Ref<A>> l = new ArrayList();

      public A set(int i, A o) {
        A prev = l.get(i).get();
        l.get(i).set(o);
        return prev;
      }

      public void add(int i, A o) {
        l.add(i, new Ref(o));
      }

      public A get(int i) {
        return l.get(i).get();
      }

      public A remove(int i) {
        return l.remove(i).get();
      }

      public int size() {
        return l.size();
      }

      public boolean contains(Object o) {
        if (o instanceof Concept)
          for (Ref<A> r : l) if (eq(r.get(), o))
            return true;
        return super.contains(o);
      }
    }

    public void delete() {
      for (Ref r : unnull(refs)) r.unindex();
      refs = null;
      for (Ref r : cloneList(backRefs)) r.set((Concept) null);
      backRefs = null;
      if (_concepts != null) {
        _concepts.concepts.remove((long) id);
        _concepts.allChanged();
        if (_concepts.conceptIndices != null)
          for (IConceptIndex index : _concepts.conceptIndices) index.remove(this);
        _concepts = null;
      }
      id = 0;
    }

    public BaseXRef export() {
      return new BaseXRef(_concepts.progID(), id);
    }

    public void change() {
      if (_concepts != null) {
        _concepts.allChanged();
        if (_concepts.conceptIndices != null)
          for (IConceptIndex index : _concepts.conceptIndices) index.update(this);
      }
    }

    public void _change() {
      change();
    }

    public String _programID() {
      return _concepts == null ? getDBProgramID() : _concepts.progID();
    }
  }

  public static class RC {

    public transient Object owner;

    public String id;

    public RC() {
    }

    public RC(long id) {
      this.id = str(id);
    }

    public RC(Object owner, long id) {
      this.id = str(id);
      this.owner = owner;
    }

    public RC(Concept c) {
      this(c.id);
    }

    public long longID() {
      return parseLong(id);
    }

    public String toString() {
      return id;
    }

    public transient RemoteDB db;

    public String getString(String field) {
      return db.xS(this, field);
    }

    public Object get(String field) {
      return db.xget(this, field);
    }

    public void set(String field, Object value) {
      db.xset(this, field, value);
    }
  }

  public static class BaseXRef {

    public String programID;

    public long id;

    public BaseXRef() {
    }

    public BaseXRef(String programID, long id) {
      this.id = id;
      this.programID = programID;
    }

    public boolean equals(Object o) {
      if (!(o instanceof BaseXRef))
        return false;
      BaseXRef r = (BaseXRef) (o);
      return eq(programID, r.programID) && eq(id, r.id);
    }

    public int hashCode() {
      return programID.hashCode() + (int) id;
    }
  }

  public static class XRef extends Concept {

    public BaseXRef ref;

    public XRef() {
    }

    public XRef(BaseXRef ref) {
      this.ref = ref;
      _doneLoading2();
    }

    public void _doneLoading2() {
      getIndex().put(ref, this);
    }

    public HashMap<BaseXRef, XRef> getIndex() {
      return getXRefIndex(_concepts);
    }
  }

  public static synchronized HashMap<BaseXRef, XRef> getXRefIndex(Concepts concepts) {
    HashMap cache = (HashMap) concepts.perClassData.get(XRef.class);
    if (cache == null)
      concepts.perClassData.put(XRef.class, cache = new HashMap());
    return cache;
  }

  public static XRef lookupOrCreateXRef(BaseXRef ref) {
    XRef xref = getXRefIndex(mainConcepts).get(ref);
    if (xref == null)
      xref = new XRef(ref);
    return xref;
  }

  public static <A extends Concept> List<A> list(Class<A> type) {
    return mainConcepts.list(type);
  }

  public static <A extends Concept> List<A> list(Concepts concepts, Class<A> type) {
    return concepts.list(type);
  }

  public static List<Concept> list(String type) {
    return mainConcepts.list(type);
  }

  public static List<Concept> list(Concepts concepts, String type) {
    return concepts.list(type);
  }

  public static int csetAll(Concept c, Object... values) {
    return cset(c, values);
  }

  public static void cleanMeUp_concepts() {
    mainConcepts.cleanMeUp();
  }

  public static void loadAndAutoSaveConcepts() {
    mainConcepts.persist();
  }

  public static void loadAndAutoSaveConcepts(int interval) {
    mainConcepts.persist(interval);
  }

  public static void loadConceptsFrom(String progID) {
    mainConcepts.programID = progID;
    mainConcepts.load();
  }

  public static List<Concept> conceptsOfType(String type) {
    return mainConcepts.conceptsOfType(type);
  }

  public static long changeCount() {
    return mainConcepts.changes;
  }

  public static List<String> exposedDBMethods = ll("xlist", "xnew", "xset", "xdelete", "xget", "xclass", "xfullgrab", "xshutdown", "xchangeCount", "xcount");

  public static RC toPassRef(Concept c) {
    return new RC(c);
  }

  public static UnsupportedOperationException unsupportedOperation() {
    throw new UnsupportedOperationException();
  }

  public static String quickVisualize_progID = "#1007145";

  public static Lock quickVisualize_lock = lock();

  public static boolean quickVisualize_hasCached(String query) {
    return quickVisualize_imageFile(query).length() != 0;
  }

  public static BufferedImage quickVisualize_fromCache(String query) {
    File f = quickVisualize_imageFile(query);
    if (f.length() != 0)
      try {
        return loadPNG(f);
      } catch (Throwable __e) {
        printStackTrace2(__e);
      }
    return null;
  }

  public static String quickVisualize_preprocess(String query) {
    return toUpper(shorten(trim(query), 200));
  }

  public static BufferedImage quickVisualize(String query) {
    query = quickVisualize_preprocess(query);
    if (empty(query))
      return null;
    BufferedImage img = quickVisualize_fromCache(query);
    if (img != null)
      return img;
    File f = quickVisualize_imageFile(query);
    Lock __111 = quickVisualize_lock;
    lock(__111);
    try {
      img = googleImageSearchFirst(query);
      if (img == null)
        return null;
      savePNG(f, img);
      return img;
    } finally {
      unlock(__111);
    }
  }

  public static String quickVisualize_imagePath(String query) {
    query = quickVisualize_preprocess(query);
    return fsI(quickVisualize_progID) + "/" + urlencode(query) + ".png";
  }

  public static File quickVisualize_imageFile(String query) {
    query = quickVisualize_preprocess(query);
    return prepareProgramFile(quickVisualize_progID, urlencode(query) + ".png");
  }

  public static File quickVisualize_urlsFile(String query) {
    query = quickVisualize_preprocess(query);
    return prepareProgramFile(quickVisualize_progID, "urls-" + urlencode(query) + ".txt");
  }

  public static int thoughtCircleSize(BufferedImage img) {
    return min(img.getWidth(), img.getHeight()) + 20;
  }

  public static JFrame showFullScreen(final JComponent c) {
    return (JFrame) swingAndWait(new F0<Object>() {

      public Object get() {
        try {
          GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
          if (!gd.isFullScreenSupported())
            throw fail("No full-screen mode supported!");
          boolean dec = JFrame.isDefaultLookAndFeelDecorated();
          if (dec)
            JFrame.setDefaultLookAndFeelDecorated(false);
          final JFrame window = new JFrame();
          window.setUndecorated(true);
          if (dec)
            JFrame.setDefaultLookAndFeelDecorated(true);
          registerEscape(window, new Runnable() {

            public void run() {
              try {
                disposeWindow(window);
              } catch (Exception __e) {
                throw rethrow(__e);
              }
            }

            public String toString() {
              return "disposeWindow(window)";
            }
          });
          window.add(wrap(c));
          gd.setFullScreenWindow(window);
          for (int i = 100; i <= 1000; i += 100) awtLater(i, new Runnable() {

            public void run() {
              try {
                window.toFront();
              } catch (Exception __e) {
                throw rethrow(__e);
              }
            }

            public String toString() {
              return "window.toFront()";
            }
          });
          return window;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()\r\n      ...";
      }
    });
  }

  public static Lisp lisp(String head, Object... args) {
    Lisp l = new Lisp(head);
    for (Object o : args) l.add(o);
    return l;
  }

  public static Lisp lisp(String head, Collection args) {
    return new Lisp(head, args);
  }

  public static String lines(Collection lines) {
    return fromLines(lines);
  }

  public static List<String> lines(String s) {
    return toLines(s);
  }

  public static int hashCodeFor(Object a) {
    return a == null ? 0 : a.hashCode();
  }

  public static Pt scalePt(Pt p, double f) {
    return new Pt(iround(p.x * f), iround(p.y * f));
  }

  public static boolean stdEq2(Object a, Object b) {
    if (a == null)
      return b == null;
    if (b == null)
      return false;
    if (a.getClass() != b.getClass())
      return false;
    for (String field : allFields(a)) if (neq(getOpt(a, field), getOpt(b, field)))
      return false;
    return true;
  }

  public static BufferedImage drawThoughtCirclePlus_img;

  public static int drawThoughtCirclePlus_size = 24;

  public static void drawThoughtCirclePlus(BufferedImage bg, BufferedImage img, double x, double y) {
    if (drawThoughtCirclePlus_img == null)
      drawThoughtCirclePlus_img = resizeImage(loadImage2("#1009864"), drawThoughtCirclePlus_size);
    x -= drawThoughtCirclePlus_img.getWidth() / 2;
    y -= drawThoughtCirclePlus_img.getHeight() / 2;
    drawImageOnImage(drawThoughtCirclePlus_img, bg, iround(x), iround(y));
  }

  public static double pointDistance(Pt a, Pt b) {
    return sqrt(sqr(a.x - b.x) + sqr(a.y - b.y));
  }

  public static double pointDistance(double x1, double y1, double x2, double y2) {
    return sqrt(sqr(x1 - x2) + sqr(y1 - y2));
  }

  public static int stdHash2(Object a) {
    if (a == null)
      return 0;
    return stdHash(a, toStringArray(allFields(a)));
  }

  public static boolean inRange(int x, int n) {
    return x >= 0 && x < n;
  }

  public static DoublePt translateDoublePt(Pt a, DoublePt b) {
    return a == null ? b : b == null ? new DoublePt(a.x, a.y) : new DoublePt(a.x + b.x, a.y + b.y);
  }

  public static int drawThoughtCircleText_margin = 5;

  public static Color drawThoughtCircleText_color = Color.yellow;

  public static void drawThoughtCircleText(BufferedImage bg, BufferedImage img, DoublePt p, String text) {
    Graphics2D g = imageGraphics(bg);
    g.setFont(sansSerifBold(20));
    FontMetrics fm = g.getFontMetrics();
    int h = fm.getHeight();
    double y = p.y + thoughtCircleSize(img) / 2 + drawThoughtCircleText_margin;
    for (String s : lines(text)) {
      drawTextWithOutline(g, s, (float) (p.x - fm.stringWidth(s) / 2), (float) (y + fm.getLeading() + fm.getMaxAscent()), drawThoughtCircleText_color, Color.black);
      y += h;
    }
    g.dispose();
  }

  public static boolean containsInstance(Iterable i, Class c) {
    if (i != null)
      for (Object o : i) if (isInstanceX(c, o))
        return true;
    return false;
  }

  public static void markWebsPosted() {
    markWebsPosted_createMarker();
    triggerWebsChanged();
  }

  public static boolean loadBufferedImage_useImageCache = true;

  public static BufferedImage loadBufferedImage(String snippetIDOrURL) {
    try {
      if (snippetIDOrURL == null)
        return null;
      if (isURL(snippetIDOrURL))
        return imageIO_readURL(snippetIDOrURL);
      if (!isSnippetID(snippetIDOrURL))
        throw fail("Not a URL or snippet ID: " + snippetIDOrURL);
      String snippetID = "" + parseSnippetID(snippetIDOrURL);
      File dir = getCacheProgramDir("Image-Snippets");
      if (loadBufferedImage_useImageCache) {
        dir.mkdirs();
        File file = new File(dir, snippetID + ".png");
        if (file.exists() && file.length() != 0)
          try {
            return ImageIO.read(file);
          } catch (Throwable e) {
            e.printStackTrace();
          }
      }
      String imageURL = snippetImageURL(snippetID);
      print("Loading image: " + imageURL);
      BufferedImage image = imageIO_readURL(imageURL);
      if (loadBufferedImage_useImageCache) {
        File tempFile = new File(dir, snippetID + ".tmp." + System.currentTimeMillis());
        ImageIO.write(image, "png", tempFile);
        tempFile.renameTo(new File(dir, snippetID + ".png"));
      }
      return image;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static BufferedImage loadBufferedImage(File file) {
    try {
      return file.isFile() ? ImageIO.read(file) : null;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void moveAllMenuItems(JPopupMenu src, JMenu dest) {
    Component[] l = src.getComponents();
    src.removeAll();
    for (Component c : l) dest.add(c);
  }

  public static int ptY(Pt p) {
    return p == null ? 0 : p.y;
  }

  public static int ptX(Pt p) {
    return p == null ? 0 : p.x;
  }

  public static <A> boolean setAdd(Collection<A> c, A a) {
    if (c == null || c.contains(a))
      return false;
    c.add(a);
    return true;
  }

  public static BufferedImage clipBufferedImage(BufferedImage src, Rectangle clip) {
    return clipBufferedImage(src, new Rect(clip));
  }

  public static BufferedImage clipBufferedImage(BufferedImage src, Rect r) {
    if (src == null)
      return null;
    r = intersectRects(r, new Rect(0, 0, src.getWidth(), src.getHeight()));
    if (rectEmpty(r))
      return null;
    return src.getSubimage(r.x, r.y, r.w, r.h);
  }

  public static BufferedImage clipBufferedImage(BufferedImage src, int x, int y, int w, int h) {
    return clipBufferedImage(src, new Rect(x, y, w, h));
  }

  public static void clearAll(Object... l) {
    for (Object o : l) callOpt(o, "clear");
  }

  public static JScrollPane enclosingScrollPane(Component c) {
    while (c.getParent() != null && !(c.getParent() instanceof JViewport) && c.getParent().getComponentCount() == 1) c = c.getParent();
    if (!(c.getParent() instanceof JViewport))
      return null;
    c = c.getParent().getParent();
    return c instanceof JScrollPane ? (JScrollPane) c : null;
  }

  public static <A extends Component> A repaint(A c) {
    if (c != null)
      c.repaint();
    return c;
  }

  public static <A> A findWhere(Collection<A> c, Object... data) {
    for (A x : c) if (checkFields(x, data))
      return x;
    return null;
  }

  public static void awtLater(int delay, final Object r) {
    swingLater(delay, r);
  }

  public static void awtLater(Object r) {
    swingLater(r);
  }

  public static void awtLater(JComponent component, int delay, Object r) {
    installTimer(component, r, delay, delay, false);
  }

  public static void awtLater(JFrame frame, int delay, Object r) {
    awtLater(frame.getRootPane(), delay, r);
  }

  public static class Canvas extends ImageSurface {

    public Object makeImg;

    public boolean updating;

    public Canvas() {
      zoomable = false;
    }

    public Canvas(Object makeImg) {
      this();
      this.makeImg = makeImg;
    }

    public void update() {
      updateCanvas(this, makeImg);
    }
  }

  public static Canvas jcanvas() {
    return jcanvas(null, 0);
  }

  public static Canvas jcanvas(Object f) {
    return jcanvas(f, 0);
  }

  public static Canvas jcanvas(final Object f, final int updateDelay) {
    return (Canvas) swing(new F0<Object>() {

      public Object get() {
        try {
          final Canvas is = new Canvas(f);
          is.specialPurposed = true;
          final Runnable update = new Runnable() {

            public boolean first = true;

            public void run() {
              BufferedImage img = is.getImage();
              int w = is.getWidth(), h = is.getHeight();
              if (first || img.getWidth() != w || img.getHeight() != h) {
                updateCanvas(is, f);
                first = false;
              }
            }
          };
          onResize(is, new Runnable() {

            public void run() {
              try {
                awtLater(is, updateDelay, update);
              } catch (Exception __e) {
                throw rethrow(__e);
              }
            }

            public String toString() {
              return "awtLater(is, updateDelay, update)";
            }
          });
          bindToComponent(is, update);
          return is;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "final Canvas is = new Canvas(f);\r\n    is.specialPurposed = true;\r\n    final R...";
      }
    });
  }

  public static <A> void setAddAll(List<A> a, Collection<A> b) {
    for (A x : b) setAdd(a, x);
  }

  public static boolean containsIC(Collection<String> l, String s) {
    return containsIgnoreCase(l, s);
  }

  public static boolean containsIC(String[] l, String s) {
    return containsIgnoreCase(l, s);
  }

  public static boolean containsIC(String s, char c) {
    return containsIgnoreCase(s, c);
  }

  public static boolean containsIC(String a, String b) {
    return containsIgnoreCase(a, b);
  }

  public static int asInt(Object o) {
    return toInt(o);
  }

  public static int drawThoughtLineText_shift = 10;

  public static void drawThoughtLineText_multiLine(BufferedImage bg, BufferedImage img1, int x1, int y1, BufferedImage img2, int x2, int y2, String text, Color color) {
    Graphics2D g = imageGraphics(bg);
    g.setColor(color);
    g.setFont(sansSerif(20));
    int w1 = img_minOfWidthAndHeight(img1);
    int w2 = img_minOfWidthAndHeight(img2);
    int sideShift = (w1 - w2) / 4;
    int len = vectorLength(x2 - x1, y2 - y1);
    int dx = iround(sideShift * (x2 - x1) / len), dy = iround(sideShift * (y2 - y1) / len);
    x1 += dx;
    x2 += dx;
    y1 += dy;
    y2 += dy;
    FontMetrics fm = g.getFontMetrics();
    int lineHeight = fm.getHeight();
    float yshift = 0;
    for (String line : reversed(lines(text))) {
      drawOutlineTextAlongLine(g, line, x1, y1, x2, y2, drawThoughtLine_width / 2 + drawThoughtLineText_shift, yshift, color, Color.black);
      yshift -= lineHeight;
    }
    g.dispose();
  }

  public static String firstToUpper(String s) {
    if (empty(s))
      return s;
    return Character.toUpperCase(s.charAt(0)) + s.substring(1);
  }

  public static int updateCanvas_retryInterval = 50;

  public static void updateCanvas(final Canvas canvas, final Object makeImg) {
    swingNowOrLater(new Runnable() {

      public void run() {
        try {
          if (canvas.updating || canvas.getWidth() == 0)
            return;
          canvas.updating = true;
          try {
            BufferedImage img = asBufferedImage(callF(makeImg, canvas.getWidth(), canvas.getHeight()));
            if (img != null) {
              canvas.setImage(img);
              canvas.updating = false;
            } else
              awtLater(updateCanvas_retryInterval, new Runnable() {

                public void run() {
                  try {
                    canvas.updating = false;
                    updateCanvas(canvas, makeImg);
                  } catch (Exception __e) {
                    throw rethrow(__e);
                  }
                }

                public String toString() {
                  return "canvas.updating = false;\r\n          updateCanvas(canvas, makeImg);";
                }
              });
          } catch (Throwable e) {
            canvas.updating = false;
            throw rethrow(e);
          }
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "if (canvas.updating || canvas.getWidth() == 0) return;\r\n    canvas.updating =...";
      }
    });
  }

  public static void updateCanvas(final Canvas canvas) {
    if (canvas != null)
      canvas.update();
  }

  public static boolean hasTransparency(BufferedImage img) {
    return img.getColorModel().hasAlpha();
  }

  public static double distancePointToLineSegment(Pt a, Pt b, Pt p) {
    int x1 = a.x, y1 = a.y, x2 = b.x, y2 = b.y, x3 = p.x, y3 = p.y;
    float px = x2 - x1;
    float py = y2 - y1;
    float temp = (px * px) + (py * py);
    float u = ((x3 - x1) * px + (y3 - y1) * py) / (temp);
    if (u > 1)
      u = 1;
    else if (u < 0)
      u = 0;
    float x = x1 + u * px;
    float y = y1 + u * py;
    float dx = x - x3;
    float dy = y - y3;
    return sqrt(dx * dx + dy * dy);
  }

  public static <A extends JComponent> A setToolTipText(final A c, final Object toolTip) {
    if (c == null)
      return null;
    {
      swing(new Runnable() {

        public void run() {
          try {
            String s = nullIfEmpty(str(toolTip));
            if (neq(s, c.getToolTipText()))
              c.setToolTipText(s);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "String s = nullIfEmpty(str(toolTip));\r\n    if (neq(s, c.getToolTipText()))\r\n ...";
        }
      });
    }
    return c;
  }

  public static void copyCAL(CirclesAndLines cal1, CirclesAndLines cal2) {
    if (cal1 == cal2)
      return;
    copyList(cal1.circles, cal2.circles);
    copyList(cal1.lines, cal2.lines);
    copyFields(cal1, cal2, "defaultImageID", "arrowClass", "circleClass", "imgZoom", "imageForUserMadeNodes", "title", "globalID");
    copyListeners(cal1, cal2);
  }

  public static AutoComboBox autoComboBox() {
    return autoComboBox(new ArrayList());
  }

  public static AutoComboBox autoComboBox(final Collection<String> items) {
    return swing(new F0<AutoComboBox>() {

      public AutoComboBox get() {
        try {
          AutoComboBox cb = new AutoComboBox();
          cb.setKeyWord(items);
          return cb;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "new AutoComboBox cb;\r\n    cb.setKeyWord(items);\r\n    ret cb;";
      }
    });
  }

  public static AutoComboBox autoComboBox(String value, Collection<String> items) {
    return setText(autoComboBox(items), value);
  }

  public static int drawThoughtArrow_size = 15;

  public static void drawThoughtArrow(BufferedImage bg, BufferedImage img1, double x1, double y1, BufferedImage img2, double x2, double y2, Color color) {
    double cs = thoughtCircleSize(img2) / 2 - 1;
    double dist = pointDistance(x1, y1, x2, y2);
    double arrowLen = drawThoughtArrow_size * drawArrowHead_length - 1;
    DoublePt v = blendDoublePts(new DoublePt(x2, y2), new DoublePt(x1, y1), (cs + arrowLen) / dist);
    DoublePt p = blendDoublePts(new DoublePt(x2, y2), new DoublePt(x1, y1), cs / dist);
    Graphics2D g = imageGraphics(bg);
    g.setColor(color);
    g.setStroke(new BasicStroke(drawThoughtLine_width));
    g.draw(new Line2D.Double(x1, y1, v.x, v.y));
    drawArrowHead(g, x1, y1, p.x, p.y, drawThoughtArrow_size);
    g.dispose();
  }

  public static JFrame showCenterFrame(String title, int w, int h) {
    return showCenterFrame(title, w, h, null);
  }

  public static JFrame showCenterFrame(int w, int h, Component content) {
    return showCenterFrame(defaultFrameTitle(), w, h, content);
  }

  public static JFrame showCenterFrame(String title, int w, int h, Component content) {
    JFrame frame = makeFrame(title, content);
    frame.setSize(w, h);
    return centerFrame(frame);
  }

  public static JFrame showCenterFrame(String title, Component content) {
    return centerFrame(makeFrame(title, content));
  }

  public static JFrame showCenterFrame(Component content) {
    return centerFrame(makeFrame(content));
  }

  public static Pt untranslatePt(Pt a, Pt b) {
    if (a == null)
      return b;
    return new Pt(b.x - a.x, b.y - a.y);
  }

  public static String fsi(String id) {
    return formatSnippetID(id);
  }

  public static String getTextFromClipboard() {
    try {
      Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
      if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
        return (String) transferable.getTransferData(DataFlavor.stringFlavor);
      return null;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static BufferedImage quickVisualizeOr(String query, String defaultImageID) {
    BufferedImage img = quickVisualize(query);
    return img != null ? img : loadImage2(defaultImageID);
  }

  public static <A> A nu(Class<A> c, Object... values) {
    A a = nuObject(c);
    setAll(a, values);
    return a;
  }

  public static String web_defaultRelationName = "";

  public static String web_defaultRelationName() {
    return web_defaultRelationName;
  }

  public static JFrame frameInnerSize(final Component c, final double w, final double h) {
    final JFrame frame = getFrame(c);
    if (frame != null) {
      swing(new Runnable() {

        public void run() {
          try {
            Container cp = frame.getContentPane();
            Dimension oldSize = cp.getPreferredSize();
            cp.setPreferredSize(new Dimension(iround(w), iround(h)));
            frame.pack();
            cp.setPreferredSize(oldSize);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "Container cp = frame.getContentPane();\r\n    Dimension oldSize = cp.getPreferr...";
        }
      });
    }
    return frame;
  }

  public static void frameInnerSize(JFrame frame, Dimension d) {
    frameInnerSize(frame, d.width, d.height);
  }

  public static JFrame frameInnerSize(Pt p, JFrame frame) {
    frameInnerSize(frame, p.x, p.y);
    return frame;
  }

  public static Pt pt(int x, int y) {
    return new Pt(x, y);
  }

  public static boolean neqic(String a, String b) {
    return !eqic(a, b);
  }

  public static <A> Iterator<A> iterator(Iterable<A> c) {
    return c == null ? emptyIterator() : c.iterator();
  }

  public static void showFullScreenImageSurface(BufferedImage img) {
    showFullScreen(jscroll_centered(disposeFrameOnClick(new ImageSurface(img))));
  }

  public static <A> List<A> getWhere(Collection<A> c, Object... data) {
    List l = new ArrayList();
    for (A x : c) if (checkFields(x, data))
      l.add(x);
    return l;
  }

  public static Color drawThoughtCircle_defaultColor = Color.white;

  public static void drawThoughtCircle(BufferedImage bg, BufferedImage img, double x, double y) {
    BufferedImage circle = cutImageToCircle(img_addBorder(cutImageToCircle(img), drawThoughtCircle_defaultColor, 10));
    x -= circle.getWidth() / 2;
    y -= circle.getHeight() / 2;
    drawImageOnImage(circle, bg, iround(x), iround(y));
  }

  public static long stopTiming_defaultMin = 10;

  public static long startTiming_startTime;

  public static void startTiming() {
    startTiming_startTime = now();
  }

  public static void stopTiming() {
    stopTiming(null);
  }

  public static void stopTiming(String text) {
    stopTiming(text, stopTiming_defaultMin);
  }

  public static void stopTiming(String text, long minToPrint) {
    long time = now() - startTiming_startTime;
    if (time >= minToPrint) {
      text = or2(text, "Time: ");
      print(text + time + " ms");
    }
  }

  public static void disableImageSurfaceSelector(ImageSurface is) {
    ImageSurfaceSelector s = firstInstance(is.tools, ImageSurfaceSelector.class);
    if (s == null)
      return;
    is.removeMouseListener(s);
    is.removeMouseMotionListener(s);
    is.tools.add(s);
  }

  public static JMenu jmenu(final String title, final Object... items) {
    return swing(new F0<JMenu>() {

      public JMenu get() {
        try {
          JMenu menu = new JMenu(title);
          fillJMenu(menu, items);
          return menu;
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "JMenu menu = new(title);\r\n    fillJMenu(menu, items);\r\n    ret menu;";
      }
    });
  }

  public static boolean possibleGlobalID(String s) {
    return l(s) == 16 && allLowerCaseCharacters(s);
  }

  public static void popup(final Throwable throwable) {
    popupError(throwable);
  }

  public static void popup(final String msg) {
    print(msg);
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        JOptionPane.showMessageDialog(null, msg);
      }
    });
  }

  public static BufferedImage renderTiledBackground(String tileImageID, int w, int h) {
    return renderTiledBackground(tileImageID, w, h, 0, 0);
  }

  public static BufferedImage renderTiledBackground(String tileImageID, int w, int h, int shiftX, int shiftY) {
    BufferedImage tileImage = loadImage2(tileImageID);
    BufferedImage img = newBufferedImage(w, h, Color.black);
    Graphics2D g = img.createGraphics();
    int tw = tileImage.getWidth(), th = tileImage.getHeight();
    for (int x = mod(shiftX - 1, tw) - tw + 1; x < w; x += tw) for (int y = mod(shiftY - 1, th) - th + 1; y < h; y += th) g.drawImage(tileImage, x, y, null);
    g.dispose();
    return img;
  }

  public static int drawThoughtLine_width = 10;

  public static void drawThoughtLine(BufferedImage bg, BufferedImage img1, int x1, int y1, BufferedImage img2, int x2, int y2, Color color) {
    Graphics2D g = imageGraphics(bg);
    g.setColor(color);
    g.setStroke(new BasicStroke(drawThoughtLine_width));
    g.drawLine(x1, y1, x2, y2);
    g.dispose();
  }

  public static BufferedImage loadImage2(String snippetIDOrURL) {
    return loadBufferedImage(snippetIDOrURL);
  }

  public static BufferedImage loadImage2(File file) {
    return loadBufferedImage(file);
  }

  public static Map<BufferedImage, Object> createGraphics_modulators = synchroIdentityHashMap();

  public static Graphics2D createGraphics(BufferedImage img) {
    Graphics2D g = img.createGraphics();
    Object mod = createGraphics_modulators.get(img);
    if (mod != null)
      callF(mod, g);
    return g;
  }

  public static void createGraphics_modulate(BufferedImage img, Object mod) {
    mapPut2(createGraphics_modulators, img, mod);
  }

  public static BufferedImage scaleImage(BufferedImage before, double scale) {
    if (scale == 1)
      return before;
    int w = before.getWidth();
    int h = before.getHeight();
    int neww = max(1, iround(w * scale)), newh = max(1, iround(h * scale));
    BufferedImage after = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_ARGB);
    AffineTransform at = new AffineTransform();
    at.scale(scale, scale);
    AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    return scaleOp.filter(before, after);
  }

  public static void jHandleFileDrop(JComponent c, final Object onDrop) {
    new DropTarget(c, new DropTargetAdapter() {

      public void drop(DropTargetDropEvent e) {
        try {
          Transferable tr = e.getTransferable();
          DataFlavor[] flavors = tr.getTransferDataFlavors();
          for (DataFlavor flavor : flavors) {
            if (flavor.isFlavorJavaFileListType()) {
              e.acceptDrop(e.getDropAction());
              File file = first((List<File>) tr.getTransferData(flavor));
              if (file != null && !isFalse(callF(onDrop, file)))
                e.dropComplete(true);
              return;
            }
          }
        } catch (Throwable __e) {
          printStackTrace2(__e);
        }
        e.rejectDrop();
      }
    });
  }

  public static long nowUnlessLoading() {
    return now();
  }

  public static BufferedImage getImageFromClipboard() {
    try {
      Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
      if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
        return (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
      return imageFromDataURL(getTextFromClipboard());
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void requestFocus(final JComponent c) {
    focus(c);
  }

  public static void popupError(final Throwable throwable) {
    throwable.printStackTrace();
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        String text = throwable.toString();
        JOptionPane.showMessageDialog(null, text);
      }
    });
  }

  public static int vectorLength(int x, int y) {
    return isqrt(sqr((long) x) + sqr((long) y));
  }

  public static <A> void copyList(Collection<? extends A> a, Collection<A> b) {
    if (a == null || b == null)
      return;
    b.clear();
    b.addAll(a);
  }

  public static File prepareProgramFile(String name) {
    return mkdirsForFile(getProgramFile(name));
  }

  public static File prepareProgramFile(String progID, String name) {
    return mkdirsForFile(getProgramFile(progID, name));
  }

  public static boolean rectEmpty(Rect r) {
    return r == null || r.w <= 0 || r.h <= 0;
  }

  public static <A extends JComponent> A disposeFrameOnClick(final A c) {
    onClick(c, new Runnable() {

      public void run() {
        try {
          disposeFrame(c);
        } catch (Exception __e) {
          throw rethrow(__e);
        }
      }

      public String toString() {
        return "disposeFrame(c)";
      }
    });
    return c;
  }

  public static Set<String> allFields(Object o) {
    TreeSet<String> fields = new TreeSet();
    Class _c = _getClass(o);
    do {
      for (Field f : _c.getDeclaredFields()) fields.add(f.getName());
      _c = _c.getSuperclass();
    } while (_c != null);
    return fields;
  }

  public static Rect intersectRects(Rect a, Rect b) {
    int x = max(a.x, b.x), y = max(a.y, b.y);
    int x2 = min(a.x + a.w, b.x + b.w), y2 = min(a.y + a.h, b.y + b.h);
    return new Rect(x, y, x2 - x, y2 - y);
  }

  public static void savePNG(BufferedImage img, File file) {
    try {
      File tempFile = new File(file.getPath() + "_temp");
      CriticalAction ca = beginCriticalAction("Save " + f2s(file));
      try {
        ImageIO.write(img, "png", mkdirsFor(tempFile));
        file.delete();
        tempFile.renameTo(file);
      } finally {
        ca.done();
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void savePNG(File file, BufferedImage img) {
    savePNG(img, file);
  }

  public static void savePNG(File file, RGBImage img) {
    savePNG(file, img.getBufferedImage());
  }

  public static <A extends JComponent> A focus(final A a) {
    if (a != null)
      swingLater(new Runnable() {

        public void run() {
          try {
            a.requestFocus();
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "a.requestFocus();";
        }
      });
    return a;
  }

  public static DoublePt blendDoublePts(DoublePt x, DoublePt y, double yish) {
    double xish = 1 - yish;
    return new DoublePt(x.x * xish + y.x * yish, x.y * xish + y.y * yish);
  }

  public static String nullIfEmpty(String s) {
    return isEmpty(s) ? null : s;
  }

  public static void copyListeners(Object a, Object b) {
    for (String f : fieldNames(a)) if (l(f) > 2 && startsWith(f, "on") && isUpperCase(f.charAt(2)))
      setOpt(b, f, getOpt(a, f));
  }

  public static ThreadLocal<Boolean> drawOutlineTextAlongLine_flip = new ThreadLocal();

  public static void drawOutlineTextAlongLine(Graphics2D g, String text, int x1, int y1, int x2, int y2, int shift, Color fillColor, Color outlineColor) {
    drawOutlineTextAlongLine(g, text, x1, y1, x2, y2, shift, 0, fillColor, outlineColor);
  }

  public static void drawOutlineTextAlongLine(Graphics2D g, String text, int x1, int y1, int x2, int y2, int shift, float yshift, Color fillColor, Color outlineColor) {
    Boolean flip = optPar(drawOutlineTextAlongLine_flip);
    if (y2 == y1 && x2 == x1)
      ++x2;
    AffineTransform tx = new AffineTransform();
    double angle = Math.atan2(y2 - y1, x2 - x1);
    if (flip == null)
      flip = abs(angle) > pi() / 2;
    if (flip)
      angle -= pi();
    tx.translate((x1 + x2) / 2.0, (y1 + y2) / 2.0);
    tx.rotate(angle);
    FontMetrics fm = g.getFontMetrics();
    int y = -shift - fm.getMaxDescent();
    AffineTransform old = g.getTransform();
    g.transform(tx);
    drawTextWithOutline(g, text, -fm.stringWidth(text) / 2.0f, y + yshift, fillColor, outlineColor);
    g.setTransform(old);
  }

  public static ThreadLocal<Boolean> imageGraphics_antiAlias = new ThreadLocal();

  public static Graphics2D imageGraphics(BufferedImage img) {
    return !isFalse(imageGraphics_antiAlias.get()) ? antiAliasGraphics(img) : createGraphics(img);
  }

  public static String fromLines(Collection lines) {
    StringBuilder buf = new StringBuilder();
    if (lines != null)
      for (Object line : lines) buf.append(str(line)).append('\n');
    return buf.toString();
  }

  public static String fromLines(String... lines) {
    return fromLines(asList(lines));
  }

  public static BufferedImage img_addBorder(BufferedImage img, Color color, int border) {
    int top = border, bottom = border, left = border, right = border;
    int w = img.getWidth(), h = img.getHeight();
    BufferedImage img2 = createBufferedImage(left + w + right, top + h + bottom, color);
    Graphics2D g = img2.createGraphics();
    g.drawImage(img, left, top, null);
    g.dispose();
    return img2;
  }

  public static BufferedImage cutImageToCircle(String imageID) {
    return cutImageToCircle(loadImage2(imageID));
  }

  public static BufferedImage cutImageToCircle(BufferedImage image) {
    int w = min(image.getWidth(), image.getHeight());
    return cutImageToCircle(image, w);
  }

  public static BufferedImage cutImageToCircle(BufferedImage image, int w) {
    int h = w;
    image = img_getCenterPortion(image, w, w);
    BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = output.createGraphics();
    g2.setComposite(AlphaComposite.Src);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(Color.WHITE);
    g2.fill(new Ellipse2D.Float(0, 0, w, h));
    g2.setComposite(AlphaComposite.SrcAtop);
    g2.drawImage(image, 0, 0, null);
    g2.dispose();
    return output;
  }

  public static boolean allLowerCaseCharacters(String s) {
    for (int i = 0; i < l(s); i++) if (Character.getType(s.charAt(i)) != Character.LOWERCASE_LETTER)
      return false;
    return true;
  }

  public static int img_minOfWidthAndHeight(BufferedImage image) {
    return image == null ? 0 : min(image.getWidth(), image.getHeight());
  }

  public static BufferedImage loadPNG(File file) {
    return loadBufferedImage(file);
  }

  public static void setAll(Object o, Map<String, Object> fields) {
    if (fields == null)
      return;
    for (String field : keys(fields)) set(o, field, fields.get(field));
  }

  public static void setAll(Object o, Object... values) {
    failIfOddCount(values);
    for (int i = 0; i + 1 < l(values); i += 2) {
      String field = (String) values[i];
      Object value = values[i + 1];
      set(o, field, value);
    }
  }

  public static String defaultFrameTitle() {
    return autoFrameTitle();
  }

  public static void defaultFrameTitle(String title) {
    autoFrameTitle_value = title;
  }

  public static float drawArrowHead_length = 2f;

  public static void drawArrowHead(Graphics2D g, double x1, double y1, double x2, double y2, double size) {
    if (y2 == y1 && x2 == x1)
      return;
    Path2D.Double arrowHead = new Path2D.Double();
    arrowHead.moveTo(0, 0);
    double l = drawArrowHead_length * size;
    arrowHead.lineTo(-size, -l);
    arrowHead.lineTo(size, -l);
    arrowHead.closePath();
    AffineTransform tx = new AffineTransform();
    double angle = Math.atan2(y2 - y1, x2 - x1);
    tx.translate(x2, y2);
    tx.rotate(angle - Math.PI / 2d);
    AffineTransform old = g.getTransform();
    g.transform(tx);
    g.fill(arrowHead);
    g.setTransform(old);
  }

  public static JScrollPane jscroll_centered(Component c) {
    return new JScrollPane(jFullCenter(c));
  }

  public static void onResize(Component c, final Object r) {
    c.addComponentListener(new ComponentAdapter() {

      public void componentResized(ComponentEvent e) {
        pcallF(r);
      }
    });
  }

  public static <A> A copyFields(Object x, A y, String... fields) {
    if (empty(fields)) {
      Map<String, Object> map = objectToMap(x);
      for (String field : map.keySet()) setOpt(y, field, map.get(field));
    } else
      for (String field : fields) {
        Object o = getOpt(x, field);
        if (o != null)
          setOpt(y, field, o);
      }
    return y;
  }

  public static <A> A copyFields(Object x, A y, Collection<String> fields) {
    return copyFields(x, y, asStringArray(fields));
  }

  public static <A, B> Map<A, B> synchroIdentityHashMap() {
    return synchroMap(new IdentityHashMap());
  }

  public static String toUpper(String s) {
    return s == null ? null : s.toUpperCase();
  }

  public static List<String> toUpper(Collection<String> s) {
    return allToUpper(s);
  }

  public static BufferedImage asBufferedImage(Object o) {
    BufferedImage bi = toBufferedImageOpt(o);
    if (bi == null)
      throw fail(getClassName(o));
    return bi;
  }

  public static String googleImageSearch(String q) {
    return (String) call(hotwireOnce("#1010230"), "googleImageSearch", q);
  }

  public static List<String> googleImageSearch_multi(String q) {
    return (List<String>) call(hotwireOnce("#1010230"), "googleImageSearch_multi", q);
  }

  public static BufferedImage googleImageSearchFirst(String q) {
    return (BufferedImage) call(hotwireOnce("#1010230"), "googleImageSearchFirst", q);
  }

  public static double sqrt(double x) {
    return Math.sqrt(x);
  }

  public static void markWebsPosted_createMarker() {
    markWebsPosted_createMarker(programID());
  }

  public static void markWebsPosted_createMarker(String progID) {
    createMarkerFile("#1007609", "webs.posted.at." + psI(progID));
  }

  public static BufferedImage drawImageOnImage(BufferedImage img, BufferedImage canvas, int x, int y) {
    createGraphics(canvas).drawImage(img, x, y, null);
    return canvas;
  }

  public static Font sansSerifBold(int fontSize) {
    return new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
  }

  public static <A> A firstInstance(Collection c, Class<A> type) {
    return firstOfType(c, type);
  }

  public static long sqr(long l) {
    return l * l;
  }

  public static double sqr(double d) {
    return d * d;
  }

  public static List onWebsChanged_listeners = synchroList();

  public static void triggerWebsChanged() {
    pcallF_all(onWebsChanged_listeners);
  }

  public static void onWebsChanged(Object f) {
    setAdd(onWebsChanged_listeners, f);
  }

  public static boolean isURL(String s) {
    return s.startsWith("http://") || s.startsWith("https://");
  }

  public static BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
    return resizeImage(img, newW, newH, Image.SCALE_SMOOTH);
  }

  public static BufferedImage resizeImage(BufferedImage img, int newW, int newH, int scaleType) {
    Image tmp = img.getScaledInstance(newW, newH, scaleType);
    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();
    return dimg;
  }

  public static BufferedImage resizeImage(BufferedImage img, int newW) {
    int newH = iround(img.getHeight() * (double) newW / img.getWidth());
    return resizeImage(img, newW, newH);
  }

  public static Font sansSerif(int fontSize) {
    return new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
  }

  public static void registerEscape(JFrame frame, final Runnable r) {
    String name = "Escape";
    Action action = abstractAction(name, r);
    JComponent pnl = frame.getRootPane();
    KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    pnl.getActionMap().put(name, action);
    pnl.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, name);
  }

  public static String snippetImageURL(String snippetID) {
    return snippetImageURL(snippetID, "png");
  }

  public static String snippetImageURL(String snippetID, String contentType) {
    long id = parseSnippetID(snippetID);
    String url;
    if (id == 1000010 || id == 1000012)
      url = "http://tinybrain.de:8080/tb/show-blobimage.php?id=" + id;
    else if (isImageServerSnippet(id))
      url = imageServerLink(id);
    else
      url = "https://www.botcompany.de:8443/img/" + id;
    return url;
  }

  public static BufferedImage imageIO_readURL(String url) {
    try {
      if (startsWith(url, "https:"))
        disableCertificateValidation();
      return ImageIO.read(new URL(url));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static <A extends JComponent> A bindToComponent(A component, final Runnable onShow, final Runnable onUnShow) {
    component.addAncestorListener(new AncestorListener() {

      public void ancestorAdded(AncestorEvent event) {
        pcallF(onShow);
      }

      public void ancestorRemoved(AncestorEvent event) {
        pcallF(onUnShow);
      }

      public void ancestorMoved(AncestorEvent event) {
      }
    });
    return component;
  }

  public static <A extends JComponent> A bindToComponent(A component, Runnable onShow) {
    return bindToComponent(component, onShow, null);
  }

  public static int stdHash(Object a, String... fields) {
    if (a == null)
      return 0;
    int hash = getClassName(a).hashCode();
    for (String field : fields) hash = hash * 2 + hashCode(getOpt(a, field));
    return hash;
  }

  public static boolean checkFields(Object x, Object... data) {
    for (int i = 0; i < l(data); i += 2) if (neq(getOpt(x, (String) data[i]), data[i + 1]))
      return false;
    return true;
  }

  public static int mod(int n, int m) {
    return (n % m + m) % m;
  }

  public static long mod(long n, long m) {
    return (n % m + m) % m;
  }

  public static BigInteger mod(BigInteger n, int m) {
    return n.mod(bigint(m));
  }

  public static double mod(double n, double m) {
    return (n % m + m) % m;
  }

  public static BufferedImage imageFromDataURL(String url) {
    String pref = "base64,";
    int i = indexOf(url, pref);
    if (i < 0)
      return null;
    return decodeImage(base64decode(substring(url, i + l(pref))));
  }

  public static File getCacheProgramDir() {
    return getCacheProgramDir(getProgramID());
  }

  public static File getCacheProgramDir(String snippetID) {
    return new File(userHome(), "JavaX-Caches/" + formatSnippetIDOpt(snippetID));
  }

  public static void drawTextWithOutline(Graphics2D g2, String text, float x, float y, Color fillColor, Color outlineColor) {
    BasicStroke outlineStroke = new BasicStroke(2.0f);
    g2.translate(x, y);
    Stroke originalStroke = g2.getStroke();
    RenderingHints originalHints = g2.getRenderingHints();
    GlyphVector glyphVector = g2.getFont().createGlyphVector(g2.getFontRenderContext(), text);
    Shape textShape = glyphVector.getOutline();
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2.setColor(outlineColor);
    g2.setStroke(outlineStroke);
    g2.draw(textShape);
    g2.setColor(fillColor);
    g2.fill(textShape);
    g2.setStroke(originalStroke);
    g2.setRenderingHints(originalHints);
    g2.translate(-x, -y);
  }

  public static float abs(float f) {
    return Math.abs(f);
  }

  public static int abs(int i) {
    return Math.abs(i);
  }

  public static double abs(double d) {
    return Math.abs(d);
  }

  public static String[] asStringArray(Collection<String> c) {
    return toStringArray(c);
  }

  public static String[] asStringArray(Object o) {
    return toStringArray(o);
  }

  public static BufferedImage decodeImage(byte[] data) {
    try {
      return ImageIO.read(new ByteArrayInputStream(data));
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static <A> A firstOfType(Collection c, Class<A> type) {
    for (Object x : c) if (isInstanceX(type, x))
      return (A) x;
    return null;
  }

  public static void failIfOddCount(Object... list) {
    if (odd(l(list)))
      throw fail("Odd list size: " + list);
  }

  public static Set<String> fieldNames(Object o) {
    return allFields(o);
  }

  public static boolean isUpperCase(char c) {
    return Character.isUpperCase(c);
  }

  public static BufferedImage toBufferedImageOpt(Object o) {
    if (o instanceof BufferedImage)
      return (BufferedImage) o;
    if (o instanceof MakesBufferedImage)
      return ((MakesBufferedImage) o).getBufferedImage();
    String c = getClassName(o);
    if (eqOneOf(c, "main$BWImage", "main$RGBImage"))
      return (BufferedImage) call(o, "getBufferedImage");
    if (eq(c, "main$PNGFile"))
      return (BufferedImage) call(o, "getImage");
    return null;
  }

  public static List<String> allToUpper(Collection<String> l) {
    List<String> x = new ArrayList(l(l));
    if (l != null)
      for (String s : l) x.add(upper(s));
    return x;
  }

  public static void pcallF_all(Collection l, Object... args) {
    if (l != null)
      for (Object f : cloneList(l)) pcallF(f, args);
  }

  public static Graphics2D antiAliasGraphics(BufferedImage img) {
    return antiAliasOn(createGraphics(img));
  }

  public static int hashCode(Object a) {
    return a == null ? 0 : a.hashCode();
  }

  public static int isqrt(double x) {
    return iround(Math.sqrt(x));
  }

  public static void createMarkerFile(String progID, String name) {
    File f = getProgramFile(progID, name);
    if (fileLength(f) == 0)
      saveTextFile(f, "1");
  }

  public static double pi() {
    return Math.PI;
  }

  public static BufferedImage createBufferedImage(int w, int h, Color color) {
    return newBufferedImage(w, h, color);
  }

  public static byte[] base64decode(String s) {
    byte[] alphaToInt = base64decode_base64toint;
    int sLen = s.length();
    int numGroups = sLen / 4;
    if (4 * numGroups != sLen)
      throw new IllegalArgumentException("String length must be a multiple of four.");
    int missingBytesInLastGroup = 0;
    int numFullGroups = numGroups;
    if (sLen != 0) {
      if (s.charAt(sLen - 1) == '=') {
        missingBytesInLastGroup++;
        numFullGroups--;
      }
      if (s.charAt(sLen - 2) == '=')
        missingBytesInLastGroup++;
    }
    byte[] result = new byte[3 * numGroups - missingBytesInLastGroup];
    int inCursor = 0, outCursor = 0;
    for (int i = 0; i < numFullGroups; i++) {
      int ch0 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      int ch1 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      int ch2 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      int ch3 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));
      result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
      result[outCursor++] = (byte) ((ch2 << 6) | ch3);
    }
    if (missingBytesInLastGroup != 0) {
      int ch0 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      int ch1 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));
      if (missingBytesInLastGroup == 1) {
        int ch2 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
        result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
      }
    }
    return result;
  }

  public static int base64decode_base64toint(char c, byte[] alphaToInt) {
    int result = alphaToInt[c];
    if (result < 0)
      throw new IllegalArgumentException("Illegal character " + c);
    return result;
  }

  public static final byte[] base64decode_base64toint = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };

  public static String imageServerLink(String md5OrID) {
    if (possibleMD5(md5OrID))
      return "https://botcompany.de/images/md5/" + md5OrID;
    return imageServerLink(parseSnippetID(md5OrID));
  }

  public static String imageServerLink(long id) {
    return "https://botcompany.de/images/" + id;
  }

  public static BufferedImage img_getCenterPortion(BufferedImage img, int w, int h) {
    int iw = img.getWidth(), ih = img.getHeight();
    if (iw < w || ih < h)
      throw fail("Too small");
    int x = (iw - w) / 2, y = (ih - h) / 2;
    return clipBufferedImage(img, x, y, w, h);
  }

  public static Map<String, Object> objectToMap(Object o) {
    try {
      if (o instanceof Map)
        return (Map) o;
      TreeMap<String, Object> map = new TreeMap();
      Class c = o.getClass();
      while (c != Object.class) {
        Field[] fields = c.getDeclaredFields();
        for (final Field field : fields) {
          if ((field.getModifiers() & Modifier.STATIC) != 0)
            continue;
          field.setAccessible(true);
          final Object value = field.get(o);
          if (value != null)
            map.put(field.getName(), value);
        }
        c = c.getSuperclass();
      }
      if (o instanceof DynamicObject)
        map.putAll(((DynamicObject) o).fieldValues);
      return map;
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static List<Map<String, Object>> objectToMap(Collection l) {
    List x = new ArrayList();
    for (Object o : l) x.add(objectToMap(o));
    return x;
  }

  public static long fileLength(String path) {
    return getFileSize(path);
  }

  public static long fileLength(File f) {
    return getFileSize(f);
  }

  public static boolean possibleMD5(String s) {
    return isMD5(s);
  }

  public static String upper(String s) {
    return s == null ? null : s.toUpperCase();
  }

  public static Graphics2D antiAliasOn(Graphics2D g) {
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    return g;
  }

  public static class AutoComboBox extends JComboBox<Object> {

    public String[] keyWord = { "item1", "item2", "item3" };

    public Vector myVector = new Vector();

    public AutoComboBox() {
      setModel(new DefaultComboBoxModel(myVector));
      setSelectedIndex(-1);
      setEditable(true);
      JTextField text = (JTextField) this.getEditor().getEditorComponent();
      text.setFocusable(true);
      text.setText("");
      text.addKeyListener(new ComboListener(this, myVector));
      text.setFocusTraversalKeysEnabled(false);
      setMyVector();
    }

    public void setKeyWord(String[] keyWord) {
      this.keyWord = keyWord;
      setMyVector();
    }

    public void setKeyWord(Collection<String> keyWord) {
      setKeyWord(toStringArray(keyWord));
    }

    public final void setMyVector() {
      copyArrayToVector(keyWord, myVector);
    }
  }

  public static class ComboListener extends KeyAdapter {

    public JComboBox cb;

    public Vector vector;

    public ComboListener(JComboBox cb, Vector vector) {
      this.vector = vector;
      this.cb = cb;
    }

    public void keyPressed(KeyEvent key) {
      if (key.getKeyCode() == KeyEvent.VK_ENTER)
        return;
      if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
        cb.hidePopup();
        return;
      }
      if (key.getKeyCode() == KeyEvent.VK_TAB) {
        print("Have tab event (modifiers=" + key.getModifiers() + ")");
        if ((key.getModifiers() & ActionEvent.SHIFT_MASK) == 0 && cb.isPopupVisible()) {
          cb.setSelectedIndex(0);
          cb.hidePopup();
        } else
          swing_standardTabBehavior(key);
        return;
      }
      JTextField tf = (JTextField) (cb.getEditor().getEditorComponent());
      if (tf.getCaretPosition() != l(tf.getText()))
        return;
      String text = ((JTextField) key.getSource()).getText();
      Vector list = getFilteredList(text);
      if (nempty(list)) {
        cb.setModel(new DefaultComboBoxModel(list));
        cb.setSelectedIndex(-1);
        tf.setText(text);
        cb.showPopup();
      } else
        cb.hidePopup();
    }

    public Vector getFilteredList(String text) {
      return new Vector(scoredSearch(text, vector));
    }
  }

  public static class RemoteDB {

    public DialogIO db;

    public String name;

    public RemoteDB(String s) {
      this(s, false);
    }

    public RemoteDB(String s, boolean autoStart) {
      name = s;
      if (isSnippetID(s))
        name = dbBotName(s);
      db = findBot(name);
      if (db == null)
        if (autoStart) {
          nohupJavax(fsI(s));
          waitForBotStartUp(name);
          assertNotNull("Weird problem", db = findBot(s));
        } else
          throw fail("DB " + s + " not running");
    }

    public boolean functional() {
      return db != null;
    }

    public List<RC> list() {
      return adopt((List<RC>) rpc(db, "xlist"));
    }

    public List<RC> list(String className) {
      return adopt((List<RC>) rpc(db, "xlist", className));
    }

    public List<RC> xlist() {
      return list();
    }

    public List<RC> xlist(String className) {
      return list(className);
    }

    public List<RC> adopt(List<RC> l) {
      if (l != null)
        for (RC rc : l) adopt(rc);
      return l;
    }

    public RC adopt(RC rc) {
      if (rc != null)
        rc.db = this;
      return rc;
    }

    public Object adopt(Object o) {
      if (o instanceof RC)
        return adopt((RC) o);
      return o;
    }

    public String xclass(RC o) {
      return (String) rpc(db, "xclass", o);
    }

    public Object xget(RC o, String field) {
      return adopt(rpc(db, "xget", o, field));
    }

    public String xS(RC o, String field) {
      return (String) xget(o, field);
    }

    public RC xgetref(RC o, String field) {
      return adopt((RC) xget(o, field));
    }

    public void xset(RC o, String field, Object value) {
      rpc(db, "xset", o, field, value);
    }

    public RC uniq(String className) {
      RC ref = first(list(className));
      if (ref == null)
        ref = xnew(className);
      return ref;
    }

    public RC xuniq(String className) {
      return uniq(className);
    }

    public RC xnew(String className, Object... values) {
      return adopt((RC) rpc(db, "xnew", className, values));
    }

    public void xdelete(RC o) {
      rpc(db, "xdelete", o);
    }

    public void xdelete(List<RC> l) {
      rpc(db, "xdelete", l);
    }

    public void close() {
      if (db != null)
        db.close();
    }

    public String fullgrab() {
      return (String) rpc(db, "xfullgrab");
    }

    public String xfullgrab() {
      return fullgrab();
    }

    public void xshutdown() {
      rpc(db, "xshutdown");
    }

    public long xchangeCount() {
      return (long) rpc(db, "xchangeCount");
    }

    public int xcount() {
      return (int) rpc(db, "xcount");
    }

    public void reconnect() {
      close();
      db = findBot(name);
    }

    public RC rc(long id) {
      return new RC(this, id);
    }
  }

  public static class Lisp implements Iterable<Lisp> {

    public String head;

    public List<Lisp> args;

    public Lisp() {
    }

    public Lisp(String head) {
      this.head = _compactString(head);
    }

    public Lisp(String head, Lisp... args) {
      this.head = head;
      argsForEdit().addAll(asList(args));
    }

    public Lisp(String head, Collection args) {
      this.head = _compactString(head);
      for (Object arg : args) add(arg);
    }

    public List<Lisp> argsForEdit() {
      return args == null ? (args = new ArrayList()) : args;
    }

    public String toString() {
      if (empty())
        return quoteIfNotIdentifierOrInteger(head);
      List<String> bla = new ArrayList();
      for (Lisp a : args) bla.add(a.toString());
      String inner = join(", ", bla);
      if (head.equals(""))
        return "{" + inner + "}";
      else
        return quoteIfNotIdentifier(head) + "(" + inner + ")";
    }

    public String raw() {
      if (!isEmpty())
        throw fail("not raw: " + this);
      return head;
    }

    public Lisp add(Lisp l) {
      argsForEdit().add(l);
      return this;
    }

    public Lisp add(String s) {
      argsForEdit().add(new Lisp(s));
      return this;
    }

    public Lisp add(Object o) {
      if (o instanceof Lisp)
        add((Lisp) o);
      else if (o instanceof String)
        add((String) o);
      else
        throw fail("Bad argument type: " + structure(o));
      return this;
    }

    public int size() {
      return l(args);
    }

    public boolean empty() {
      return main.empty(args);
    }

    public boolean isEmpty() {
      return main.empty(args);
    }

    public boolean isLeaf() {
      return main.empty(args);
    }

    public Lisp get(int i) {
      return main.get(args, i);
    }

    public String getString(int i) {
      Lisp a = get(i);
      return a == null ? null : a.head;
    }

    public String s(int i) {
      return getString(i);
    }

    public String rawOrNull(int i) {
      Lisp a = get(i);
      return a != null && a.isLeaf() ? a.head : null;
    }

    public String raw(int i) {
      return assertNotNull(rawOrNull(i));
    }

    public boolean isLeaf(int i) {
      return rawOrNull(i) != null;
    }

    public boolean isA(String head) {
      return eq(head, this.head);
    }

    public boolean is(String head, int size) {
      return isA(head) && size() == size;
    }

    public boolean is(String head) {
      return isA(head);
    }

    public boolean headIs(String head) {
      return isA(head);
    }

    public boolean is(String... heads) {
      return asList(heads).contains(head);
    }

    public boolean isic(String... heads) {
      return containsIgnoreCase(heads, head);
    }

    public Iterator<Lisp> iterator() {
      return main.iterator(args);
    }

    public Lisp subList(int fromIndex, int toIndex) {
      Lisp l = new Lisp(head);
      l.argsForEdit().addAll(args.subList(fromIndex, toIndex));
      return l;
    }

    public boolean equals(Object o) {
      if (o == null || o.getClass() != Lisp.class)
        return false;
      Lisp l = (Lisp) (o);
      return eq(head, l.head) && (isLeaf() ? l.isLeaf() : l.args != null && eq(args, l.args));
    }

    public int hashCode() {
      return head.hashCode() + main.hashCode(args);
    }

    public Lisp addAll(List args) {
      for (Object arg : args) add(arg);
      return this;
    }

    public String unquoted() {
      return unquote(raw());
    }

    public String unq() {
      return unquoted();
    }

    public String unq(int i) {
      return get(i).unq();
    }

    public List<String> heads() {
      return collect(args, "head");
    }
  }

  public static Object cget(Object c, String field) {
    Object o = getOpt(c, field);
    if (o instanceof Concept.Ref)
      return ((Concept.Ref) o).get();
    return o;
  }

  public static Collection<Concept> allConcepts() {
    return mainConcepts.allConcepts();
  }

  public static Collection<Concept> allConcepts(Concepts concepts) {
    return concepts.allConcepts();
  }

  public static FixedRateTimer doEvery_daemon(long delay, final Object r) {
    return doEvery_daemon(delay, delay, r);
  }

  public static FixedRateTimer doEvery_daemon(long delay, long firstDelay, final Object r) {
    FixedRateTimer timer = new FixedRateTimer(true);
    timer.scheduleAtFixedRate(smartTimerTask(r, timer, delay), firstDelay, delay);
    return timer;
  }

  public static FixedRateTimer doEvery_daemon(double delaySeconds, final Object r) {
    return doEvery_daemon(toMS(delaySeconds), r);
  }

  public static String loadConceptsStructure(String progID) {
    return loadTextFilePossiblyGZipped(getProgramFile(progID, "concepts.structure"));
  }

  public static String loadConceptsStructure() {
    return loadConceptsStructure(dbProgramID());
  }

  public static boolean exceptionMessageContains(Throwable e, String s) {
    return cic(getInnerMessage(e), s);
  }

  public static void add(BitSet bs, int i) {
    bs.set(i);
  }

  public static <A> boolean add(Collection<A> c, A a) {
    return c != null && c.add(a);
  }

  public static void printShortException(Throwable e) {
    print(exceptionToStringShort(e));
  }

  public static long toK(long l) {
    return (l + 1023) / 1024;
  }

  public static long toM(long l) {
    return (l + 1024 * 1024 - 1) / (1024 * 1024);
  }

  public static String toM(long l, int digits) {
    return formatDouble(toM_double(l), digits);
  }

  public static void clearConcepts() {
    mainConcepts.clearConcepts();
  }

  public static void clearConcepts(Concepts concepts) {
    concepts.clearConcepts();
  }

  public static SortedMap synchroTreeMap() {
    return Collections.synchronizedSortedMap(new TreeMap());
  }

  public static boolean hasType(Collection c, Class type) {
    for (Object x : c) if (isInstanceX(type, x))
      return true;
    return false;
  }

  public static <A extends Concept> A uniq(Class<A> c, Object... params) {
    return uniqueConcept(c, params);
  }

  public static <A extends Concept> A uniq(Concepts cc, Class<A> c, Object... params) {
    return uniqueConcept(cc, c, params);
  }

  public static String javaTokWordWrap(String s) {
    return javaTokWordWrap(120, s);
  }

  public static String javaTokWordWrap(int cols, String s) {
    int col = 0;
    List<String> tok = javaTok(s);
    for (int i = 0; i < l(tok); i++) {
      String t = tok.get(i);
      if (odd(i) && col >= cols && !containsNewLine(t))
        tok.set(i, t = rtrimSpaces(t) + "\n");
      int idx = t.lastIndexOf('\n');
      if (idx >= 0)
        col = l(t) - (idx + 1);
      else
        col += l(t);
    }
    return join(tok);
  }

  public static Object rpc(String botName, String method, Object... args) {
    return unstructure_matchOK2OrFail(sendToLocalBot(botName, rpc_makeCall(method, args)));
  }

  public static Object rpc(DialogIO bot, String method, Object... args) {
    return unstructure_matchOK2OrFail(bot.ask(rpc_makeCall(method, args)));
  }

  public static String rpc_makeCall(String method, Object... args) {
    if (empty(args))
      return "call " + method;
    return format("call *", concatLists((List) ll(method), asList(args)));
  }

  public static void copyArrayToVector(Object[] array, Vector v) {
    v.clear();
    v.addAll(toList(array));
  }

  public static String quoteIfNotIdentifier(String s) {
    if (s == null)
      return null;
    return isJavaIdentifier(s) ? s : quote(s);
  }

  public static <A> void addAll(Collection<A> c, Iterable<A> b) {
    if (c != null && b != null)
      for (A a : b) c.add(a);
  }

  public static <A> void addAll(Collection<A> c, Collection<A> b) {
    if (c != null && b != null)
      c.addAll(b);
  }

  public static <A> void addAll(Collection<A> c, A... b) {
    if (c != null)
      c.addAll(Arrays.asList(b));
  }

  public static boolean bareDBMode_on;

  public static void bareDBMode() {
    bareDBMode(null);
  }

  public static void bareDBMode(Integer autoSaveInterval) {
    bareDBMode_on = true;
    conceptsAndBot(autoSaveInterval);
  }

  public static Str concept(String name) {
    for (Str s : list(Str.class)) if (eqic(s.name, name) || containsIgnoreCase(s.otherNames, name))
      return s;
    return new Str(name);
  }

  public static int hours() {
    return hours(Calendar.getInstance());
  }

  public static int hours(Calendar c) {
    return c.get(Calendar.HOUR_OF_DAY);
  }

  public static String _compactString(String s) {
    return s;
  }

  public static <A extends Concept> A findBackRef(Concept c, Class<A> type) {
    for (Concept.Ref r : c.backRefs) if (instanceOf(r.concept(), type))
      return (A) r.concept();
    return null;
  }

  public static <A extends Concept> A findBackRef(Class<A> type, Concept c) {
    return findBackRef(c, type);
  }

  public static <A> List<A> removeDyn(List<A> l, A a) {
    if (l == null)
      return null;
    l.remove(a);
    return empty(l) ? null : l;
  }

  public static String formatInt(int i, int digits) {
    return padLeft(str(i), '0', digits);
  }

  public static String formatInt(long l, int digits) {
    return padLeft(str(l), '0', digits);
  }

  public static <A> List<A> filterByType(Collection c, Class<A> type) {
    List<A> l = new ArrayList();
    for (Object x : c) if (isInstanceX(type, x))
      l.add((A) x);
    return l;
  }

  public static <A> List<A> filterByType(Object[] c, Class<A> type) {
    return filterByType(asList(c), type);
  }

  public static int done_minPrint = 10;

  public static long done(long startTime, String desc) {
    long time = now() - startTime;
    if (time >= done_minPrint)
      print(desc + " [" + time + " ms]");
    return time;
  }

  public static long done(String desc, long startTime) {
    return done(startTime, desc);
  }

  public static long done(long startTime) {
    return done(startTime, "");
  }

  public static void readLocally(String progID, String varNames) {
    readLocally2(mc(), progID, varNames);
  }

  public static void readLocally(String varNames) {
    readLocally2(mc(), programID(), varNames);
  }

  public static void readLocally2(Object obj, String varNames) {
    readLocally2(obj, programID(), varNames);
  }

  public static int readLocally_stringLength;

  public static ThreadLocal<Boolean> readLocally2_allDynamic = new ThreadLocal();

  public static void readLocally2(Object obj, String progID, String varNames) {
    try {
      boolean allDynamic = isTrue(getAndClearThreadLocal(readLocally2_allDynamic));
      for (String variableName : javaTokC(varNames)) {
        File textFile = new File(programDir(progID), variableName + ".text");
        String value = loadTextFile(textFile);
        if (value != null)
          set(main.class, variableName, value);
        else {
          File structureFile = new File(programDir(progID), variableName + ".structure");
          value = loadTextFile(structureFile);
          if (value == null) {
            File structureGZFile = new File(programDir(progID), variableName + ".structure.gz");
            if (!structureGZFile.isFile())
              return;
            InputStream fis = new FileInputStream(structureGZFile);
            try {
              GZIPInputStream gis = newGZIPInputStream(fis);
              InputStreamReader reader = new InputStreamReader(gis, "UTF-8");
              BufferedReader bufferedReader = new BufferedReader(reader);
              Object o = unstructure_tok(javaTokC_noMLS_onReader(bufferedReader), allDynamic, null);
              readLocally_set(obj, variableName, o);
            } finally {
              fis.close();
            }
            return;
          }
          readLocally_stringLength = l(value);
          if (nempty(value))
            readLocally_set(obj, variableName, allDynamic ? safeUnstructure(value) : unstructure(value));
        }
      }
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static void readLocally_set(Object c, String varName, Object value) {
    Object oldValue = get(c, varName);
    if (oldValue instanceof List && !(oldValue instanceof ArrayList) && value != null) {
      value = synchroList((List) value);
    }
    set(c, varName, value);
  }

  public static String ymd() {
    return year() + formatInt(month(), 2) + formatInt(dayOfMonth(), 2);
  }

  public static void setSelectedIndex(final JList l, final int i) {
    {
      swing(new Runnable() {

        public void run() {
          try {
            l.setSelectedIndex(i);
          } catch (Exception __e) {
            throw rethrow(__e);
          }
        }

        public String toString() {
          return "l.setSelectedIndex(i);";
        }
      });
    }
  }

  public static void saveGZStructureToFile(String file, Object o) {
    saveGZStructureToFile(getProgramFile(file), o);
  }

  public static void saveGZStructureToFile(File file, Object o) {
    try {
      File parentFile = file.getParentFile();
      if (parentFile != null)
        parentFile.mkdirs();
      File tempFile = tempFileFor(file);
      if (tempFile.exists())
        try {
          String saveName = tempFile.getPath() + ".saved." + now();
          copyFile(tempFile, new File(saveName));
        } catch (Throwable e) {
          printStackTrace(e);
        }
      FileOutputStream fileOutputStream = newFileOutputStream(tempFile.getPath());
      try {
        GZIPOutputStream gos = new GZIPOutputStream(fileOutputStream);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(gos, "UTF-8");
        PrintWriter printWriter = new PrintWriter(outputStreamWriter);
        structureToPrintWriter(o, printWriter);
        printWriter.close();
        gos.close();
        fileOutputStream.close();
      } catch (Throwable e) {
        fileOutputStream.close();
        tempFile.delete();
        throw rethrow(e);
      }
      if (file.exists() && !file.delete())
        throw new IOException("Can't delete " + file.getPath());
      if (!tempFile.renameTo(file))
        throw new IOException("Can't rename " + tempFile + " to " + file);
    } catch (Exception __e) {
      throw rethrow(__e);
    }
  }

  public static boolean checkConceptFields(Concept x, Object... data) {
    for (int i = 0; i < l(data); i += 2) if (neq(cget(x, (String) data[i]), deref(data[i + 1])))
      return false;
    return true;
  }

  public static void saveLocally(String variableName) {
    saveLocally(programID(), variableName);
  }

  public static void saveLocally(String progID, String variableName) {
    saveLocally2(mc(), progID, variableName);
  }

  public static void saveLocally2(Object obj, String variableName) {
    saveLocally2(obj, programID(), variableName);
  }

  public static void saveLocally2(Object obj, String progID, String variableName) {
    Lock __629 = saveLock();
    lock(__629);
    try {
      File textFile = new File(programDir(progID), variableName + ".text");
      File structureFile = new File(programDir(progID), variableName + ".structure");
      Object x = get(obj, variableName);
      if (x == null) {
        textFile.delete();
        structureFile.delete();
      } else if (x instanceof String) {
        saveTextFile(textFile, (String) x);
        structureFile.delete();
      } else {
        saveTextFile(structureFile, javaTokWordWrap(structure(x)));
        textFile.delete();
      }
    } finally {
      unlock(__629);
    }
  }

  public static List<String> scoredSearch(String query, Iterable<String> data) {
    Map<String, Integer> scores = new HashMap();
    List<String> prepared = scoredSearch_prepare(query);
    for (String s : data) {
      int score = scoredSearch_score(s, prepared);
      if (score != 0)
        scores.put(s, score);
    }
    return keysSortedByValuesDesc(scores);
  }

  public static RemoteDB connectToDBOpt(String dbNameOrID) {
    try {
      return new RemoteDB(dbNameOrID);
    } catch (Throwable __e) {
      return null;
    }
  }

  public static <A> List<A> addDyn(List<A> l, A a) {
    if (l == null)
      l = new ArrayList();
    l.add(a);
    return l;
  }

  public static void change() {
    callOpt(getOptMC("mainConcepts"), "allChanged");
  }

  public static <A> List<A> filterByDynamicType(Collection<A> c, String type) {
    List<A> l = new ArrayList();
    for (A x : c) if (eq(dynamicClassName(x), type))
      l.add(x);
    return l;
  }

  public static long waitForBotStartUp_timeoutSeconds = 60;

  public static String waitForBotStartUp(String botName) {
    for (int i = 0; i < waitForBotStartUp_timeoutSeconds; i++) {
      sleepSeconds(i == 0 ? 0 : 1);
      String addr = getBotAddress(botName);
      if (addr != null)
        return addr;
    }
    throw fail("Bot not found: " + quote(botName));
  }

  public static <A, B> Map<A, B> cloneMap(Map<A, B> map) {
    if (map == null)
      return litmap();
    synchronized (map) {
      return map instanceof TreeMap ? new TreeMap((TreeMap) map) : map instanceof LinkedHashMap ? new LinkedHashMap(map) : new HashMap(map);
    }
  }

  public static Lock dbLock() {
    return mainConcepts.lock;
  }

  public static void swing_standardTabBehavior(KeyEvent key) {
    if ((key.getModifiers() & ActionEvent.SHIFT_MASK) != 0)
      KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
    else
      KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
  }

  public static boolean hasConcept(Class<? extends Concept> c, Object... params) {
    return findConceptWhere(c, params) != null;
  }

  public static <A extends Concept> int countConcepts(Class<A> c, Object... params) {
    return mainConcepts.countConcepts(c, params);
  }

  public static int countConcepts() {
    return mainConcepts.countConcepts();
  }

  public static int countConcepts(String className) {
    return mainConcepts.countConcepts(className);
  }

  public static <A extends Concept> int countConcepts(Concepts concepts, String className) {
    return concepts.countConcepts(className);
  }

  public static <A, B> List<B> getAll(Map<A, B> map, Collection<A> l) {
    return lookupAllOpt(map, l);
  }

  public static <A, B> List<B> getAll(Collection<A> l, Map<A, B> map) {
    return lookupAllOpt(map, l);
  }

  public static int year() {
    return Calendar.getInstance().get(Calendar.YEAR);
  }

  public static <A extends Concept> A uniqueConcept(Class<A> c, Object... params) {
    return uniqueConcept(mainConcepts, c, params);
  }

  public static <A extends Concept> A uniqueConcept(Concepts cc, Class<A> c, Object... params) {
    params = expandParams(c, params);
    A x = findConceptWhere(cc, c, params);
    if (x == null) {
      x = unlisted(c);
      csetAll(x, params);
      cc.register(x);
    }
    return x;
  }

  public static Lock saveLock_lock = fairLock();

  public static Lock saveLock() {
    return saveLock_lock;
  }

  public static int scoredSearch_score(String s, List<String> words) {
    int score = 0;
    for (String word : words) score += scoredSearch_score_single(s, word);
    return score;
  }

  public static int scoredSearch_score(String s, String query) {
    return scoredSearch_score(s, scoredSearch_prepare(query));
  }

  public static List<String> scoredSearch_prepare(String query) {
    return splitAtSpace(query);
  }

  public static <A extends Concept> A findConceptWhere(Class<A> c, Object... params) {
    return findConceptWhere(mainConcepts, c, params);
  }

  public static <A extends Concept> A findConceptWhere(Concepts concepts, Class<A> c, Object... params) {
    params = expandParams(c, params);
    if (concepts.fieldIndices != null)
      for (int i = 0; i < l(params); i += 2) {
        IFieldIndex<A, Object> index = concepts.getFieldIndex(c, (String) params[i]);
        if (index != null) {
          for (A x : index.getAll(params[i + 1])) if (checkConceptFields(x, params))
            return x;
          return null;
        }
      }
    for (A x : concepts.list(c)) if (checkConceptFields(x, params))
      return x;
    return null;
  }

  public static Concept findConceptWhere(Concepts concepts, String c, Object... params) {
    for (Concept x : concepts.list(c)) if (checkConceptFields(x, params))
      return x;
    return null;
  }

  public static boolean cic(Collection<String> l, String s) {
    return containsIgnoreCase(l, s);
  }

  public static boolean cic(String[] l, String s) {
    return containsIgnoreCase(l, s);
  }

  public static boolean cic(String s, char c) {
    return containsIgnoreCase(s, c);
  }

  public static boolean cic(String a, String b) {
    return containsIgnoreCase(a, b);
  }

  public static String dynamicClassName(Object o) {
    if (o instanceof DynamicObject && ((DynamicObject) o).className != null)
      return "main$" + ((DynamicObject) o).className;
    return className(o);
  }

  public static <A, B> List<B> lookupAllOpt(Map<A, B> map, Collection<A> l) {
    List<B> out = new ArrayList();
    if (l != null)
      for (A a : l) addIfNotNull(out, map.get(a));
    return out;
  }

  public static <A, B> List<B> lookupAllOpt(Collection<A> l, Map<A, B> map) {
    return lookupAllOpt(map, l);
  }

  public static Object getOptMC(String field) {
    return getOpt(mc(), field);
  }

  public static String rtrimSpaces(String s) {
    if (s == null)
      return null;
    int i = s.length();
    while (i > 0 && " \t".indexOf(s.charAt(i - 1)) >= 0) --i;
    return i < s.length() ? s.substring(0, i) : s;
  }

  public static String formatDouble(double d, int digits) {
    String format = "0." + rep(digits, '#');
    return new java.text.DecimalFormat(format, new java.text.DecimalFormatSymbols(Locale.ENGLISH)).format(d);
  }

  public static TimerTask smartTimerTask(Object r, java.util.Timer timer, long delay) {
    return new SmartTimerTask(r, timer, delay);
  }

  public static class SmartTimerTask extends TimerTask {

    public Object r;

    public java.util.Timer timer;

    public long delay;

    public SmartTimerTask() {
    }

    public SmartTimerTask(Object r, java.util.Timer timer, long delay) {
      this.delay = delay;
      this.timer = timer;
      this.r = r;
    }

    public String toString() {
      return "SmartTimerTask(" + r + ", " + timer + ", " + delay + ")";
    }

    public long lastRun;

    public void run() {
      if (!licensed())
        timer.cancel();
      else {
        AutoCloseable __1084 = tempActivity(r);
        try {
          lastRun = fixTimestamp(lastRun);
          long now = now();
          if (now >= lastRun + delay * 0.9) {
            lastRun = now;
            if (eq(false, pcallF(r)))
              timer.cancel();
          }
        } finally {
          _close(__1084);
        }
      }
    }
  }

  public static <A, B> List<A> keysSortedByValuesDesc(final Map<A, B> map) {
    List<A> l = new ArrayList(map.keySet());
    sort(l, mapComparatorDesc(map));
    return l;
  }

  public static Object unstructure_matchOK2OrFail(String s) {
    if (swic(s, "ok "))
      return unstructure_startingAtIndex(s, 3);
    else
      throw fail(s);
  }

  public static int month() {
    return Calendar.getInstance().get(Calendar.MONTH) + 1;
  }

  public static String getBotAddress(String bot) {
    List<ScannedBot> l = fullBotScan(bot);
    return empty(l) ? null : first(l).address;
  }

  public static String padLeft(String s, char c, int n) {
    return rep(c, n - l(s)) + s;
  }

  public static String padLeft(String s, int n) {
    return padLeft(s, ' ', n);
  }

  public static String dbProgramID() {
    return getDBProgramID();
  }

  public static String sendToLocalBot(String bot, String text, Object... args) {
    text = format3(text, args);
    DialogIO channel = findBot(bot);
    if (channel == null)
      throw fail(quote(bot) + " not found");
    try {
      channel.readLine();
      print(bot + "> " + shorten(text, 80));
      channel.sendLine(text);
      String s = channel.readLine();
      print(bot + "< " + shorten(s, 80));
      return s;
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    } finally {
      channel.close();
    }
  }

  public static String sendToLocalBot(int port, String text, Object... args) {
    text = format3(text, args);
    DialogIO channel = talkTo(port);
    try {
      channel.readLine();
      print(port + "> " + shorten(text, 80));
      channel.sendLine(text);
      String s = channel.readLine();
      print(port + "< " + shorten(s, 80));
      return s;
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    } finally {
      if (channel != null)
        channel.close();
    }
  }

  public static int dayOfMonth() {
    return days();
  }

  public static double toM_double(long l) {
    return l / (1024 * 1024.0);
  }

  public static File tempFileFor(File f) {
    return new File(f.getPath() + "_temp");
  }

  public static int days() {
    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
  }

  public static String rep(int n, char c) {
    return repeat(c, n);
  }

  public static String rep(char c, int n) {
    return repeat(c, n);
  }

  public static <A> List<A> rep(A a, int n) {
    return repeat(a, n);
  }

  public static <A> List<A> rep(int n, A a) {
    return repeat(n, a);
  }

  public static <A> void addIfNotNull(Collection<A> l, A a) {
    if (a != null)
      l.add(a);
  }

  public static List<String> splitAtSpace(String s) {
    return asList(s.split("\\s+"));
  }

  public static long fixTimestamp(long timestamp) {
    return timestamp > now() ? 0 : timestamp;
  }

  public static class ScannedBot {

    public String helloString;

    public String address;

    public ScannedBot(String helloString, String address) {
      this.address = address;
      this.helloString = helloString;
    }

    public ScannedBot() {
    }
  }

  public static List<ScannedBot> fullBotScan() {
    return fullBotScan("");
  }

  public static List<ScannedBot> fullBotScan(String searchPattern) {
    List<ScannedBot> bots = new ArrayList();
    for (ProgramScan.Program p : quickBotScan()) {
      String botName = firstPartOfHelloString(p.helloString);
      boolean isVM = startsWithIgnoreCase(p.helloString, "This is a JavaX VM.");
      boolean shouldRecurse = swic(botName, "Multi-Port") || isVM;
      if (swic(botName, searchPattern))
        bots.add(new ScannedBot(botName, "" + p.port));
      if (shouldRecurse)
        try {
          Map<Number, String> subBots = (Map) unstructure(sendToLocalBotQuietly(p.port, "list bots"));
          for (Number vport : subBots.keySet()) {
            botName = subBots.get(vport);
            if (swic(botName, searchPattern))
              bots.add(new ScannedBot(botName, p.port + "/" + vport));
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
    }
    return bots;
  }

  public static <A, B> Comparator<A> mapComparatorDesc(final Map<A, B> map) {
    return new Comparator<A>() {

      public int compare(A a, A b) {
        return cmp(map.get(b), map.get(a));
      }
    };
  }

  public static <T> void sort(T[] a, Comparator<? super T> c) {
    Arrays.sort(a, c);
  }

  public static <T> void sort(T[] a) {
    Arrays.sort(a);
  }

  public static <T> void sort(List<T> a, Comparator<? super T> c) {
    Collections.sort(a, c);
  }

  public static void sort(List a) {
    Collections.sort(a);
  }

  public static int scoredSearch_score_single(String s, String query) {
    int i = indexOfIC_underscore(s, query);
    if (i < 0)
      return 0;
    if (i > 0)
      return 1;
    return l(s) == l(query) ? 3 : 2;
  }

  public static <A extends Concept> A unlisted(Class<A> c, Object... args) {
    concepts_unlisted.set(true);
    try {
      return nuObject(c, args);
    } finally {
      concepts_unlisted.set(null);
    }
  }

  public static Object unstructure_startingAtIndex(String s, int i) {
    return unstructure_tok(javaTokC_noMLS_iterator(s, i), false, null);
  }

  public static int indexOfIC_underscore(String a, String b) {
    int la = l(a), lb = l(b);
    if (la < lb)
      return -1;
    int n = la - lb;
    elsewhere: for (int i = 0; i <= n; i++) {
      for (int j = 0; j < lb; j++) {
        char c2 = b.charAt(j);
        if (c2 == '_' || eqic(c2, a.charAt(i + j))) {
        } else
          continue elsewhere;
      }
      return i;
    }
    return -1;
  }

  public static class Str extends Concept {

    public String name;

    public List<String> otherNames = new ArrayList();

    public Str() {
    }

    public Str(String name) {
      this.name = name;
    }

    public String toString() {
      return name;
    }
  }

  public static class FixedRateTimer extends java.util.Timer {

    public FixedRateTimer() {
      this(false);
    }

    public FixedRateTimer(boolean daemon) {
      super(daemon);
      _registerTimer(this);
    }

    public List<Entry> entries = synchroList();

    public static class Entry {

      public TimerTask task;

      public long firstTime;

      public long period;

      public Entry() {
      }

      public Entry(TimerTask task, long firstTime, long period) {
        this.period = period;
        this.firstTime = firstTime;
        this.task = task;
      }

      public String toString() {
        return "Entry(" + task + ", " + firstTime + ", " + period + ")";
      }
    }

    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
      entries.add(new Entry(task, now() + delay, period));
      super.scheduleAtFixedRate(task, delay, period);
    }

    public void cancel() {
      entries.clear();
      super.cancel();
    }

    public int purge() {
      entries.clear();
      return super.purge();
    }
  }

  public static Set<java.util.Timer> _registerTimer_list = newWeakHashSet();

  public static void _registerTimer(java.util.Timer timer) {
    _registerTimer_list.add(timer);
  }

  public static <A> Set<A> newWeakHashSet() {
    return synchroWeakHashSet();
  }

  public static <A> Set<A> synchroWeakHashSet() {
    return new WeakHashSet();
  }

  public static class WeakHashSet<A> extends AbstractSet<A> {

    public Map<A, Boolean> map = newWeakHashMap();

    public int size() {
      return map.size();
    }

    public Iterator<A> iterator() {
      return keys(map).iterator();
    }

    public boolean contains(Object o) {
      return map.containsKey(o);
    }

    public boolean add(A a) {
      return map.put(a, Boolean.TRUE) != null;
    }

    public boolean remove(Object o) {
      return map.remove(o) != null;
    }

    public Object mutex() {
      return collectionMutex(map);
    }
  }
}