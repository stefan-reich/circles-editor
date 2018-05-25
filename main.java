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
class main {
static class Drawing extends Concept {
  static String _fieldOrder = "globalID name calStructure";
  String globalID = aGlobalID();
  String name;
  String calStructure;
}

static CirclesAndLines cal = new CirclesAndLines();
static Canvas canvas;
static Drawing drawing;
static JLabel lblGlobalID;
static boolean autoVis;

public static void main(final String[] args) throws Exception {
  autoRestart();
  useDBOf("#1007609");
  db();
  fixGlobalIDs();
  substance();
  { swing(new Runnable() { public void run() { try { 
    fixCAL();
    canvas = cal.show(1000, 600);
    makeMenu();
    addToWindowNorth(canvas, withMargin(jcenteredline(
      jbutton("New drawing", "newDrawing"),
      lblGlobalID = jlabel(),
      jbutton("New circle", "newCircle")
    )));
    addToWindow(canvas, withMargin(jcenteredline(
      jbutton("Save drawing", "saveDrawing"),
      jbutton("Save drawing as...", "saveDrawingAs"),
      jbutton("PUBLISH", "publish"))));
    //addToWindow(canvas, withMargin(jcenteredlabel("Create connections by dragging between circles with right mouse button")));
    addDrawingsList();
    centerFrame(canvas);
    hideConsole();
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "fixCAL();\n    canvas = cal.show(1000, 600);\n    makeMenu();\n    addToWindowNo..."; }}); }
  bot("Circles Editor.");
  sleepIfMain();
}

static void newCircle() {
  final JTextField text = jtextfield();
  showFormTitled("New Circle", "Text", text, runnableThread(new Runnable() { public void run() { try {  { JWindow _loading_window = showLoadingAnimation(); try {
    String theText = getTextTrim(text);
    newCircle(theText);
  } finally { disposeWindow(_loading_window); }}
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "{ JWindow _loading_window = showLoadingAnimation(); try {\n    String theText ..."; }}));
}

static void newCircle(String theText) {
  double x = random(0.2, 0.8), y = random(0.2, 0.8);
  if (autoVis)
    cal.circle_autoVis(theText, x, y);
  else
    cal.circle(theText, x, y);
  canvas.update();
}

static Drawing saveDrawing() {
  return saveDrawing(drawing != null ? drawing.name : "");
}
  
static Drawing saveDrawing(String name) {
  if (empty(name)) {
    final JTextField tfName = jtextfield(makeUpName());
    showFormTitled("Name drawing",
      "Name", tfName,
      new Runnable() { public void run() { try {  
        saveDrawing(or2(getTextTrim(tfName), "Untitled"));
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "saveDrawing(or2(getTextTrim(tfName), \"Untitled\"));"; }});
    return null;
  }
  
  cal.title = name;
  if (drawing == null)
    drawing = cnew(Drawing.class);
  cal.globalID = drawing.globalID;
  String s = cal_structure(cal);
  cset(drawing, "calStructure" , s, "name", name);
  setDrawing(drawing);
  return drawing;
}

static void newDrawing() {
  setCAL(new CirclesAndLines());
  setDrawing(null);
  newCircle();
}

static void makeMenu() {
  /*if (hasSSHPassword("root", "tinybrain.de"))
    addMenu(canvas, "Menu",
      "Upload all drawings to tinybrain.de (only for Stefan ^^)", f uploadAll);*/
  addMenu(canvas, "Menu",
    "Upload all drawings!", "uploadAll2");
}

static String drawingName(Drawing d) {
  return d == null ? "" : "[" + d.id + "] "
    + (nempty(d.name) ? quote(d.name) + " " : "");
}

static List makeDrawingsTable() {
  List l = new ArrayList();
  for (final Drawing d : reversed(list(Drawing.class))) {
    CirclesAndLines cal = cal_unstructure(d.calStructure);
    l.add(litorderedmap("Drawing" , drawingName(d),
      "Circles + Relations" , join("|",
        concatLists(
          collect(cal.circles, "text"),
          collectTreeSet(cal.lines, "text")))));
  }
  return l;
}

static Drawing drawingFromTable(JTable table, int row) {
  return getConcept(Drawing.class, parseFirstLong((String) getTableCell(table, row, 0)));
}

static void addDrawingsList() {
  final JTable table = sexyTableWithoutDrag();
  Object load = new VF1<Integer>() { void get(Integer row) { try {  loadDrawing(drawingFromTable(table, row)) ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "loadDrawing(drawingFromTable(table, row))"; }};
  onDoubleClick(table, load);
  
  tablePopupMenu(table, new Object() { void get(JPopupMenu menu, final int row) { try { 
    final Drawing d = drawingFromTable(table, row);
    addMenuItem(menu, "ID: " + d.globalID, new Runnable() { public void run() { try {  copyTextToClipboard(d.globalID) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "copyTextToClipboard(d.globalID)"; }});
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "final Drawing d = drawingFromTable(table, row);\n    addMenuItem(menu, \"ID: \" ..."; }});
  
  tablePopupMenuItem(table, "Rename...", new VF1<Integer>() { void get(Integer row) { try { 
    final Drawing d = drawingFromTable(table, row);
    final JTextField tf = jtextfield(d.name);
    showFormTitled("Rename Drawing",
      "Old name", jlabel(d.name),
      "New name", tf,
      new Runnable() { public void run() { try { 
        cset(d, "name" , getTextTrim(tf));
        setDrawing(d);
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "cset(d, \"name\" , getTextTrim(tf));\n        setDrawing(d);"; }});
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "final Drawing d = drawingFromTable(table, row);\n    final JTextField tf = jte..."; }});
  
  tablePopupMenuItem(table, "Show structure", new VF1<Integer>() { void get(Integer row) { try { 
    final Drawing d = drawingFromTable(table, row);
    showWrappedText("Structure of drawing", cal_simplifiedStructure(d.calStructure));
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "final Drawing d = drawingFromTable(table, row);\n    showWrappedText(\"Structur..."; }});
  
  tablePopupMenuItem(table, "Copy structure to clipboard", new VF1<Integer>() { void get(Integer row) { try { 
    final Drawing d = drawingFromTable(table, row);
    copyTextToClipboard(cal_simplifiedStructure(d.calStructure));
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "final Drawing d = drawingFromTable(table, row);\n    copyTextToClipboard(cal_s..."; }});
  
  tablePopupMenuItem(table, "Delete", new VF1<Integer>() { void get(Integer row) { try { 
    final Drawing d = drawingFromTable(table, row);
    removeConcept(d);
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "final Drawing d = drawingFromTable(table, row);\n    removeConcept(d);"; }});
  
  addToWindowSplitRight_aggressive(canvas, tableWithSearcher(table), 0.7f);
  awtCalcOnConceptsChange(table, new Runnable() { public void run() { try {  dataToTable(table, makeDrawingsTable()) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "dataToTable(table, makeDrawingsTable())"; }});
}

static void fixCAL() {
  cal.imageForUserMadeNodes = whiteImage(10, 10);
}

static void setCAL(CirclesAndLines cal) {
  main.cal = cal;
  fixCAL();
  Canvas c = canvas;
  canvas = cal.makeCanvas();
  awtReplaceComponent(c, canvas);
  //new CirclesRelater(canvas, cal);
}

static void setDrawing(Drawing d) {
  drawing = d;
  setText(lblGlobalID, drawing != null ? "Drawing ID: " + drawing.globalID : "");
  setFrameTitle(canvas, trim(drawingName(d)) + " - " + programName());
}

static void loadDrawing(Drawing d) {
  setDrawing(d);
  CirclesAndLines cal = (CirclesAndLines) ( unstructure(d.calStructure));
  //cal_setAutoVis(cal);
  setCAL(cal);
}

static void publish() {
  final Drawing d = saveDrawing();
  if (d == null) return;
  { Thread _t_0 = new Thread() {
public void run() { try { { JWindow _loading_window = showLoadingAnimation(); try {
    String fullName = "Drawing " + d.globalID + " " + renderDate(now())
      + " - " + or2(d.name, "Untitled");
    String url = uploadToImageServer(canvas.getImage(), fullName);
    infoBox("Image uploaded to server! " + url);
    String id = createSuperHighSnippet(structure(d), fullName, 56 /*SN_AI_DRAWING*/, null, null);
    infoBox("Drawing uploaded. " + fsI(id));
  } finally { disposeWindow(_loading_window); }}} catch (Throwable __e) { printStackTrace2(__e); } }
};
startThread(_t_0); }
}

static String makeUpName() {
  Circle c = first(cal.circles);
  return c == null ? "" : c.text;
}

static void saveDrawingAs() {
  drawing = cnew(Drawing.class);
  saveDrawing();
}

static void uploadAll() { fixGlobalIDs(); run("#1009985"); }
static void uploadAll2() { runInNewThread_awt("#1010485"); }

static String answer(String s) {
final Matches m = new Matches();

  if (match("new diagram with node *", s, m)) {
    { swing(new Runnable() { public void run() { try { 
      activateFrame(canvas);
      newDrawing();
      if (nempty(m.unq(0))) newCircle(m.unq(0));
    
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "activateFrame(canvas);\n      newDrawing();\n      if (nempty(m.unq(0))) newCir..."; }}); }
    return "OK";
  }

return null;
}

static void fixGlobalIDs() {
  for (Drawing d : list(Drawing.class)) try {
    if (!d.calStructure.contains(d.globalID)) {
      print("Fixing global ID: " + d.globalID);
      CirclesAndLines cal = cal_unstructure(d.calStructure);
      cal.globalID = d.globalID;
      cset(d, "calStructure" , cal_structure(cal));
    }
  } catch (Throwable __e) { printStackTrace2(__e); }
}
static String aGlobalID() {
  return randomID(16);
}
static LinkedHashMap litorderedmap(Object... x) {
  LinkedHashMap map = new LinkedHashMap();
  litmap_impl(map, x);
  return map;
}
static long parseFirstLong(String s) {
  return parseLong(jextract("<int>", s));
}
static String cal_simplifiedStructure(CirclesAndLines cal) {
  return cal_simplifiedStructure(structure(cal));
}

static String cal_simplifiedStructure(String structure) {
  CirclesAndLines cal = (CirclesAndLines) ( unstructure(structure));
  for (Circle c : cal.circles) {
    c.x = roundToOneHundredth(c.x);
    c.y = roundToOneHundredth(c.y);
  }
  return cal_structure_impl(cal);
}
static BufferedImage whiteImage(int w, int h) {
  return newBufferedImage(w, h, Color.white);
}
static void activateFrame(final Component c) {
  { swing(new Runnable() { public void run() { try { 
    Frame f = getAWTFrame(c);
    if (f == null) return;
    if (!f.isVisible()) f.setVisible(true);
    if (f.getState() == Frame.ICONIFIED)
      f.setState(Frame.NORMAL);
      
    // My glorious Windows hack
    // See: https://stackoverflow.com/questions/309023/how-to-bring-a-window-to-the-front
    if (isWindows()) {
      boolean fullscreen = f.getExtendedState() == Frame.MAXIMIZED_BOTH;
      f.setExtendedState(JFrame.ICONIFIED);
      f.setExtendedState(fullscreen ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
    }
    
    f.toFront();
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "Frame f = getAWTFrame(c);\r\n    if (f == null) return;\r\n    if (!f.isVisible()..."; }}); }
}
static Class run(String progID, String... args) {
  Class main = hotwire(progID);
  callMain(main, args);
  return main;
}
static boolean empty(Collection c) { return c == null || c.isEmpty(); }
static boolean empty(String s) { return s == null || s.length() == 0; }
static boolean empty(Map map) { return map == null || map.isEmpty(); }
static boolean empty(Object[] o) { return o == null || o.length == 0; }
static boolean empty(Object o) {
  if (o instanceof Collection) return empty((Collection) o);
  if (o instanceof String) return empty((String) o);
  if (o instanceof Map) return empty((Map) o);
  if (o instanceof Object[]) return empty((Object[]) o);
  if (o == null) return true;
  throw fail("unknown type for 'empty': " + getType(o));
}

static boolean empty(float[] a) { return a == null || a.length == 0; }
static boolean empty(int[] a) { return a == null || a.length == 0; }
static boolean empty(long[] a) { return a == null || a.length == 0; }


static String quote(Object o) {
  if (o == null) return "null";
  return quote(str(o));
}

static String quote(String s) {
  if (s == null) return "null";
  StringBuilder out = new StringBuilder((int) (l(s)*1.5+2));
  quote_impl(s, out);
  return out.toString();
}
  
static void quote_impl(String s, StringBuilder out) {
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
static String trim(String s) { return s == null ? null : s.trim(); }
static String trim(StringBuilder buf) { return buf.toString().trim(); }
static String trim(StringBuffer buf) { return buf.toString().trim(); }
static String cal_structure(CirclesAndLines cal) {
  return cal_structure_impl(restructure(cal));
}

static String cal_structure_impl(CirclesAndLines cal) {
  if (cal.arrowClass == Arrow.class) cal.arrowClass = null;
  if (cal.circleClass == Circle.class) cal.circleClass = null;
  for (Circle c : cal.circles) cal_structure_simplifyElement(c);
  for (Line l : cal.lines) cal_structure_simplifyElement(l);
  return structure(cal);
}

static void cal_structure_simplifyElement(Base b) {
  if (eq(b.traits, ll(b.text))) b.traits = null;
}
static String programName() {
  return getProgramName();
}
static TreeSet collectTreeSet(Collection c, String field) {
  TreeSet set = new TreeSet();
  for (Object a : c) {
    Object val = getOpt(a, field);
    if (val != null)
      set.add(val);
  }
  return set;
}
public static String join(String glue, Iterable<String> strings) {
  if (strings == null) return "";
  StringBuilder buf = new StringBuilder();
  Iterator<String> i = strings.iterator();
  if (i.hasNext()) {
    buf.append(i.next());
    while (i.hasNext())
      buf.append(glue).append(i.next());
  }
  return buf.toString();
}

public static String join(String glue, String... strings) {
  return join(glue, Arrays.asList(strings));
}

static String join(Iterable<String> strings) {
  return join("", strings);
}

static String join(Iterable<String> strings, String glue) {
  return join(glue, strings);
}

public static String join(String[] strings) {
  return join("", strings);
}


static String join(String glue, Pair p) {
  return p == null ? "" : str(p.a) + glue + str(p.b);
}

static String fsI(String id) {
  return formatSnippetID(id);
}

static String fsI(long id) {
  return formatSnippetID(id);
}
static void awtCalcOnConceptsChange(JComponent component, int delay, final Object runnable, final boolean runOnFirstTime) {
  awtCalcOnConceptChanges(component, delay, runnable, runOnFirstTime);
}

static void awtCalcOnConceptsChange(JComponent component, int delay, int firstDelay, final Object runnable, final boolean runOnFirstTime) {
  awtCalcOnConceptChanges(component, delay, firstDelay, runnable, runOnFirstTime);
}

static void awtCalcOnConceptsChange(JComponent component, final Object runnable) {
  awtCalcOnConceptChanges(component, 1000, runnable, true);
}

static int withMargin_defaultWidth = 6;

static JPanel withMargin(Component c) {
  return withMargin(withMargin_defaultWidth, c);
}

static JPanel withMargin(final int w, final Component c) {
  return swing(new F0<JPanel>() { JPanel get() { try { 
    JPanel p = new JPanel(new BorderLayout());
    p.setBorder(BorderFactory.createEmptyBorder(w, w, w, w));
    p.add(c);
    return p;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "JPanel p = new JPanel(new BorderLayout);\r\n    p.setBorder(BorderFactory.creat..."; }});
}
static JMenu addMenu(JComponent c, String menuName, Object... items) {
  return addMenu(getFrame(c), menuName, items);
}

static JMenu addMenu(final JFrame frame, final String menuName, final Object... items) {
  return (JMenu) swing(new F0<Object>() { Object get() { try { 
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
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "JMenuBar bar = addMenuBar(frame);\r\n    JMenu menu = getMenuNamed(bar, menuNam..."; }});
}
static String uploadToImageServer(BufferedImage img, String name) {
  return uploadToImageServerIfNotThere(img, name);
}
static JButton jbutton(String text, Object action) {
  return newButton(text, action);
}

// button without action
static JButton jbutton(String text) {
  return newButton(text, null);
}

static JButton jbutton(BufferedImage img, Object action) {
  return setButtonImage(img, jbutton("", action));
}

static JButton jbutton(Action action) {
  return swingNu(JButton.class, action);
}
static JTextField jtextfield() {
  return jTextField();
}

static JTextField jtextfield(String text) {
  return jTextField(text);
}

static JTextField jtextfield(Object o) {
  return jTextField(o);
}

static class tablePopupMenu_Maker {
  List menuMakers = new ArrayList();
}

static Map<JTable, tablePopupMenu_Maker> tablePopupMenu_map = new WeakHashMap();

static ThreadLocal<MouseEvent> tablePopupMenu_mouseEvent = new ThreadLocal();
static ThreadLocal<Boolean> tablePopupMenu_first = new ThreadLocal();

// menuMaker = voidfunc(JPopupMenu, int row)
static void tablePopupMenu(final JTable table, final Object menuMaker) {
  final boolean first = isTrue(getAndClearThreadLocal(tablePopupMenu_first));
  swingNowOrLater(new Runnable() { public void run() { try { 
    tablePopupMenu_Maker maker = tablePopupMenu_map.get(table);
    if (maker == null) {
      tablePopupMenu_map.put(table, maker = new tablePopupMenu_Maker());
      final tablePopupMenu_Maker _maker = maker;
      table.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) { displayMenu(e); }
        public void mouseReleased(MouseEvent e) { displayMenu(e); }
      
        void displayMenu(MouseEvent e) {
          if (e.isPopupTrigger()) {
            JPopupMenu menu = new JPopupMenu();
            int row = table.rowAtPoint(e.getPoint());
            if (table.getSelectedRowCount() < 2)
              table.setRowSelectionInterval(row, row);
            int emptyCount = menu.getComponentCount();
            
            tablePopupMenu_mouseEvent.set(e);
            for (Object menuMaker : _maker.menuMakers)
              pcallF(menuMaker, menu, row);
            
            // show menu if any items in it
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
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "tablePopupMenu_Maker maker = tablePopupMenu_map.get(table);\r\n    if (maker ==..."; }});
}
static String or2(String a, String b) {
  return nempty(a) ? a : b;
}

static String or2(String a, String b, String c) {
  return or2(or2(a, b), c);
}
static JSplitPane addToWindowSplitRight_aggressive(Component c, Component toAdd) {
  return addToWindowSplitRight_f(c, toAdd, new F1<JComponent, Object>() { Object get(JComponent c) { try { return  jMinWidth_pure(100, c) ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "jMinWidth_pure(100, c)"; }});
}

static JSplitPane addToWindowSplitRight_aggressive(Component c, Component toAdd, float splitRatio) {
  return setSplitPaneLater(addToWindowSplitRight_aggressive(c, toAdd), splitRatio);
}
static void sleepIfMain() {
  if (isMainProgram()) sleep();
}
// returns number of changes
static int cset(Concept c, Object... values) { try {
  if (c == null) return 0;
  int changes = 0;
  values = expandParams(c.getClass(), values);
  warnIfOddCount(values);
  for (int i = 0; i+1 < l(values); i += 2) {
    String field = (String) values[i];
    Object value = values[i+1];
    Field f = setOpt_findField(c.getClass(), field);
    //print("cset: " + c.id + " " + field + " " + struct(value) + " " + f);
    if (value instanceof RC) value = c._concepts.getConcept((RC) value);
    value = deref(value);
    
    if (value instanceof String && l((String) value) >= concepts_internStringsLongerThan) value = intern((String) value);
    
    if (f == null) {
      // TODO: keep ref if it exists
      mapPut2(c.fieldValues, field, value instanceof Concept ? c.new Ref((Concept) value) : value);
      c.change();
    } else if (isSubtypeOf(f.getType(), Concept.Ref.class)) {
      ((Concept.Ref) f.get(c)).set((Concept) derefRef(value));
      c.change(); ++changes;
    } else {
      Object old = f.get(c);
      if (neq(value, old)) {
        f.set(c, value);
        if ((f.getModifiers() & java.lang.reflect.Modifier.TRANSIENT) == 0) c.change(); 
        ++changes;
      }
    }
  }
  return changes;
} catch (Exception __e) { throw rethrow(__e); } }
static String createSuperHighSnippet(String text, String title, int type, String user, String pass) {
  String addURL = "http://tinybrain.de:8080/tb-int/add_snippet.php";
  Map query = litmap("type", type,
    "superhigh", 1,
    "public", 1,
    "text", text,
    "name", title,
    "_user", user,
    "_pass", pass);
  String answer = doPost(query, addURL);
  if (!isSnippetID(answer)) throw fail(answer);
  print("Snippet created. " + formatSnippetID(answer));
  return formatSnippetID(answer);
}
static <A> List<A> concatLists(Collection<A>... lists) {
  List<A> l = new ArrayList();
  for (Collection<A> list : lists)
    if (list != null)
      l.addAll(list);
  return l;
}

static <A> List<A> concatLists(Collection<? extends Collection<A>> lists) {
  List<A> l = new ArrayList();
  for (Collection<A> list : lists)
    if (list != null)
      l.addAll(list);
  return l;
}

static void removeConcept(long id) {
  deleteConcept(id);
}

static void removeConcept(Concept c) {
  deleteConcept(c);
}

static void removeConcept(Concept.Ref ref) {
  deleteConcept(ref);
}

static Component addToWindow(final Component c, final Component toAdd) {
  if (toAdd != null) { swing(new Runnable() { public void run() { try { 
    JFrame frame = getFrame(c);
    Container cp = frame.getContentPane();
  
    JPanel newCP = new JPanel();
    newCP.setLayout(new BorderLayout());
    newCP.add(BorderLayout.CENTER, cp);
    newCP.add(BorderLayout.SOUTH, toAdd);
    
    frame.setContentPane(newCP);
    
    // magic combo to actually relayout and repaint
    frame.revalidate();
    frame.repaint();
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "JFrame frame = getFrame(c);\r\n    Container cp = frame.getContentPane();\r\n  \r\n..."; }}); }
  return c;
}
static void addMenuItem(JPopupMenu menu, String text, Object action) {
  menu.add(jmenuItem(text, action));
}

static void addMenuItem(JPopupMenu menu, JMenuItem menuItem) {
  menu.add(menuItem);
}

static void addMenuItem(JMenu menu, String text, Object action) {
  menu.add(jmenuItem(text, action));
}

static void addMenuItem(JMenu menu, JMenuItem menuItem) {
  menu.add(menuItem);
}

static JComponent tableWithSearcher(final JTable t) {
  final JTextField input = new JTextField();
  onUpdate(input, new Runnable() {
    List lastFiltered, lastOriginal;
    
    public void run() {
      String pat = trim(input.getText());
      
      List<Map<String, Object>> data = rawTableData(t);
      if (eq(lastFiltered, data))
        data = lastOriginal;
      
      print("Searching " + n(l(data), "entry"));
      List data2 = new ArrayList();
      for (Map<String, Object> map : data)
        if (anyValueContainsIgnoreCase(map, pat))
          data2.add(map);
          
      print("Found " + n(l(data2), "entry"));
      lastFiltered = data2;
      lastOriginal = data;
      dataToTable(t, data2);
    }
  });
  return northAndCenter(withLabel("Search:", input), t);
}
static JPanel jcenteredline(final Component... components) {
  //ret new CenteredLine(components);
  return swing(new F0<JPanel>() { JPanel get() { try { return  jFullCenter(hstackWithSpacing(components)) ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "jFullCenter(hstackWithSpacing(components))"; }});
}

static JPanel jcenteredline(List<? extends Component> components) {
  return jcenteredline(asArray(Component.class, components));
}
static void hideConsole() {
  final JFrame frame = consoleFrame();
  if (frame != null) {
    autoVMExit();
    swingLater(new Runnable() { public void run() { try { 
      frame.setVisible(false);
    
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "frame.setVisible(false);"; }});
  }
}
static JTextArea showWrappedText(final String title, final String text) {
  return (JTextArea) swingAndWait(new F0<Object>() { Object get() { try { 
    JTextArea textArea = wrappedTextArea(text);
    textArea.setFont(typeWriterFont());
    makeFrame(title, new JScrollPane(textArea));
    return textArea;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "JTextArea textArea = wrappedTextArea(text);\r\n    textArea.setFont(typeWriterF..."; }});
}

static JTextArea showWrappedText(Object text) {
  return showWrappedText(autoFrameTitle(), str(text));
}
static List collect(Collection c, String field) {
  return collectField(c, field);
}

static List collect(String field, Collection c) {
  return collectField(c, field);
}


static List collect(Class c, String field) {
  return collect(list(c), field);
}

static String renderDate(long timestamp) {
  return formatDate(timestamp);
}
static CirclesAndLines cal_unstructure(String s) {
  return (CirclesAndLines) unstructure(s);
}
static Object getTableCell(JTable tbl, int row, int col) {
  if (row >= 0 && row < tbl.getModel().getRowCount())
    return tbl.getModel().getValueAt(row, col);
  return null;
}
static Android3 bot(String greeting) {
  return makeAndroid3(greeting);
}

static Android3 bot(Android3 a) {
  return makeBot(a);
}

static Android3 bot(String greeting, Object responder) {
  return makeBot(greeting, responder);
}

static Android3 bot() {
  return makeBot();
}
static <A> A setFrameTitle(A c, String title) {
  Frame f = getAWTFrame(c);
  if (f == null)
    showFrame(title, c);
  else
    f.setTitle(title);
  return c;
}

static <A extends Component> A setFrameTitle(String title, A c) {
  return setFrameTitle(c, title);
}

// magically find a field called "frame" in main class :-)
static JFrame setFrameTitle(String title) {
  Object f = getOpt(mc(), "frame");
  if (f instanceof JFrame)
    return setFrameTitle((JFrame) f, title);
  return null;
}
static JTable dataToTable(Object data) {
  return dataToTable(showTable(), data);
}

static JTable dataToTable(Object data, String title) {
  return dataToTable(showTable(title), data);
}

static JTable dataToTable(JTable table, Object data) {
  return dataToTable(table, data, false);
}

static JTable dataToTable(JTable table, Object data, boolean now) {
  List<List> rows = new ArrayList();
  List<String> cols = new ArrayList();

  if (data instanceof List) {
    for (Object x : (List) data) try {
      rows.add(dataToTable_makeRow(x, cols));
    } catch (Throwable __e) { printStackTrace2(__e); }
  } else if (data instanceof Map) {
    Map map = (Map) ( data);
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

static JWindow infoBox(String text) {
  return infoMessage(text);
}

static JWindow infoBox(String text, double seconds) {
  return infoMessage(text, seconds);
}

static JWindow infoBox(Throwable e) {
  return infoMessage(e);
}
static JLabel jlabel(final String text) {
  return swingConstruct(BetterLabel.class, text);
}

static JLabel jlabel() {
  return jlabel(" ");
}
// runnable can be a func(O o) {} receving the selected item
static void onDoubleClick(final JList list, final Object runnable) {
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

// runnable can be a func(O o) {} receving the selected row index
static void onDoubleClick(final JTable table, final Object runnable) {
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

// other components get the pointer position
// only reacts on left button
static void onDoubleClick(JComponent c, final Object runnable) {
  c.addMouseListener(new MouseAdapter() {
    public void mouseClicked(MouseEvent evt) {
      if (evt.getButton() == 1 && evt.getClickCount() == 2)
        callF(runnable, evt.getPoint());
    }
  });
}
static Object unstructure(String text) {
  return unstructure(text, false);
}

static Object unstructure(String text, final boolean allDynamic) {
  return unstructure(text, allDynamic, null);
}

static int structure_internStringsLongerThan = 50;

static int unstructure_tokrefs; // stats

abstract static class unstructure_Receiver {
  abstract void set(Object o);
}

// classFinder: func(name) -> class (optional)
static Object unstructure(String text, boolean allDynamic,
  Object classFinder) {
  if (text == null) return null;
  return unstructure_tok(javaTokC_noMLS_iterator(text), allDynamic, classFinder);
}

static Object unstructure_reader(BufferedReader reader) {
  return unstructure_tok(javaTokC_noMLS_onReader(reader), false, null);
}

static Object unstructure_tok(final Producer<String> tok, final boolean allDynamic, final Object classFinder) {
  final boolean debug = unstructure_debug;
  
  final class X {
    int i = -1;
    HashMap<Integer,Object> refs = new HashMap();
    HashMap<Integer,Object> tokrefs = new HashMap();
    HashSet<String> concepts = new HashSet();
    HashMap<String,Class> classesMap = new HashMap();
    List<Runnable> stack = new ArrayList();
    String curT;
    
    // look at current token
    String t() {
      return curT;
    }
    
    // get current token, move to next
    String tpp() {
      String t = curT;
      consume();
      return t;
    }
    
    void parse(final unstructure_Receiver out) {
      String t = t();
      
      int refID = 0;
      if (structure_isMarker(t, 0, l(t))) {
        refID = parseInt(t.substring(1));
        consume();
      }
      final int _refID = refID;
      
      // if (debug) print("parse: " + quote(t));
      
      final int tokIndex = i;  
      parse_inner(refID, tokIndex, new unstructure_Receiver() {
        void set(Object o) {
          if (_refID != 0)
            refs.put(_refID, o);
          if (o != null)
            tokrefs.put(tokIndex, o);
          out.set(o);
        }
      });
    }
    
    void parse_inner(int refID, int tokIndex, final unstructure_Receiver out) {
      String t = t();
      
      // if (debug) print("parse_inner: " + quote(t));
      
      Class c = classesMap.get(t);
      if (c == null) {
        if (t.startsWith("\"")) {
          String s = internIfLongerThan(unquote(tpp()), structure_internStringsLongerThan);
          out.set(s); return;
        }
        
        if (t.startsWith("'")) {
          out.set(unquoteCharacter(tpp())); return;
        }
        if (t.equals("bigint")) {
          out.set(parseBigInt()); return;
        }
        if (t.equals("d")) {
          out.set(parseDouble()); return;
        }
        if (t.equals("fl")) {
          out.set(parseFloat()); return;
        }
        if (t.equals("sh")) {
          consume();
          t = tpp();
          if (t.equals("-")) {
            t = tpp();
            out.set((short) (-parseInt(t))); return;
          }
          out.set((short) parseInt(t)); return;
        }
        if (t.equals("-")) {
          consume();
          t = tpp();
          out.set(isLongConstant(t) ? (Object) (-parseLong(t)) : (Object) (-parseInt(t))); return;
        }
        if (isInteger(t) || isLongConstant(t)) {
          consume();
          //if (debug) print("isLongConstant " + quote(t) + " => " + isLongConstant(t));
          if (isLongConstant(t)) {
            out.set(parseLong(t)); return;
          }
          long l = parseLong(t);
          boolean isInt = l == (int) l;
          if (debug)
            print("l=" + l + ", isInt: " + isInt);
          out.set(isInt ? (Object) new Integer((int) l) : (Object) new Long(l)); return;
        }
        if (t.equals("false") || t.equals("f")) {
          consume(); out.set(false); return;
        }
        if (t.equals("true") || t.equals("t")) {
          consume(); out.set(true); return;
        }
        if (t.equals("-")) {
          consume();
          t = tpp();
          out.set(isLongConstant(t) ? (Object) (-parseLong(t)) : (Object) (-parseInt(t))); return;
        }
        if (isInteger(t) || isLongConstant(t)) {
          consume();
          //if (debug) print("isLongConstant " + quote(t) + " => " + isLongConstant(t));
          if (isLongConstant(t)) {
            out.set(parseLong(t)); return;
          }
          long l = parseLong(t);
          boolean isInt = l == (int) l;
          if (debug)
            print("l=" + l + ", isInt: " + isInt);
          out.set(isInt ? (Object) new Integer((int) l) : (Object) new Long(l)); return;
        }
        
        if (t.equals("File")) {
          consume();
          File f = new File(unquote(tpp()));
          out.set(f); return;
        }
        
        if (t.startsWith("r") && isInteger(t.substring(1))) {
          consume();
          int ref = Integer.parseInt(t.substring(1));
          Object o = refs.get(ref);
          if (o == null)
            print("Warning: unsatisfied back reference " + ref);
          out.set(o); return;
        }
      
        if (t.startsWith("t") && isInteger(t.substring(1))) {
          consume();
          int ref = Integer.parseInt(t.substring(1));
          Object o = tokrefs.get(ref);
          if (o == null)
            print("Warning: unsatisfied token reference " + ref);
          out.set(o); return;
        }
        
        if (t.equals("hashset")) {
          parseHashSet(out); return;
        }
        if (t.equals("treeset")) {
          parseTreeSet(out); return;
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
          parseMap(out); return;
        }
        if (t.equals("[")) {
          parseList(out); return;
        }
        if (t.equals("bitset")) {
          parseBitSet(out); return;
        }
        if (t.equals("array") || t.equals("intarray")) {
          parseArray(out); return;
        }
        if (t.equals("ba")) {
          consume();
          String hex = unquote(tpp());
          out.set(hexToBytes(hex)); return;
        }
        if (t.equals("boolarray")) {
          consume();
          int n = parseInt(tpp());
          String hex = unquote(tpp());
          out.set(boolArrayFromBytes(hexToBytes(hex), n)); return;
        }
        if (t.equals("class")) {
          out.set(parseClass()); return;
        }
        if (t.equals("l")) {
          parseLisp(out); return;
        }
        if (t.equals("null")) {
          consume(); out.set(null); return;
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
        out.set(parseJava()); return;
      }

      if (c == null && !isJavaIdentifier(t))
        throw new RuntimeException("Unknown token " + (i+1) + ": " + t);
        
      // any other class name
      if (c == null) {
        // First, find class
        if (allDynamic) c = null;
        else c = classFinder != null ? (Class) callF(classFinder, t) : findClass(t);
        if (c != null)
          classesMap.put(t, c);
      }
          
      // Check if it has an outer reference
      consume();
      boolean hasBracket = eq(t(), "(");
      if (hasBracket) consume();
      boolean hasOuter = hasBracket && eq(t(), "this$1");
      
      DynamicObject dO = null;
      Object o = null;
      if (c != null) {
        o = hasOuter ? nuStubInnerObject(c) : nuEmptyObject(c);
        if (o instanceof DynamicObject) dO = (DynamicObject) o;
      } else {
        if (concepts.contains(t) && (c = findClass("Concept")) != null)
          o = dO = (DynamicObject) nuEmptyObject(c);
        else
          dO = new DynamicObject();
        dO.className = t;
        if (debug) print("Made dynamic object " + t + " " + shortClassName(dO));
      }
      
      // Save in references list early because contents of object
      // might link back to main object
      
      if (refID != 0)
        refs.put(refID, o != null ? o : dO);
      tokrefs.put(tokIndex, o != null ? o : dO);
      
      // NOW parse the fields!
      
      final LinkedHashMap<String,Object> fields = new LinkedHashMap(); // preserve order
      final Object _o = o;
      final DynamicObject _dO = dO;
      if (hasBracket) {
        stack.add(new Runnable() { public void run() { try { 
          if (eq(t(), ")")) {
            consume(")");
            objRead(_o, _dO, fields);
            out.set(_o != null ? _o : _dO);
          } else {
            final String key = unquote(tpp());
            consume("=");
            stack.add(this);
            parse(new unstructure_Receiver() {
              void set(Object value) {
                fields.put(key, value);
                if (eq(t(), ",")) consume();
              }
            });
          }
        
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (eq(t(), \")\")) {\r\n            consume(\")\");\r\n            objRead(_o, _dO, ..."; }});
      } else {
        objRead(o, dO, fields);
        out.set(o != null ? o : dO);
      }
    }
    
    void objRead(Object o, DynamicObject dO, Map<String, Object> fields) {
      
      if (o != null)
        if (dO != null) {
          if (debug)
            printStructure("setOptAllDyn", fields);
          setOptAllDyn(dO, fields);
        } else {
          setOptAll_pcall(o, fields);
          
        }
      else for (String field : keys(fields))
        dO.fieldValues.put(intern(field), fields.get(field));

      if (o != null)
        pcallOpt_noArgs(o, "_doneLoading");
    }
    
    void parseSet(final Set set, final unstructure_Receiver out) {
      parseList(new unstructure_Receiver() {
        void set(Object o) {
          set.addAll((List) o);
          out.set(set);
        }
      });
    }
    
    void parseLisp(final unstructure_Receiver out) {
      
        consume("l");
        consume("(");
        final ArrayList list = new ArrayList();
        stack.add(new Runnable() { public void run() { try { 
          if (eq(t(), ")")) {
            consume(")");
            out.set(new Lisp((String) list.get(0), subList(list, 1)));
          } else {
            stack.add(this);
            parse(new unstructure_Receiver() {
              void set(Object o) {
                list.add(o);
                if (eq(t(), ",")) consume();
              }
            });
          }
        
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (eq(t(), \")\")) {\r\n            consume(\")\");\r\n            out.set(Lisp((Str..."; }});
        if (false) // skip fail line
      
      
      throw fail("class Lisp not included");
    }
    
    void parseBitSet(final unstructure_Receiver out) {
      consume("bitset");
      consume("{");
      final BitSet bs = new BitSet();
      stack.add(new Runnable() { public void run() { try { 
        if (eq(t(), "}")) {
          consume("}");
          out.set(bs);
        } else {
          stack.add(this);
          parse(new unstructure_Receiver() {
            void set(Object o) {
              bs.set((Integer) o);
              if (eq(t(), ",")) consume();
            }
          });
        }
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (eq(t(), \"}\")) {\r\n          consume(\"}\");\r\n          out.set(bs);\r\n       ..."; }});
    }
    
    void parseList(final unstructure_Receiver out) {
      consume("[");
      final ArrayList list = new ArrayList();
      stack.add(new Runnable() { public void run() { try { 
        if (eq(t(), "]")) {
          consume("]");
          out.set(list);
        } else {
          stack.add(this);
          parse(new unstructure_Receiver() {
            void set(Object o) {
              //if (debug) print("List element type: " + getClassName(o));
              list.add(o);
              if (eq(t(), ",")) consume();
            }
          });
        }
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (eq(t(), \"]\")) {\r\n          consume(\"]\");\r\n          out.set(list);\r\n     ..."; }});
    }
    
    void parseArray(final unstructure_Receiver out) {
      final String type = tpp();
      consume("{");
      final List list = new ArrayList();
      
      stack.add(new Runnable() { public void run() { try { 
        if (eq(t(), "}")) {
          consume("}");
          out.set(type.equals("intarray") ? toIntArray(list) : list.toArray());
        } else {
          stack.add(this);
          parse(new unstructure_Receiver() {
            void set(Object o) {
              list.add(o);
              if (eq(t(), ",")) consume();
            }
          });
        }
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (eq(t(), \"}\")) {\r\n          consume(\"}\");\r\n          out.set(type.equals(\"..."; }});
    }
    
    Object parseClass() {
      consume("class");
      consume("(");
      String name = unquote(tpp());
      consume(")");
      name = dropPrefix("main$", name);
      Class c = allDynamic ? null : classFinder != null ? (Class) callF(classFinder, name) : findClass(name);
      if (c != null) return c;
      DynamicObject dO = new DynamicObject();
      dO.className = "java.lang.Class";
      dO.fieldValues.put("name", name);
      return dO;
    }
    
    Object parseBigInt() {
      consume("bigint");
      consume("(");
      String val = tpp();
      if (eq(val, "-"))
        val = "-" + tpp();
      consume(")");
      return new BigInteger(val);
    }
    
    Object parseDouble() {
      consume("d");
      consume("(");
      String val = unquote(tpp());
      consume(")");
      return Double.parseDouble(val);
    }
    
    Object parseFloat() {
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
    
    void parseHashSet(unstructure_Receiver out) {
      consume("hashset");
      parseSet(new HashSet(), out);
    }
    
    void parseTreeSet(unstructure_Receiver out) {
      consume("treeset");
      parseSet(new TreeSet(), out);
    }
    
    void parseMap(unstructure_Receiver out) {
      parseMap(new TreeMap(), out);
    }
    
    Object parseJava() {
      String j = unquote(tpp());
      Matches m = new Matches();
      if (jmatch("java.awt.Color[r=*,g=*,b=*]", j, m))
        return nuObject("java.awt.Color", parseInt(m.unq(0)), parseInt(m.unq(1)), parseInt(m.unq(2)));
      else {
        warn("Unknown Java object: " + j);
        return null;
      }
    }
    
    void parseMap(final Map map, final unstructure_Receiver out) {
      consume("{");
      stack.add(new Runnable() {
        boolean v;
        Object key;
        
        public void run() { 
          if (v) {
            v = false;
            stack.add(this);
            consume("=");
            parse(new unstructure_Receiver() {
              void set(Object value) {
                map.put(key, value);
                if (debug)
                  print("parseMap: Got value " + getClassName(value) + ", next token: " + quote(t()));
                if (eq(t(), ",")) consume();
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
                void set(Object o) {
                  key = o;
                }
              });
            }
          } // if v else
        } // run()
      });
    }
    
    /*void parseSub(unstructure_Receiver out) {
      int n = l(stack);
      parse(out);
      while (l(stack) > n)
        stack
    }*/
    
    void consume() { curT = tok.next(); ++i; }
    
    void consume(String s) {
      if (!eq(t(), s)) {
        /*S prevToken = i-1 >= 0 ? tok.get(i-1) : "";
        S nextTokens = join(tok.subList(i, Math.min(i+2, tok.size())));
        fail(quote(s) + " expected: " + prevToken + " " + nextTokens + " (" + i + "/" + tok.size() + ")");*/
        throw fail(quote(s) + " expected, got " + quote(t()));
      }
      consume();
    }
    
    void parse_x(unstructure_Receiver out) {
      consume(); // get first token
      parse(out);
      while (nempty(stack))
        popLast(stack).run();
    }
  }
  
  Boolean b = DynamicObject_loading.get();
  DynamicObject_loading.set(true);
  try {
    final Var v = new Var();
    X x = new X();
    x.parse_x(new unstructure_Receiver() {
      void set(Object o) { v.set(o); }
    });
    unstructure_tokrefs = x.tokrefs.size();
    return v.get();
  } finally {
    DynamicObject_loading.set(b);
  }
}

static boolean unstructure_debug;
static Concept getConcept(long id) {
  return mainConcepts.getConcept(id);
}

static <A extends Concept> A getConcept(Class<A> cc, long id) {
  return getConcept(mainConcepts, cc, id);
}

static <A extends Concept> A getConcept(Concepts concepts, Class<A> cc, long id) {
  Concept c = concepts.getConcept(id);
  if (c == null) return null;
  if (!isInstance(cc, c))
    throw fail("Can't convert concept: " + getClassName(c) + " -> " + getClassName(cc) + " (" + id + ")");
  return (A) c;
}
static JTable sexyTableWithoutDrag() {
  final JTable table = tableWithToolTips();

  tablePopupMenu(table, new Object() { void get(JPopupMenu menu, final int row) { try { 
    final String item = first(getTableLine(table, row));
    MouseEvent e = tablePopupMenu_mouseEvent.get();
    final int col = table.columnAtPoint(e.getPoint());
    final Object value = table.getModel().getValueAt(row, col);
    //print("Cell type: " + getClassName(value));

    if (value instanceof ImageIcon) {
      addMenuItem(menu, "Copy image to clipboard", new Runnable() { public void run() { try { 
        copyImageToClipboard(((ImageIcon) value).getImage());
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "copyImageToClipboard(((ImageIcon) value).getImage());"; }});
    } else {
      final String text = str(value);
      addMenuItem(menu, "Copy text to clipboard", new Runnable() { public void run() { try { 
        copyTextToClipboard(text);
        print("Copied text to clipboard: " + quote(text));
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "copyTextToClipboard(text);\r\n        print(\"Copied text to clipboard: \" + quot..."; }});
    }
    
    addMenuItem(menu, "Set row height...", new Runnable() { public void run() { try { 
      final JTextField tf = jTextField(table.getRowHeight());
      showTitledForm("Set row height",
        "Pixels", tf, new Runnable() { public void run() { try { 
          table.setRowHeight(parseInt(trim(tf.getText())))
        ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "table.setRowHeight(parseInt(trim(tf.getText())))"; }});
    
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "final JTextField tf = jTextField(table.getRowHeight());\r\n      showTitledForm..."; }});
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "final S item = first(getTableLine(table, row));\r\n    MouseEvent e = tablePopu..."; }});
  
  // Disable Ctrl+PageUp and Ctrl+PageDown
  
  table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
  //table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.CTRL_MASK), "none");
  /*table.registerKeyboardAction(
    null,
    KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK),
    JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
  );*/
  table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
    .put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
  table.getInputMap(JComponent.WHEN_FOCUSED)
    .put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
  table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
    .put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
  table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
    .put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
  ((InputMap) UIManager.get("Table.ancestorInputMap")).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), "none");
    
  return table;
}
static <A extends Component> A centerFrame(A c) {
  Window w = getWindow(c);
  if (w != null)
    w.setLocationRelativeTo(null); // magic trick
  return c;
}

static <A extends Component> A centerFrame(int w, int h, A c) {
  return centerFrame(setFrameSize(w, h, c));
}

static Random random_random = new Random();

static int random(int n) {
  return n <= 0 ? 0 : random_random.nextInt(n);
}

static double random(double max) {
  return random()*max;
}

static double random() {
  return random_random.nextInt(100001)/100000.0;
}

static double random(double min, double max) {
  return min+random()*(max-min);
}

// min <= value < max
static int random(int min, int max) {
  return min+random(max-min);
}

static <A> A random(List<A> l) {
  return oneOf(l);
}

static <A> A random(Collection<A> c) {
  if (c instanceof List) return random((List<A>) c);
  int i = random(l(c));
  return collectionGet(c, i);
}
static JWindow showLoadingAnimation() {
  return showLoadingAnimation("Hold on user...");
}

static JWindow showLoadingAnimation(String text) {
  return showAnimationInTopRightCorner("#1003543", text);
}
static long now_virtualTime;
static long now() {
  return now_virtualTime != 0 ? now_virtualTime : System.currentTimeMillis();
}

//please include function withMargin.

static int showForm_defaultGap = 4;

static JPanel showFormTitled(final String title, final Object... _parts) {
  return swing(new F0<JPanel>() { JPanel get() { try { 
    final Var<JFrame> frame = new Var();
    JPanel panel = showForm_makePanel(frame, _parts);
    frame.set(handleEscapeKey(minFrameWidth(showPackedFrame(title, withMargin(panel)), 400)));
    return panel;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "final new Var<JFrame> frame;\r\n    JPanel panel = showForm_makePanel(frame, _p..."; }});
}

static JPanel showForm_makePanel(final Var<JFrame> frame, Object... _parts) {
  List<List> l = new ArrayList();
  List parts = asList(_parts);
  Runnable submit = null;
  for (int i = 0; i < l(parts); i++) {
    final Object o = parts.get(i), next = get(parts, i+1);
    if (o instanceof Component || o instanceof String || next instanceof Component) { // smartAdd accepts strings
      l.add(ll(o == null ? new JPanel() : o, next));
      if (next instanceof JButton && submit == null)
        submit = new Runnable() { public void run() { try {  ((JButton) next).doClick() ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "((JButton) next).doClick()"; }};
      i++;
    } else if (isRunnable(o))
      l.add(ll(null, jbutton(showFormSubmitButtonName(), submit = new Runnable() { public void run() { try { 
        Object result = call(o);
        if (neq(Boolean.FALSE, result))
          disposeFrame(frame.get());
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "Object result = call(o);\r\n        if (neq(Boolean.FALSE, result))\r\n          ..."; }})));
    else print("showForm: Unknown element type: " + getClassName(o));
  }
  JPanel panel = hvgrid((List) l, showForm_defaultGap);
  if (submit != null)
    for (JTextField textField : childrenOfType(panel, JTextField.class))
      onEnter(textField, submit);
  return panel;
}
static void useDBOf(String progID) {
  setDBProgramID(progID);
}
static Thread startThread(Object runnable) {
  return startThread(defaultThreadName(), runnable);
}

static Thread startThread(String name, Object runnable) {
  return startThread(newThread(toRunnable(runnable), name));
}

static Thread startThread(Thread t) {
  
  _registerThread(t);
  
  t.start();
  return t;
}
static double autoRestart_interval = 10;
static boolean autoRestart_on, autoRestart_debug, autoRestart_simulate;
static java.util.Timer autoRestart_timer;

static void autoRestart(double interval) {
  autoRestart_interval = interval;
  autoRestart();
}

static void autoRestart() {
  if (autoRestart_on) return;
  autoRestart_on = true;
  autoRestart_schedule();
  preloadProgramTitle();
}

static void autoRestart_off() {
  if (!autoRestart_on) return;
  stopTimer(autoRestart_timer);
  autoRestart_timer = null;
}

static void autoRestart_schedule() {
  autoRestart_timer = doLater_daemon(toMS(autoRestart_interval), "autoRestart_check");
}

static void autoRestart_check() {
  try {
    String newMD5 = loadPageSilently("http://botcompany.de/1010693/raw?id=" + psI(programID()));
    if (!isMD5(newMD5)) { if (autoRestart_debug) print("autoRestart: no server transpilation"); return; }
    if (autoRestart_localMD5 == null)
      autoRestart_localMD5 = md5(loadCachedTranspilation(programID()));

    String localMD5 = autoRestart_localMD5();
    if (neq(localMD5, newMD5)) {
      if (autoRestart_simulate)
        print("Would upgrade now. " + localMD5 + " -> " + newMD5);
      else {
        infoBox("Upgrading " + programTitle());
        restartWithDelay(1000);
        sleep();
      }
    }
  } finally {
    if (autoRestart_debug) print("autoRestart: Done");
    autoRestart_schedule();
  }
}

static RuntimeException rethrow(Throwable t) {
  
  if (t instanceof Error)
    _handleError((Error) t);
  
  throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
}
static void addToWindowNorth(Component c, Component toAdd) {
  addToWindowTop(c, toAdd);
}
static void substance() {
  substanceLAF();
}

static void substance(String skinName) {
  substanceLAF(skinName);
}
static Runnable runnableThread(final Runnable r) {
  return new Runnable() { public void run() { try {  new Thread(r).start() ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "new Thread(r).start()"; }};
}
static Map<Thread, Boolean> _registerThread_threads = newWeakHashMap();

static Thread _registerThread(Thread t) {
  _registerThread_threads.put(t, true);
  return t;
}

static void _registerThread() { _registerThread(Thread.currentThread()); }
static boolean nempty(Collection c) {
  return !isEmpty(c);
}

static boolean nempty(CharSequence s) {
  return !isEmpty(s);
}

static boolean nempty(Object[] o) {
  return !isEmpty(o);
}

static boolean nempty(Map m) {
  return !isEmpty(m);
}

static boolean nempty(Iterator i) {
  return i != null && i.hasNext();
}
static boolean match(String pat, String s) {
  return match3(pat, s);
}

static boolean match(String pat, String s, Matches matches) {
  return match3(pat, s, matches);
}

// makeNewComponent: func(Component) -> Component
//                   or Component
// returns new component
static Component awtReplaceComponent(final Component c, final Object makeNewComponent) {
  if (c == null) return null;
  return (Component) swing(new F0<Object>() { Object get() { try {  
    Container parent = c.getParent();
    if (parent == null) return null;
    Component[] l = parent.getComponents();
    LayoutManager layout = parent.getLayout();
    if (!(layout instanceof BorderLayout || layout instanceof ViewportLayout))
      warn("awtReplaceComponent only tested for BorderLayout/ViewportLayout. Have: " + layout);
    int idx = indexOf(l, c);
    if (idx < 0) throw fail("component not found in parent");
    //print("idx: " + idx);
    //print("Parent: " + parent);
    Object constraints = callOpt(layout, "getConstraints", c);
    //print("Constraints: " + constraints);
    
    // remove
    parent.remove(c);
  
    // add new component  
    Component newComponent = (Component) (makeNewComponent instanceof Component ? makeNewComponent :  callF(makeNewComponent, c));
    parent.add(newComponent, constraints, idx);
    validateFrame(parent);
    return newComponent;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "Container parent = c.getParent();\r\n    if (parent == null) null;\r\n    Compone..."; }});
}
static String getTextTrim(JTextComponent c) {
  return trim(getText(c));
}

// tested for editable combo box - returns the contents of text field
static String getTextTrim(JComboBox cb) {
  return trim(getText(cb));
}

static String getTextTrim(JComponent c) {
  if (c instanceof JLabel) return trim(((JLabel) c).getText());
  if (c instanceof JComboBox) return getTextTrim((JComboBox) c);
  return getTextTrim((JTextComponent) c);
}
// can probably be merged with next variant
static void disposeWindow(final Window window) {
  if (window != null) { swing(new Runnable() { public void run() { try { 
    window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING)); // call listeners
    window.dispose();
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING)); //..."; }}); }
}

static void disposeWindow(final Component c) {
  disposeWindow(getWindow(c));
}

static void disposeWindow(Object o) {
  if (o != null) disposeWindow((Component) o);
}
static boolean structure_showTiming, structure_checkTokenCount;

static String structure(Object o) {
  return structure(o, new structure_Data());
}

static String structure(Object o, structure_Data d) {
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

static void structure_go(Object o, structure_Data d) {
  structure_1(o, d);
  while (nempty(d.stack))
    popLast(d.stack).run();
}

static void structureToPrintWriter(Object o, PrintWriter out) {
  structure_Data d = new structure_Data();
  d.out = out;
  structure_go(o, d);
}

// leave to false, unless unstructure() breaks
static boolean structure_allowShortening = false;

static class structure_Data {
  PrintWriter out;
  int stringSizeLimit;
  int shareStringsLongerThan = 20;
  boolean noStringSharing;

  IdentityHashMap<Object,Integer> seen = new IdentityHashMap();
  //new BitSet refd;
  HashMap<String,Integer> strings = new HashMap();
  HashSet<String> concepts = new HashSet();
  HashMap<Class, List<Field>> fieldsByClass = new HashMap();
  int n; // token count
  List<Runnable> stack = new ArrayList();
  
  // append single token
  structure_Data append(String token) { out.print(token); ++n; return this; }
  structure_Data append(int i) { out.print(i); ++n; return this; }
  
  // append multiple tokens
  structure_Data append(String token, int tokCount) { out.print(token); n += tokCount; return this; }
  
  // extend last token
  structure_Data app(String token) { out.print(token); return this; }
  structure_Data app(int i) { out.print(i); return this; }
}

static void structure_1(final Object o, final structure_Data d) {
  if (o == null) { d.append("null"); return; }
  
  Class c = o.getClass();
  boolean concept = false;
  
    concept = o instanceof Concept;
  
  List<Field> lFields = d.fieldsByClass.get(c);
  
  if (lFields == null) {
    // these are never back-referenced (for readability)
    
    if (o instanceof Number) {
      PrintWriter out = d.out;
if (o instanceof Integer) { int i = ((Integer) o).intValue(); out.print(i); d.n += i < 0 ? 2 : 1; return; }
      if (o instanceof Long) { long l = ((Long) o).longValue(); out.print(l); out.print("L"); d.n += l < 0 ? 2 : 1; return; }
      if (o instanceof Short) { short s = ((Short) o).shortValue(); d.append("sh ", 2); out.print(s); d.n += s < 0 ? 2 : 1; return; }
      if (o instanceof Float) { d.append("fl ", 2); quoteToPrintWriter(str(o), out); return; }
      if (o instanceof Double) { d.append("d(", 3); quoteToPrintWriter(str(o), out); d.append(")"); return; }
      if (o instanceof BigInteger) { out.print("bigint("); out.print(o); out.print(")"); d.n += ((BigInteger) o).signum() < 0 ? 5 : 4; return; }
    }
  
    if (o instanceof Boolean) {
      d.append(((Boolean) o).booleanValue() ? "t" : "f"); return;
    }
      
    if (o instanceof Character) {
      d.append(quoteCharacter((Character) o)); return;
    }
      
    if (o instanceof File) {
      d.append("File ").append(quote(((File) o).getPath())); return;
    }
      
    // referencable objects follow
    
    Integer ref = d.seen.get(o);
    if (o instanceof String && ref == null) ref = d.strings.get((String) o);
    if (ref != null) { /*d.refd.set(ref);*/ d.append("t").app(ref); return; }

    if (!(o instanceof String))
      d.seen.put(o, d.n); // record token number
    else {
      String s = d.stringSizeLimit != 0 ? shorten((String) o, d.stringSizeLimit) : (String) o;
      if (!d.noStringSharing) {
        if (d.shareStringsLongerThan == Integer.MAX_VALUE)
          d.seen.put(o, d.n);
        if (l(s) >= d.shareStringsLongerThan)
          d.strings.put(s, d.n);
      }
      quoteToPrintWriter(s, d.out); d.n++; return;
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
    if (o instanceof Collection
      && !startsWith(name, "main$")
      /* && neq(name, "main$Concept$RefL") */) {
      d.append("[");
      final int l = d.n;
      final Iterator it = ((Collection) o).iterator();
      d.stack.add(new Runnable() { public void run() { try { 
        if (!it.hasNext())
          d.append("]");
        else {
          d.stack.add(this);
          if (d.n != l) d.append(", ");
          structure_1(it.next(), d);
        }
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (!it.hasNext())\r\n          d.append(\"]\");\r\n        else {\r\n          d.sta..."; }});
      return;
    }
    
    if (o instanceof Map && !startsWith(name, "main$")) {
      if (o instanceof LinkedHashMap) d.append("lhm");
      else if (o instanceof HashMap) d.append("hm");
      d.append("{");
      final int l = d.n;
      final Iterator it = ((Map) o).entrySet().iterator();
      
      d.stack.add(new Runnable() {
        boolean v;
        Map.Entry e;
        
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
              if (d.n != l) d.append(", ");
              structure_1(e.getKey(), d);
            }
          }
        }
      });
      return;
    }
    
    if (c.isArray()) {
      if (o instanceof byte[]) {
        d.append("ba ").append(quote(bytesToHex((byte[]) o))); return;
      }
  
      final int n = Array.getLength(o);
  
      if (o instanceof boolean[]) {
        String hex = boolArrayToHex((boolean[]) o);
        int i = l(hex);
        while (i > 0 && hex.charAt(i-1) == '0' && hex.charAt(i-2) == '0') i -= 2;
        d.append("boolarray ").append(n).app(" ").append(quote(substring(hex, 0, i))); return;
      }
      
      String atype = "array", sep = ", ";
  
      if (o instanceof int[]) {
        //ret "intarray " + quote(intArrayToHex((int[]) o));
        atype = "intarray";
        sep = " ";
      }
      
      d.append(atype).append("{");
      d.stack.add(new Runnable() {
        int i;
        public void run() {
          if (i >= n)
            d.append("}");
          else {
            d.stack.add(this);
            if (i > 0) d.append(", ");
            structure_1(Array.get(o, i++), d);
          }
        }
      });
      return;
    }
  
    if (o instanceof Class) {
      d.append("class(", 2).append(quote(((Class) o).getName())).append(")"); return;
    }
      
    if (o instanceof Throwable) {
      d.append("exception(", 2).append(quote(((Throwable) o).getMessage())).append(")"); return;
    }
      
    if (o instanceof BitSet) {
      BitSet bs = (BitSet) o;
      d.append("bitset{", 2);
      int l = d.n;
      for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
        if (d.n != l) d.append(", ");
        d.append(i);
      }
      d.append("}"); return;
    }
      
    // Need more cases? This should cover all library classes...
    if (name.startsWith("java.") || name.startsWith("javax.")) {
      d.append("j ").append(quote(str(o))); return; // Hm. this is not unstructure-able
    }
    
    
      if (o instanceof Lisp) {
        d.append("l(", 2);
        final Lisp lisp = (Lisp) ( o);
        structure_1(lisp.head, d);
        final Iterator it = lisp.args == null ? emptyIterator() : lisp.args.iterator();
        d.stack.add(new Runnable() { public void run() { try { 
          if (!it.hasNext())
            d.append(")");
          else {
            d.stack.add(this);
            d.append(", ");
            structure_1(it.next(), d);
          }
        
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (!it.hasNext())\r\n            d.append(\")\");\r\n          else {\r\n           ..."; }});
        return;
      }
    
      
    /*if (name.equals("main$Lisp")) {
      fail("lisp not supported right now");
    }*/
    
    String dynName = shortDynamicClassName(o);
    if (concept && !d.concepts.contains(dynName)) {
      d.concepts.add(dynName);
      d.append("c ");
    }
    
    // serialize an object with fields.
    // first, collect all fields and values in fv.
    
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
        
        // put special cases here...
      }
        
      cc = cc.getSuperclass();
    }
    
    lFields = asList(fields);
    
    // Render this$1 first because unstructure needs it for constructor call.
    
    for (int i = 0; i < l(lFields); i++) {
      Field f = lFields.get(i);
      if (f.getName().equals("this$1")) {
        lFields.remove(i);
        lFields.add(0, f);
        break;
      }
    }
  
    
    d.fieldsByClass.put(c, lFields);
  } // << if (lFields == null)
  else { // ref handling for lFields != null
    Integer ref = d.seen.get(o);
    if (ref != null) { /*d.refd.set(ref);*/ d.append("t").app(ref); return; }
    d.seen.put(o, d.n); // record token number
  }

  LinkedHashMap<String,Object> fv = new LinkedHashMap();
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
    
  // Now we have fields & values. Process fieldValues if it's a DynamicObject.
  
  // omit field "className" if equal to class's name
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
  
  d.stack.add(new Runnable() { public void run() { try { 
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
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (!it.hasNext()) {\r\n      if (d.n != l)\r\n        d.append(\")\");\r\n    } else..."; }});
}

// action: voidfunc(int row)
static void tablePopupMenuItem(final JTable table, final String name, final Object action) {
  tablePopupMenu(table, new Object() { void get(JPopupMenu menu, final int row) { try { 
    addMenuItem(menu, name, new Runnable() { public void run() { try {  pcallF(action, row) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "pcallF(action, row)"; }});
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "addMenuItem(menu, name, r { pcallF(action, row) });"; }});
}
static Object swing(Object f) {
  return swingAndWait(f);
}

static <A> A swing(F0<A> f) {
  return (A) swingAndWait(f);
}
static Thread runInNewThread_awt(final String progID) {
  return startThread(progID, new Runnable() { public void run() { try { 
    try {
      callMainAsChild(hotwire(progID));
    } catch (Throwable __e) { messageBox(__e); }
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "pcall-messagebox {\r\n      callMainAsChild(hotwire(progID));\r\n    }"; }});
}
static volatile StringBuffer local_log = new StringBuffer(); // not redirected
static volatile StringBuffer print_log = local_log; // might be redirected, e.g. to main bot

// in bytes - will cut to half that
static volatile int print_log_max = 1024*1024;
static volatile int local_log_max = 100*1024;
//static int print_maxLineLength = 0; // 0 = unset

static boolean print_silent; // total mute if set

static Object print_byThread_lock = new Object();
static volatile ThreadLocal<Object> print_byThread; // special handling by thread - prefers F1<S, Bool>
static volatile Object print_allThreads;

static void print() {
  print("");
}

// slightly overblown signature to return original object...
static <A> A print(A o) {
  ping();
  if (print_silent) return o;
  String s = String.valueOf(o) + "\n";
  print_noNewLine(s);
  return o;
}

static void print_noNewLine(String s) {
  
  Object f = print_byThread == null ? null : print_byThread.get();
  if (f == null) f = print_allThreads;
  if (f != null)
    if (isFalse(f instanceof F1 ? ((F1) f).get(s) : callF(f, s))) return;
  

  print_raw(s);
}

static void print_raw(String s) {
  s = fixNewLines(s);
  // TODO if (print_maxLineLength != 0)
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

static void print(long l) {
  print(String.valueOf(l));
}

static void print(char c) {
  print(String.valueOf(c));
}

static void print_append(StringBuffer buf, String s, int max) {
  synchronized(buf) {
    buf.append(s);
    max /= 2;
    if (buf.length() > max) try {
      int newLength = max/2;
      int ofs = buf.length()-newLength;
      String newString = buf.substring(ofs);
      buf.setLength(0);
      buf.append("[...] ").append(newString);
    } catch (Exception e) {
      buf.setLength(0);
    }
  }
}
static Concept cnew(String name, Object... values) {
  Class<? extends Concept> cc = findClass(name);
  Concept c = cc != null ? nuObject(cc) : new Concept(name);
  csetAll(c, values);
  return c;
}

static Concept cnew(Concepts concepts, String name, Object... values) {
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

static <A extends Concept> A cnew(Class<A> cc, Object... values) {
  A c = nuObject(cc);
  csetAll(c, values);
  return c;
}

static <A extends Concept> A cnew(Concepts concepts, Class<A> cc, Object... values) {
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

static Throwable printStackTrace2(Throwable e) {
  // we go to system.out now - system.err is nonsense
  print(getStackTrace2(e));
  return e;
}

static void printStackTrace2() {
  printStackTrace2(new Throwable());
}

static void printStackTrace2(String msg) {
  printStackTrace2(new Throwable(msg));
}

/*static void printStackTrace2(S indent, Throwable e) {
  if (endsWithLetter(indent)) indent += " ";
  printIndent(indent, getStackTrace2(e));
}*/


static String copyTextToClipboard(String text) {
  StringSelection selection = new StringSelection(text);
  Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
  return text;
}
static void db() {
  conceptsAndBot();
}
static <A> List<A> reversed(Collection<A> l) {
  return reversedList(l);
}

static <A> List<A> reversed(A[] l) {
  return reversedList(asList(l));
}

static String reversed(String s) {
  return reversedString(s);
}
static Object first(Object list) {
  return empty((List) list) ? null : ((List) list).get(0);
}

static <A> A first(List<A> list) {
  return empty(list) ? null : list.get(0);
}

static <A> A first(A[] bla) {
  return bla == null || bla.length == 0 ? null : bla[0];
}

static <A> A first(Iterable<A> i) {
  if (i == null) return null;
  Iterator<A> it = i.iterator();
  return it.hasNext() ? it.next() : null;
}

static Character first(String s) { return empty(s) ? null : s.charAt(0); }


static <A, B> A first(Pair<A, B> p) {
  return p == null ? null : p.a;
}

static boolean setText_opt = true; // optimize by calling getText first

static <A extends JTextComponent> A setText(A c, Object text) {
  setText((Object) c, text);
  return c;
}

static <A extends JComboBox> A setText(final A c, Object text) {
  // only for editable combo boxes at this point
  final String s = strUnnull(text);
  { swing(new Runnable() { public void run() { try { 
    c.getEditor().setItem(s);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "c.getEditor().setItem(s);"; }}); }
  return c;
}

static void setText(JLabel c, Object text) {
  setText((Object) c, text);
}

static JButton setText(JButton c, Object text) {
  setText((Object) c, text);
  return c;
}

static <A> A setText(final A c, Object text) {
  if (c == null) return null;
  final String s = strUnnull(text);
  swingAndWait(new Runnable() { public void run() { try { 
    if (!setText_opt || neq(callOpt(c, "getText"), s))
      call(c, "setText", s);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (!setText_opt || neq(callOpt(c, \"getText\"), s))\r\n      call(c, \"setText\", s);"; }});
  return c;
}


static void awtCalcOnConceptChanges(JComponent component, int delay, final Object runnable, final boolean runOnFirstTime) {
  awtCalcOnConceptChanges(component, delay, 0, runnable, runOnFirstTime);
}

static void awtCalcOnConceptChanges(JComponent component, int delay, int firstDelay, final Object runnable, final boolean runOnFirstTime) {
  installTimer(component, delay, firstDelay, new Runnable() {
    long c = runOnFirstTime ? -1 : changeCount();
    SingleThread thread = new SingleThread();
    
    public void run() {
      long _c = changeCount();
      if (_c != c && !thread.running()) {
        c = _c;
        thread.go(runnable);
      }
    }
  });
}
static Font typeWriterFont() {
  return typeWriterFont(14);
}
  
static Font typeWriterFont(int size) {
  return new Font("Courier", Font.PLAIN, size);
}
static JFrame handleEscapeKey(final JFrame frame) {
  KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
  frame.getRootPane().registerKeyboardAction(new ActionListener() {
    public void actionPerformed(ActionEvent actionEvent) {
      frame.dispose();
    }
  }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
  return frame;
}

static Object[] asArray(List l) {
  return toObjectArray(l);
}

static <A> A[] asArray(Class<A> type, List l) {
  return (A[]) l.toArray((Object[]) Array.newInstance(type, l.size()));
}
static String uploadToImageServerIfNotThere(BufferedImage img, String name) {
  String md5 = md5OfRGBImage(new RGBImage(img));
  long id = imageServerCheckMD5(md5);
  if (id == 0)
    return uploadToImageServer_new(img, name);
  else
    return "http://ai1.lol/images/raw/" + id;
}
static WeakHashMap<JFrame,Boolean> makeFrame_myFrames = new WeakHashMap();

static JFrame makeFrame() {
  return makeFrame((Component) null);
}

static JFrame makeFrame(Object content) {
  return makeFrame(programTitle(), content);
}

static JFrame makeFrame(String title) {
  return makeFrame(title, null);
}

static JFrame makeFrame(String title, Object content) {
  return makeFrame(title, content, true);
}

static JFrame makeFrame(final String title, final Object content, final boolean showIt) {
  return swing(new F0<JFrame>() { JFrame get() { try { 
    if (getFrame(content) != null)
      return getFrame(setFrameTitle((Component) content, title));
    final JFrame frame = new JFrame(title);
    makeFrame_myFrames.put(frame, Boolean.TRUE);
    JComponent wrapped = wrap(content);
    if (wrapped != null)
      frame.getContentPane().add(wrapped);
    frame.setBounds(defaultNewFrameBounds());
    if (showIt)
      frame.setVisible(true);
    //callOpt(content, "requestFocus");
    //exitOnFrameClose(frame);
    
    standardTitlePopupMenu(frame);
    return frame;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "if (getFrame(content) != null)\r\n      ret getFrame(setFrameTitle((Component) ..."; }});
}
static WeakHashMap<Class, ArrayList<Method>> callF_cache = new WeakHashMap();


  static <A> A callF(F0<A> f) {
    return f == null ? null : f.get();
  }



  static <A, B> B callF(F1<A, B> f, A a) {
    return f == null ? null : f.get(a);
  }


static Object callF(Object f, Object... args) { try {
  if (f instanceof String)
    return callMC((String) f, args);
  if (f instanceof Runnable) {
    ((Runnable) f).run();
    return null;
  }
  if (f == null) return null;
  
  Class c = f.getClass();
  ArrayList<Method> methods;
  synchronized(callF_cache) {
    methods = callF_cache.get(c);
    if (methods == null)
      methods = callF_makeCache(c);
  }
  
  int n = l(methods);
  if (n == 0) throw fail("No get method in " + getClassName(c));
  if (n == 1) return invokeMethod(methods.get(0), f, args);
  for (int i = 0; i < n; i++) {
    Method m = methods.get(i);
    if (call_checkArgs(m, args, false))
      return invokeMethod(m, f, args);
  }
  throw fail("No matching get method in " + getClassName(c));
} catch (Exception __e) { throw rethrow(__e); } }

// used internally
static ArrayList<Method> callF_makeCache(Class c) {
  ArrayList<Method> l = new ArrayList();
  Class _c = c;
  do {
    for (Method m : _c.getDeclaredMethods())
      if (m.getName().equals("get")) {
        m.setAccessible(true);
        l.add(m);
      }
    if (!l.isEmpty()) break;
    _c = _c.getSuperclass();
  } while (_c != null);
  callF_cache.put(c, l);
  return l;
}
static <A, B> Map<A, B> newWeakHashMap() {
  return _registerWeakMap(synchroMap(new WeakHashMap()));
}
static <A> ArrayList<A> asList(A[] a) {
  return a == null ? new ArrayList<A>() : new ArrayList<A>(Arrays.asList(a));
}

static ArrayList<Integer> asList(int[] a) {
  ArrayList<Integer> l = new ArrayList();
  for (int i : a) l.add(i);
  return l;
}

static <A> ArrayList<A> asList(Iterable<A> s) {
  if (s instanceof ArrayList) return (ArrayList) s;
  ArrayList l = new ArrayList();
  if (s != null)
    for (A a : s)
      l.add(a);
  return l;
}

static <A> ArrayList<A> asList(Enumeration<A> e) {
  ArrayList l = new ArrayList();
  if (e != null)
    while (e.hasMoreElements())
      l.add(e.nextElement());
  return l;
}
static boolean isInstance(Class type, Object arg) {
  return type.isInstance(arg);
}
static void disposeFrame(final Component c) {
  disposeWindow(c);
}
public static boolean isWindows() {
  return System.getProperty("os.name").contains("Windows");
}
static String quoteCharacter(char c) {
  if (c == '\'') return "'\\''";
  if (c == '\\') return "'\\\\'";
  if (c == '\r') return "'\\r'";
  if (c == '\n') return "'\\n'";
  if (c == '\t') return "'\\t'";
  return "'" + c + "'";
}

static JTextArea wrappedTextArea(JTextArea ta) {
  ta.setLineWrap(true);
  ta.setWrapStyleWord(true);
  return ta;
}

static JTextArea wrappedTextArea() {
  return wrappedTextArea(jtextarea());
}

static JTextArea wrappedTextArea(String text) {
  JTextArea ta = wrappedTextArea();
  ta.setText(text);
  return ta;
}
static JTextField jTextField() {
  return jTextField("");
}

static JTextField jTextField(final String text) {
  return swing(new F0<JTextField>() { JTextField get() { try { 
    JTextField tf = new JTextField(unnull(text));
    jenableUndoRedo(tf);
    tf.selectAll();
    return tf;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "JTextField tf = new JTextField(unnull(text));\r\n    jenableUndoRedo(tf);\r\n    ..."; }});
}

static JTextField jTextField(Object o) {
  return jTextField(strOrEmpty(o));
}

static String shortDynamicClassName(Object o) {
 if (o instanceof DynamicObject && ((DynamicObject) o).className != null)
    return ((DynamicObject) o).className;
  return shortClassName(o);
}
static boolean infoMessage_alwaysOnTop = true;
static double infoMessage_defaultTime = 5.0;

// automatically switches to AWT thread for you
static JWindow infoMessage(String text) {
  return infoMessage(text, infoMessage_defaultTime);
}

static JWindow infoMessage(final String text, final double seconds) {
  print(text);
  return infoMessage_noprint(text, seconds);
}

static JWindow infoMessage_noprint(String text) {
  return infoMessage_noprint(text, infoMessage_defaultTime);
}

static JWindow infoMessage_noprint(final String text, final double seconds) {
  logQuotedWithDate(infoBoxesLogFile(), text); 
  if (isHeadless()) return null;
  return (JWindow) swingAndWait(new F0<Object>() { Object get() { try { 
    final JWindow window = showWindow(infoMessage_makePanel(text));
    window.setSize(300, 150);
    moveToTopRightCorner(window);
    if (infoMessage_alwaysOnTop)
      window.setAlwaysOnTop(true);
    window.setVisible(true);
    disposeWindowAfter(iround(seconds*1000), window);
    return window;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "final JWindow window = showWindow(infoMessage_makePanel(text));\r\n    window.s..."; }});
}

static JWindow infoMessage(Throwable e) {
  showConsole();
  printStackTrace(e);
  return infoMessage(exceptionToStringShort(e));
}
static String getText(final AbstractButton c) {
  return c == null ? "" : (String) swingAndWait(new F0<Object>() { Object get() { try { return  c.getText() ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "c.getText()"; }});
}

static String getText(final JTextComponent c) {
  return c == null ? "" : (String) swingAndWait(new F0<Object>() { Object get() { try { return  c.getText() ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "c.getText()"; }});
}

static String getText(final JLabel l) {
  return l == null ? "" : (String) swingAndWait(new F0<Object>() { Object get() { try { return  l.getText() ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "l.getText()"; }});
}

// returns the contents of text field for editable combo box
static String getText(final JComboBox cb) {
  if (cb.isEditable())
    return unnull((String) cb.getEditor().getItem());
  else
    return str(cb.getSelectedItem());
}
static byte[] hexToBytes(String s) {
  int n = l(s) / 2;
  byte[] bytes = new byte[n];
  for (int i = 0; i < n; i++) {
    String hex = substring(s, i*2, i*2+2);
    try {
      bytes[i] = (byte) parseHexByte(hex);
    } catch (Throwable _e) {
      throw fail("Bad hex byte: " + quote(hex) + " at " + i*2 + "/" + l(s));
    }
  }
  return bytes;
}
static void setDBProgramID(String progID) {
  getDBProgramID_id = progID;
}
static String str(Object o) {
  return o == null ? "null" : o.toString();
}

static String str(char[] c) {
  return new String(c);
}
static JMenuBar addMenuBar(JFrame f) {
  if (f == null) return null;
  JMenuBar bar = f.getJMenuBar();
  if (bar == null) {
    f.setJMenuBar(bar = new JMenuBar());
    revalidateFrame(f);
  }
  return bar;
}

static JMenuBar addMenuBar(Component c) {
  return addMenuBar(getFrame(c));
}
static HashMap<Class,Constructor> nuEmptyObject_cache = new HashMap();

static <A> A nuEmptyObject(Class<A> c) { try {
  Constructor ctr;
  
  synchronized(nuEmptyObject_cache) {
    ctr = nuEmptyObject_cache.get(c);
    if (ctr == null) {
      nuEmptyObject_cache.put(c, ctr = nuEmptyObject_findConstructor(c));
      ctr.setAccessible(true);
    }
  }

  return (A) ctr.newInstance();
} catch (Exception __e) { throw rethrow(__e); } }

static Constructor nuEmptyObject_findConstructor(Class c) {
  for (Constructor m : c.getDeclaredConstructors())
    if (m.getParameterTypes().length == 0)
      return m;
  throw fail("No default constructor declared in " + c.getName());
}

static void fillTableWithData(final JTable table, List<List> rows, List<String> colNames) {
  fillTableWithData(table, rows, toStringArray(colNames));
}

// thread-safe
static void fillTableWithData(final JTable table, List<List> rows, String... colNames) {
  final DefaultTableModel model = fillTableWithData_makeModel(rows, colNames);
  
  swingNowOrLater(new Runnable() { public void run() { try { 
    setTableModel(table, model);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "setTableModel(table, model);"; }});
}

static DefaultTableModel fillTableWithData_makeModel(List<List> rows, String... colNames) {
  Object[][] data = new Object[rows.size()][];
  int w = 0;
  for (int i = 0; i < rows.size(); i++) {
    List l = rows.get(i);
    Object[] r = new Object[l.size()];
    for (int j = 0; j < l.size(); j++) {
      Object o = l.get(j);
      if (o instanceof BufferedImage) o = imageIcon((BufferedImage) o);
      
        if (o instanceof RGBImage) o = imageIcon((RGBImage) o);
      
      r[j] = o;
    }
    data[i] = r;
    w = Math.max(w, l.size());
  }
  Object[] columnNames = new Object[w];
  for (int i = 0; i < w; i++)
    columnNames[i] = i < l(colNames) ? colNames[i] : "?";
  return new DefaultTableModel(data, columnNames) {
    public Class getColumnClass(int column) {
      return or(_getClass(getValueAt(0, column)), String.class);
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return false;
    }
  };
}

static void setOptAll_pcall(Object o, Map<String, Object> fields) {
  if (fields == null) return;
  for (String field : keys(fields))
    try { setOpt(o, field, fields.get(field)); } catch (Throwable __e) { print(exceptionToStringShort(__e)); }
}

static void setOptAll_pcall(Object o, Object... values) {
  //values = expandParams(c.getClass(), values);
  warnIfOddCount(values);
  for (int i = 0; i+1 < l(values); i += 2) {
    String field = (String) values[i];
    Object value = values[i+1];
    try { setOpt(o, field, value); } catch (Throwable __e) { print(exceptionToStringShort(__e)); }
  }
}
static String getType(Object o) {
  return getClassName(o);
}
static boolean setOptAllDyn_debug;

static void setOptAllDyn(DynamicObject o, Map<String, Object> fields) {
  if (fields == null) return;
  for (String field : keys(fields)) {
    Object val = fields.get(field);
    boolean has = hasField(o, field);
    if (has)
      setOpt(o, field, val);
    else {
      o.fieldValues.put(intern(field), val);
      if (setOptAllDyn_debug) print("setOptAllDyn added dyn " + field + " to " + o + " [value: " + val + ", fieldValues = " + systemHashCode(o.fieldValues) + ", " + struct(keys(o.fieldValues)) + "]");
    }
  }
}
static boolean isRunnable(Object o) {
  return o instanceof Runnable || hasMethod(o, "get");
}
static String fixNewLines(String s) {
  return s.replace("\r\n", "\n").replace("\r", "\n");
}
// runnable = Runnable or String (method name)
static Thread newThread(Object runnable) {
  return new Thread(_topLevelErrorHandling(toRunnable(runnable)));
}

static Thread newThread(Object runnable, String name) {
  if (name == null) name = defaultThreadName();
  return new Thread(_topLevelErrorHandling(toRunnable(runnable)), name);
}

static Thread newThread(String name, Object runnable) {
  return newThread(runnable, name);
}
static void setTableModel(JTable table, TableModel model) {
  Map<String, Integer> widths = tableColumnWidthsByName(table);
  int[] i = table.getSelectedRows();
  table.setModel(model);
  int n = model.getRowCount();
  ListSelectionModel sel = table.getSelectionModel();
  for (int j = 0; j < i.length; j++)
    if (i[j] < n)
      sel.addSelectionInterval(i[j], i[j]);
  tableSetColumnPreferredWidths(table, widths);
}

static void deleteConcept(long id) {
  mainConcepts.deleteConcept(id);
}

static void deleteConcept(Concept c) {
  if (c != null) c.delete();
}

static void deleteConcept(Concept.Ref ref) {
  if (ref != null) deleteConcept(ref.get());
}

// f modifies original content pane
// f: Component -> Component
static JSplitPane addToWindowSplitRight_f(Component c, Component toAdd, Object f) {
  JFrame frame = getFrame(c);
  JSplitPane sp;
  setContentPane(frame, sp = jhsplit((Component) callF(f, frame.getContentPane()), toAdd));
  return sp;
}
static <A> List<A> childrenOfType(Component c, Class<A> theClass) {
  List<A> l = new ArrayList();
  scanForComponents(c, theClass, l);
  return l;
}
static volatile boolean sleep_noSleep;

static void sleep(long ms) {
  ping();
  if (ms < 0) return;
  // allow spin locks
  if (isAWTThread() && ms > 100) throw fail("Should not sleep on AWT thread");
  try {
    Thread.sleep(ms);
  } catch (Exception e) { throw new RuntimeException(e); }
}

static void sleep() { try {
  if (sleep_noSleep) throw fail("nosleep");
  print("Sleeping.");
  sleepQuietly();
} catch (Exception __e) { throw rethrow(__e); } }
static boolean structure_isMarker(String s, int i, int j) {
  if (i >= j) return false;
  if (s.charAt(i) != 'm') return false;
  ++i;
  while (i < j) {
    char c = s.charAt(i);
    if (c < '0' || c > '9') return false;
    ++i;
  }
  return true;
}
// undefined color, seems to be all black in practice
static BufferedImage newBufferedImage(int w, int h) {
  return new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
}

static BufferedImage newBufferedImage(int w, int h, Color color) {
  BufferedImage img = newBufferedImage(w, h);
  Graphics2D g = img.createGraphics();
  g.setColor(color);
  g.fillRect(0, 0, w, h);
  return img;
}


static BufferedImage newBufferedImage(Pt p, Color color) {
  return newBufferedImage(p.x, p.y, color);
}

// It's the super precise version!
static double roundToOneHundredth(double d) {
  return new BigDecimal(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
}
static <A> List<A> reversedList(Collection<A> l) {
  List<A> x = cloneList(l);
  Collections.reverse(x);
  return x;
}
static <A> List<A> subList(List<A> l, int startIndex) {
  return subList(l, startIndex, l(l));
}

static <A> List<A> subList(List<A> l, int startIndex, int endIndex) {
  startIndex = max(0, min(l(l), startIndex));
  endIndex = max(0, min(l(l), endIndex));
  if (startIndex > endIndex) return litlist();
  return l.subList(startIndex, endIndex);
}


static String intern(String s) {
  return fastIntern(s);
}
static List collectField(Collection c, String field) {
  List l = new ArrayList();
  for (Object a : c)
    l.add(getOpt(a, field));
  return l;
}

static List collectField(String field, Collection c) {
  return collectField(c, field);
}
static int l(Object[] a) { return a == null ? 0 : a.length; }
static int l(boolean[] a) { return a == null ? 0 : a.length; }
static int l(byte[] a) { return a == null ? 0 : a.length; }
static int l(int[] a) { return a == null ? 0 : a.length; }
static int l(float[] a) { return a == null ? 0 : a.length; }
static int l(char[] a) { return a == null ? 0 : a.length; }
static int l(Collection c) { return c == null ? 0 : c.size(); }
static int l(Map m) { return m == null ? 0 : m.size(); }
static int l(CharSequence s) { return s == null ? 0 : s.length(); } static long l(File f) { return f == null ? 0 : f.length(); }

static int l(Object o) {
  return o == null ? 0
    : o instanceof String ? l((String) o)
    : o instanceof Map ? l((Map) o)
    : o instanceof Collection ? l((Collection) o)
    : (Integer) call(o, "size");
}




  static int l(Lisp l) { return l == null ? 0 : l.size(); }

static String[] toStringArray(Collection<String> c) {
  String[] a = new String[l(c)];
  Iterator<String> it = c.iterator();
  for (int i = 0; i < l(a); i++)
    a[i] = it.next();
  return a;
}

static String[] toStringArray(Object o) {
  if (o instanceof String[])
    return (String[]) o;
  else if (o instanceof Collection)
    return toStringArray((Collection<String>) o);
  else
    throw fail("Not a collection or array: " + getClassName(o));
}

static JFrame minFrameWidth(JFrame frame, int w) {
  if (frame != null && frame.getWidth() < w)
    frame.setSize(w, frame.getHeight());
  return frame;
}

static JFrame minFrameWidth(int w, JFrame frame) {
  return minFrameWidth(frame, w);
}

static String n(long l, String name) {
  return l + " " + trim(l == 1 ? singular(name) : getPlural(name));
}

static String n(Collection l, String name) {
  return n(l(l), name);
}

static String n(Map m, String name) {
  return n(l(m), name);
}

static String n(Object[] a, String name) {
  return n(l(a), name);
}


static Object call(Object o) {
  return callFunction(o);
}

// varargs assignment fixer for a single string array argument
static Object call(Object o, String method, String[] arg) {
  return call(o, method, new Object[] {arg});
}

static Object call(Object o, String method, Object... args) {
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

static Method call_findStaticMethod(Class c, String method, Object[] args, boolean debug) {
  Class _c = c;
  while (c != null) {
    for (Method m : c.getDeclaredMethods()) {
      if (debug)
        System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");;
      if (!m.getName().equals(method)) {
        if (debug) System.out.println("Method name mismatch: " + method);
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

static Method call_findMethod(Object o, String method, Object[] args, boolean debug) {
  Class c = o.getClass();
  while (c != null) {
    for (Method m : c.getDeclaredMethods()) {
      if (debug)
        System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");;
      if (m.getName().equals(method) && call_checkArgs(m, args, debug))
        return m;
    }
    c = c.getSuperclass();
  }
  throw new RuntimeException("Method '" + method + "' (non-static) with " + args.length + " parameter(s) not found in " + o.getClass().getName());
}

private static boolean call_checkArgs(Method m, Object[] args, boolean debug) {
  Class<?>[] types = m.getParameterTypes();
  if (types.length != args.length) {
    if (debug)
      System.out.println("Bad parameter length: " + args.length + " vs " + types.length);
    return false;
  }
  for (int i = 0; i < types.length; i++)
    if (!(args[i] == null || isInstanceX(types[i], args[i]))) {
      if (debug)
        System.out.println("Bad parameter " + i + ": " + args[i] + " vs " + types[i]);
      return false;
    }
  return true;
}
static HashMap<Class, Field[]> getDeclaredFields_cache = new HashMap();

static Field[] getDeclaredFields_cached(Class c) {
  Field[] fields;
  synchronized(getDeclaredFields_cache) {
    fields = getDeclaredFields_cache.get(c);
    if (fields == null) {
      getDeclaredFields_cache.put(c, fields = c.getDeclaredFields());
      for (Field f : fields)
        f.setAccessible(true);
    }
  }
  return fields;
}
static boolean isTrue(Object o) {
  if (o instanceof Boolean)
    return ((Boolean) o).booleanValue();
  if (o == null) return false;
  if (o instanceof ThreadLocal)
    return isTrue(((ThreadLocal) o).get());
  throw fail(getClassName(o));
}
static String reversedString(String s) {
  return reverseString(s);
}
static String boolArrayToHex(boolean[] a) {
  return bytesToHex(boolArrayToBytes(a));
}
static Iterator emptyIterator() {
  return Collections.emptyIterator();
}
static boolean match3(String pat, String s) {
  return match3(pat, s, null);
}

static boolean match3(String pat, String s, Matches matches) {
  if (pat == null || s == null) return false;
  return match3(pat, parse3_cached(s), matches);
}
  
static boolean match3(String pat, List<String> toks, Matches matches) {
  List<String> tokpat = parse3(pat);
  return match3(tokpat,toks,matches);
}

static boolean match3(List<String> tokpat, List<String> toks, Matches matches) {
  String[] m = match2(tokpat, toks);
  //print(structure(tokpat) + " on " + structure(toks) + " => " + structure(m));
  if (m == null) return false;
  if (matches != null) matches.m = m; return true;
}
// An "Android" is a program that accepts text questions (on console or TCP) and outputs one response text per question

static boolean makeAndroid3_disable; // disable all android making

static class Android3 {
  String greeting;
  boolean publicOverride; // optionally set this in client
  int startPort = 5000; // optionally set this in client
  Responder responder;
  boolean console = true;
  boolean quiet; // no messages on console
  boolean daemon = false;
  boolean incomingSilent = false;
  int incomingPrintLimit = 200;
  boolean useMultiPort = true;
  boolean recordHistory;
  boolean verbose;
  int answerPrintLimit = 500;
  boolean newLineAboveAnswer, newLineBelowAnswer;
  
  // set by system
  int port;
  long vport;
  DialogHandler handler;
  ServerSocket server;
  
  Android3(String greeting) {
  this.greeting = greeting;}
  Android3() {}
  
  synchronized void dispose() {
    if (server != null) {
      try {
        server.close();
      } catch (IOException e) {
        print("[internal] " + e);
      }
      server = null;
    }
    if (vport != 0) try {
      print("Disposing " + this);
      removeFromMultiPort(vport);
      vport = 0;
    } catch (Throwable __e) { printStackTrace2(__e); }
  }
  
  public String toString() { return "Bot: " + greeting + " [vport " + vport + "]"; }
}

static abstract class Responder {
  abstract String answer(String s, List<String> history);
}

static Android3 makeAndroid3(final String greeting) {
  return makeAndroid3(new Android3(greeting));
}

static Android3 makeAndroid3(final String greeting, Responder responder) {
  Android3 android = new Android3(greeting);
  android.responder = responder;
  return makeAndroid3(android);
}

static Android3 makeAndroid3(final Android3 a) {
  if (makeAndroid3_disable) return a;
  
  if (a.responder == null)
    a.responder = new Responder() {
      String answer(String s, List<String> history) {
        return callStaticAnswerMethod(s, history);
      }
    };
    
  if (!a.quiet)
    print("[bot] " + a.greeting);
  
  if (a.console && (readLine_noReadLine || makeAndroid3_consoleInUse()))
    a.console = false;
  
  record(a);
  
  if (a.useMultiPort)
    a.vport = addToMultiPort(a.greeting,
      makeAndroid3_verboseResponder(a));
      
  if (a.console)
    makeAndroid3_handleConsole(a);

  if (a.useMultiPort) return a;

  a.handler = makeAndroid3_makeDialogHandler(a);
  if (a.quiet) startDialogServer_quiet.set(true);
  try {
    a.port = a.daemon
      ? startDialogServerOnPortAboveDaemon(a.startPort, a.handler)
      : startDialogServerOnPortAbove(a.startPort, a.handler);
  } finally {
    startDialogServer_quiet.set(null);
  }
  a.server = startDialogServer_serverSocket;

  return a;
}

static void makeAndroid3_handleConsole(final Android3 a) {
  // Console handling stuff
  if (!a.quiet)
    print("You may also type on this console.");
  { Thread _t_0 = new Thread() {
public void run() { try {
    List<String> history = new ArrayList();
    while (licensed()) {
      String line;
      try {
        line = readLine();
      } catch (Throwable e) {
        print(getInnerMessage(e));
        break;
      }
      if (line == null) break;
      /*if (eq(line, "bye")) {
        print("> bye stranger");
        history = new ArrayList<S>();
      } else*/ {
        history.add(line);
        history.add(makeAndroid3_getAnswer(line, history, a)); // prints answer on console too
      }
    }
  } catch (Throwable __e) { printStackTrace2(__e); } }
};
startThread(_t_0); }
}

static DialogHandler makeAndroid3_makeDialogHandler(final Android3 a) {
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
        if (match3("this is a continuation of talk *", s, m)
          || match3("hello bot! this is a continuation of talk *", s, m)) {
          dialogID = unquote(m.m[0]);
          answer = "ok";
        } else try {
          makeAndroid3_io.set(io);
          answer = makeAndroid3_getAnswer(line, history, a);
        } finally {
          makeAndroid3_io.set(null);
        }
        if (a.recordHistory)
          history.add(answer);
        io.sendLine(answer);
        //appendToLog(logFile, s);
      }
    }
  }};
}

static String makeAndroid3_getAnswer(String line, List<String> history, Android3 a) {
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
    if (originalAnswer == null) originalAnswer = "?";
    if (a.newLineAboveAnswer) print();
    print(">" + dropFirst(indentx(2, shorten(rtrim(originalAnswer), a.answerPrintLimit))));
    if (a.newLineBelowAnswer) print();
  }
  return answer;
}

static String makeAndroid3_fallback(String s, List<String> history, String answer) {
  // Now we only do the safe thing instead of VM inspection - give out our process ID
  if (answer == null && match3("what is your pid", s))
    return getPID();
    
  if (answer == null && match3("what is your program id", s)) // should be fairly safe, right?
    return getProgramID();
    
  if (match3("get injection id", s))
    return getInjectionID();
    
  if (answer == null) answer = "?";
  if (answer.indexOf('\n') >= 0 || answer.indexOf('\r') >= 0)
    answer = quote(answer);
  return answer;
}

static boolean makeAndroid3_consoleInUse() {
  for (Object o : record_list)
    if (o instanceof Android3 && ((Android3) o).console)
      return true;
  return false;
}

static Responder makeAndroid3_verboseResponder(final Android3 a) {
  return new Responder() {
    String answer(String s, List<String> history) {
      if (a.verbose)
        print("> " + shorten(s, a.incomingPrintLimit));
      String answer = a.responder.answer(s, history);
      if (a.verbose)
        print("< " + shorten(answer, a.incomingPrintLimit));
      return answer;
    }
  };
}

static ThreadLocal<DialogIO> makeAndroid3_io = new ThreadLocal();

static Android3 makeAndroid3() {
  return makeAndroid3(getProgramTitle() + ".");
}
static <A> A callMain(A c, String... args) {
  callOpt(c, "main", new Object[] {args});
  return c;
}

static void callMain() {
  callMain(mc());
}
static Runnable toRunnable(final Object o) {
  if (o instanceof Runnable) return (Runnable) o;
  return new Runnable() { public void run() { try {  callF(o) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "callF(o)"; }};
}
static <A> A getAndClearThreadLocal(ThreadLocal<A> tl) {
  A a = tl.get();
  tl.set(null);
  return a;
}
static boolean showAnimationInTopRightCorner_alwaysOnTop = true;

// automatically switches to AWT thread for you
// text is optional text below image
static JWindow showAnimationInTopRightCorner(String imageID, String text) {
  if (isHeadless()) return null;
  return showAnimationInTopRightCorner(imageIcon(imageID), text);
}

static JWindow showAnimationInTopRightCorner(final BufferedImage image, final String text) {
  if (isHeadless()) return null;
  return showAnimationInTopRightCorner(imageIcon(image), text);
}

static JWindow showAnimationInTopRightCorner(final ImageIcon imageIcon, final String text) {
  if (isHeadless()) return null;
  return (JWindow) swingAndWait(new F0<Object>() { Object get() { try { 
    JLabel label = new JLabel(imageIcon);
    if (nempty(text)) {
      label.setText(text);
      label.setVerticalTextPosition(SwingConstants.BOTTOM);
      label.setHorizontalTextPosition(SwingConstants.CENTER);
    }
    final JWindow window = showInTopRightCorner(label);
    onClick(label, new Runnable() { public void run() { try {  window.dispose() ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "window.dispose()"; }});
    if (showAnimationInTopRightCorner_alwaysOnTop)
      window.setAlwaysOnTop(true);
    return window;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "JLabel label = new JLabel(imageIcon);\r\n    if (nempty(text)) {\r\n      label.s..."; }});
}

static JWindow showAnimationInTopRightCorner(final String imageID) {
  return showAnimationInTopRightCorner(imageID, "");
}

static JWindow showAnimationInTopRightCorner(String imageID, double seconds) {
  return showAnimationInTopRightCorner(imageID, "", seconds);
}

static JWindow showAnimationInTopRightCorner(String imageID, String text, double seconds) {
  if (isHeadless()) return null;
  return disposeWindowAfter(iround(seconds*1000), showAnimationInTopRightCorner(imageID, text));
}

static JWindow showAnimationInTopRightCorner(BufferedImage img, String text, double seconds) {
  return disposeWindowAfter(iround(seconds*1000), showAnimationInTopRightCorner(img, text));
}

  public static String bytesToHex(byte[] bytes) {
    return bytesToHex(bytes, 0, bytes.length);
  }

  public static String bytesToHex(byte[] bytes, int ofs, int len) {
    StringBuilder stringBuilder = new StringBuilder(len*2);
    for (int i = 0; i < len; i++) {
      String s = "0" + Integer.toHexString(bytes[ofs+i]);
      stringBuilder.append(s.substring(s.length()-2, s.length()));
    }
    return stringBuilder.toString();
  }

static boolean isInteger(String s) {
  if (s == null) return false;
  int n = l(s);
  if (n == 0) return false;
  int i = 0;
  if (s.charAt(0) == '-')
    if (++i >= n) return false;
  while (i < n) {
    char c = s.charAt(i);
    if (c < '0' || c > '9') return false;
    ++i;
  }
  return true;
}
static <A> A oneOf(List<A> l) {
  return l.isEmpty() ? null : l.get(new Random().nextInt(l.size()));
}

static char oneOf(String s) {
  return empty(s) ? '?' : s.charAt(random(l(s)));
}

static String oneOf(String... l) {
  return oneOf(asList(l));
}
static boolean jmatch(String pat, String s) {
  return jmatch(pat, s, null);
}

static boolean jmatch(String pat, String s, Matches matches) {
  if (s == null) return false;
  return jmatch(pat, javaTok(s), matches);
}

static boolean jmatch(String pat, List<String> toks) {
  return jmatch(pat, toks, null);
}

static boolean jmatch(String pat, List<String> toks, Matches matches) {
  List<String> tokpat = javaTok(pat);
  String[] m = match2(tokpat, toks);
  //print(structure(tokpat) + " on " + structure(toks) + " => " + structure(m));
  if (m == null)
    return false;
  else {
    if (matches != null) matches.m = m;
    return true;
  }
}
static void dataToTable_dynSet(List l, int i, Object s) {
  while (i >= l.size()) l.add("");
  l.set(i, s);
}

static List dataToTable_makeRow(Object x, List<String> cols) {
  if (instanceOf(x, "DynamicObject"))
    x = get_raw(x, "fieldValues");

  if (x instanceof Map) {
    Map m = (Map) ( x);
    List row = new ArrayList();
    for (Object _field : keysWithoutHidden(m)) {
      String field = (String) ( _field);
      Object value = m.get(field);
      int col = cols.indexOf(field);
      if (col < 0) {
        cols.add(field);
        col = cols.size()-1;
      }
      dataToTable_dynSet(row, col, dataToTable_wrapValue(value));
    }
    return row;
  }
  
  return litlist(structureOrText(x));
}

static Object dataToTable_wrapValue(Object o) {
  if (o instanceof BufferedImage) return o;
  
    if (o instanceof RGBImage) return o;
  
  if (o instanceof Boolean) return o;
  return structureOrText(o);
}
static String formatSnippetID(String id) {
  return "#" + parseSnippetID(id);
}

static String formatSnippetID(long id) {
  return "#" + id;
}
public static JComponent withLabel(String label, JComponent component) {
  return westAndCenter(jlabel(label + " "), component);
}

static long psI(String snippetID) {
  return parseSnippetID(snippetID);
}
static Producer<String> javaTokC_noMLS_iterator(final String s) {
  return new Producer<String>() {
    final int l = s.length();
    int i = 0;
    
    public String next() {
      if (i >= l) return null;
      
      int j = i;
      char c, d;
      
      // scan for whitespace
      while (j < l) {
        c = s.charAt(j);
        d = j+1 >= l ? '\0' : s.charAt(j+1);
        if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
          ++j;
        else if (c == '/' && d == '*') {
          do ++j; while (j < l && !s.substring(j, Math.min(j+2, l)).equals("*/"));
          j = Math.min(j+2, l);
        } else if (c == '/' && d == '/') {
          do ++j; while (j < l && "\r\n".indexOf(s.charAt(j)) < 0);
        } else
          break;
      }
      
      i = j;
      if (i >= l) return null;
      c = s.charAt(i);
      d = i+1 >= l ? '\0' : s.charAt(i+1);
  
      // scan for non-whitespace
      if (c == '\'' || c == '"') {
        char opener = c;
        ++j;
        while (j < l) {
          if (s.charAt(j) == opener || s.charAt(j) == '\n') { // end at \n to not propagate unclosed string literal errors
            ++j;
            break;
          } else if (s.charAt(j) == '\\' && j+1 < l)
            j += 2;
          else
            ++j;
        }
      } else if (Character.isJavaIdentifierStart(c))
        do ++j; while (j < l && (Character.isJavaIdentifierPart(s.charAt(j)) || "'".indexOf(s.charAt(j)) >= 0)); // for stuff like "don't"
      else if (Character.isDigit(c)) {
        do ++j; while (j < l && Character.isDigit(s.charAt(j)));
        if (j < l && s.charAt(j) == 'L') ++j; // Long constants like 1L
      } else
        ++j;
        
      String t = quickSubstring(s, i, j);
      i = j;
      return t;
    }
  };
}

static boolean isFalse(Object o) {
  return eq(false, o);
}
static void addToWindowTop(Component c, Component toAdd) {
  JFrame frame = getFrame(c);
  Container cp = frame.getContentPane();
  setContentPane(frame, northAndCenter(toAdd, cp));
}
static boolean eq(Object a, Object b) {
  return a == null ? b == null : a == b || a.equals(b);
}

// a little kludge for stuff like eq(symbol, "$X")

static boolean warn_on = true;

static void warn(String s) {
  if (warn_on)
    print("Warning: " + s);
}

static void warn(String s, List<String> warnings) {
  warn(s);
  if (warnings != null)
    warnings.add(s);
}
volatile static boolean conceptsAndBot_running;

static void conceptsAndBot() {
  conceptsAndBot(null);
}

static void conceptsAndBot(Integer autoSaveInterval) {
  if (conceptsAndBot_running) return;
  conceptsAndBot_running = true;
  
  try {
    ensureDBNotRunning(dbBotStandardName());
  } catch (Throwable _e) {
    mainConcepts.dontSave = true; // SAFETY
  
throw rethrow(_e); }
  
  if (autoSaveInterval != null)
    loadAndAutoSaveConcepts(autoSaveInterval);
  else
    loadAndAutoSaveConcepts();
  dbBot();
}
static Window getWindow(Object o) {
  if (!(o instanceof Component)) return null;
  Component c = (Component) o;
  while (c != null) {
    if (c instanceof Window) return (Window) c;
    c = c.getParent();
  }
  return null;
}
static void warnIfOddCount(Object... list) {
  if (odd(l(list)))
    printStackTrace("Odd list size: " + list);
}
static String substanceLAF_defaultSkin = "Creme";

static void substanceLAF() {
  substanceLAF(null);
}

static void substanceLAF(String skinName) {
  try {
    enableSubstance_impl(or2(skinName, substanceLAF_defaultSkin));
  } catch (Throwable __e) { printStackTrace2(__e); }
}
static List<String> getTableLine(JTable tbl, int row) {
  if (row >= 0 && row < tbl.getModel().getRowCount()) {
    List<String> l = new ArrayList();
    for (int i = 0; i < tbl.getModel().getColumnCount(); i++)
      l.add(String.valueOf(tbl.getModel().getValueAt(row, i)));
    return l;
  }
  return null;
}
static String loadCachedTranspilation(String id) {
  return loadTextFilePossiblyGZipped(new File(getCodeProgramDir(id), "Transpilation"));
}

static <A> A collectionGet(Collection<A> c, int idx) {
  if (c == null || idx < 0 || idx >= l(c)) return null;
  if (c instanceof List) return listGet((List<A>) c, idx);
  Iterator<A> it = c.iterator();
  for (int i = 0; i < idx; i++) if (it.hasNext()) it.next(); else return null;
  return it.hasNext() ? it.next() : null;
}
static int hstackWithSpacing_spacing = 10;

// first part can be spacing value
static JPanel hstackWithSpacing(Object... parts) {
  parts = flattenArray2(parts); // allow collections in parameters
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
      panel.add(Box.createRigidArea(new Dimension(spacing, 0)), gbc);
    panel.add(wrapForSmartAdd(parts[i]), gbc);
  }
  gbc.weightx = 1;
  panel.add(Box.createRigidArea(new Dimension(0, 0)), gbc);
  return panel;
}
static String shortClassName(Object o) {
  if (o == null) return null;
  Class c = o instanceof Class ? (Class) o : o.getClass();
  String name = c.getName();
  return shortenClassName(name);
}
static String showFormSubmitButtonName() {
  return "Submit";
}
static String unquote(String s) {
  if (s == null) return null;
  if (startsWith(s, '[')) {
    int i = 1;
    while (i < s.length() && s.charAt(i) == '=') ++i;
    if (i < s.length() && s.charAt(i) == '[') {
      String m = s.substring(1, i);
      if (s.endsWith("]" + m + "]"))
        return s.substring(i+1, s.length()-i-1);
    }
  }
  
  if (s.length() > 1) {
    char c = s.charAt(0);
    if (c == '\"' || c == '\'') {
      int l = endsWith(s, c) ? s.length()-1 : s.length();
      StringBuilder sb = new StringBuilder(l-1);
  
      for (int i = 1; i < l; i++) {
        char ch = s.charAt(i);
        if (ch == '\\') {
          char nextChar = (i == l - 1) ? '\\' : s.charAt(i + 1);
          // Octal escape?
          if (nextChar >= '0' && nextChar <= '7') {
              String code = "" + nextChar;
              i++;
              if ((i < l - 1) && s.charAt(i + 1) >= '0'
                      && s.charAt(i + 1) <= '7') {
                  code += s.charAt(i + 1);
                  i++;
                  if ((i < l - 1) && s.charAt(i + 1) >= '0'
                          && s.charAt(i + 1) <= '7') {
                      code += s.charAt(i + 1);
                      i++;
                  }
              }
              sb.append((char) Integer.parseInt(code, 8));
              continue;
          }
          switch (nextChar) {
          case '\"': ch = '\"'; break;
          case '\\': ch = '\\'; break;
          case 'b': ch = '\b'; break;
          case 'f': ch = '\f'; break;
          case 'n': ch = '\n'; break;
          case 'r': ch = '\r'; break;
          case 't': ch = '\t'; break;
          case '\'': ch = '\''; break;
          // Hex Unicode: u????
          case 'u':
              if (i >= l - 5) {
                  ch = 'u';
                  break;
              }
              int code = Integer.parseInt(
                      "" + s.charAt(i + 2) + s.charAt(i + 3)
                         + s.charAt(i + 4) + s.charAt(i + 5), 16);
              sb.append(Character.toChars(code));
              i += 5;
              continue;
          default:
            ch = nextChar; // added by Stefan
          }
          i++;
        }
        sb.append(ch);
      }
      return sb.toString();
    }
  }
    
  return s; // not quoted - return original
}
static String programID() {
  return getProgramID();
}
static JFrame showPackedFrame(String title, Component contents) {
  return packFrame(showFrame(title, contents));
}

static JFrame showPackedFrame(Component contents) {
  return packFrame(showFrame(contents));
}
static boolean isEmpty(Collection c) {
  return c == null || c.isEmpty();
}

static boolean isEmpty(CharSequence s) {
  return s == null || s.length() == 0;
}

static boolean isEmpty(Object[] a) {
  return a == null || a.length == 0;
}

static boolean isEmpty(Map map) {
  return map == null || map.isEmpty();
}
static void stopTimer(java.util.Timer timer) {
  if (timer != null) timer.cancel();
}
// value = 0 to 1
static JSplitPane setSplitPaneLater(final Component c, final double value) {
  final JSplitPane sp = first(childrenOfType(c, JSplitPane.class));
  if (sp != null)
    swingLater(new Runnable() { public void run() { try {  sp.setDividerLocation(value); 
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "sp.setDividerLocation(value);"; }});
  return sp;
}
static <A extends JComponent> A jMinWidth_pure(int w, A c) {
  Dimension size = c.getMinimumSize();
  c.setMinimumSize(new Dimension(w, size.height));
  return c;
}
static char unquoteCharacter(String s) {
  assertTrue(s.startsWith("'") && s.length() > 1);
  return unquote("\"" + s.substring(1, s.endsWith("'") ? s.length()-1 : s.length()) + "\"").charAt(0);
}
static void validateFrame(Component c) {
  revalidateFrame(c);
}
static int parseInt(String s) {
  return empty(s) ? 0 : Integer.parseInt(s);
}

static int parseInt(char c) {
  return Integer.parseInt(str(c));
}
static void autoVMExit() {
  call(getJavaX(), "autoVMExit");
}
static String structureOrText(Object o) {
  return o instanceof String ? (String) o : structure(o);
}
static Producer<String> javaTokC_noMLS_onReader(final BufferedReader r) {
  final class X implements Producer<String> {
    StringBuilder buf = new StringBuilder(); // stores from "i"
    char c, d, e = 'x'; // just not '\0'
    
    X() {
      // fill c, d and e
      nc();
      nc();
      nc();
    }
    
    // get next character(s) into c, d and e
    void nc() { try {
      c = d;
      d = e;
      if (e == '\0') return;
      int i = r.read();
      e = i < 0 ? '\0' : (char) i;
    } catch (Exception __e) { throw rethrow(__e); } }
    
    void ncSave() {
      if (c != '\0') {
        buf.append(c);
        nc();
      }
    }
    
    public String next() {
      // scan for whitespace
      while (c != '\0') {
        if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
          nc();
        else if (c == '/' && d == '*') {
          do nc(); while (c != '\0' && !(c == '*' && d == '/'));
          nc(); nc();
        } else if (c == '/' && d == '/') {
          do nc(); while (c != '\0' && "\r\n".indexOf(c) < 0);
        } else
          break;
      }
      
      if (c == '\0') return null;

      // scan for non-whitespace
      if (c == '\'' || c == '"') {
        char opener = c;
        ncSave();
        while (c != '\0') {
          if (c == opener || c == '\n') { // end at \n to not propagate unclosed string literal errors
            ncSave();
            break;
          } else if (c == '\\') {
            ncSave();
            ncSave();
          } else
            ncSave();
        }
      } else if (Character.isJavaIdentifierStart(c))
        do ncSave(); while (Character.isJavaIdentifierPart(c) || c == '\''); // for stuff like "don't"
      else if (Character.isDigit(c)) {
        do ncSave(); while (Character.isDigit(c));
        if (c == 'L') ncSave(); // Long constants like 1L
      } else
        ncSave();
        
      String t = buf.toString();
      buf.setLength(0);
      return t;
    }
  }
  
  return new X();
}

static <A extends Component> A setFrameSize(A c, int w, int h) {
  JFrame f = getFrame(c);
  if (f != null)
    f.setSize(w, h);
  return c;
}

static <A extends Component> A setFrameSize(int w, int h, A c) {
  return setFrameSize(c, w, h);
}
static Object getOpt(Object o, String field) {
  return getOpt_cached(o, field);
}

static Object getOpt_raw(Object o, String field) {
  try {
    Field f = getOpt_findField(o.getClass(), field);
    if (f == null) return null;
    f.setAccessible(true);
    return f.get(o);
  } catch (Exception e) {
    throw new RuntimeException(e);
  }
}

// access of static fields is not yet optimized
static Object getOpt(Class c, String field) {
  try {
    if (c == null) return null;
    Field f = getOpt_findStaticField(c, field);
    if (f == null) return null;
    f.setAccessible(true);
    return f.get(null);
  } catch (Exception e) {
    throw new RuntimeException(e);
  }
}

static Field getOpt_findStaticField(Class<?> c, String field) {
  Class _c = c;
  do {
    for (Field f : _c.getDeclaredFields())
      if (f.getName().equals(field) && (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0)
        return f;
    _c = _c.getSuperclass();
  } while (_c != null);
  return null;
}

static <A> A popLast(List<A> l) {
  return liftLast(l);
}
static <A> A nuStubInnerObject(Class<A> c) { try {
  Class outerType = getOuterClass(c);
  Constructor m = c.getDeclaredConstructor(outerType);
  m.setAccessible(true);
  return (A) m.newInstance(new Object[] {null});
} catch (Exception __e) { throw rethrow(__e); } }
static Map litmap(Object... x) {
  HashMap map = new HashMap();
  litmap_impl(map, x);
  return map;
}

static void litmap_impl(Map map, Object... x) {
  for (int i = 0; i < x.length-1; i += 2)
    if (x[i+1] != null)
      map.put(x[i], x[i+1]);
}
static String formatDate() {
  return formatDate(now());
}

static String formatDate(long timestamp) {
  return timestamp == 0 ? "-" : str(new Date(timestamp));
}
static JFrame consoleFrame() {
  return (JFrame) getOpt(get(getJavaX(), "console"), "frame");
}
static boolean[] boolArrayFromBytes(byte[] a, int n) {
  boolean[] b = new boolean[n];
  int m = min(n, l(a)*8);
  for (int i = 0; i < m; i++)
    b[i] = (a[i/8] & 1 << (i & 7)) != 0;
  return b;
}
static JMenu getMenuNamed(JMenuBar bar, String name) {
  int n = bar.getMenuCount();
  for (int i = 0; i < n; i++) {
    JMenu m = bar.getMenu(i);
    if (m != null && eq(m.getText(), name))
      return m;
  }
  return null;
}
static int shorten_default = 100;

static String shorten(String s) { return shorten(s, shorten_default); }

static String shorten(String s, int max) {
  return shorten(s, max, "...");
}

static String shorten(String s, int max, String shortener) {
  if (s == null) return "";
  if (max < 0) return s;
  return s.length() <= max ? s : substring(s, 0, min(s.length(), max-l(shortener))) + shortener;
}

static String shorten(int max, String s) { return shorten(s, max); }
static void swingLater(long delay, final Object r) {
  javax.swing.Timer timer = new javax.swing.Timer(toInt(delay), actionListener(r));
  timer.setRepeats(false);
  timer.start();
}

static void swingLater(Object r) {
  SwingUtilities.invokeLater(toRunnable(r));
}

static <A> A swingNu(final Class<A> c, final Object... args) {
  return swingConstruct(c, args);
}
static String getStackTrace2(Throwable throwable) {
  return hideCredentials(getStackTrace(throwable) + replacePrefix("java.lang.RuntimeException: ", "FAIL: ",
    hideCredentials(str(getInnerException(throwable)))) + "\n");
}
static void swingAndWait(Runnable r) { try {
  if (isAWTThread())
    r.run();
  else
    EventQueue.invokeAndWait(r);
} catch (Exception __e) { throw rethrow(__e); } }

static Object swingAndWait(final Object f) {
  if (isAWTThread())
    return callF(f);
  else {
    final Var result = new Var();
    swingAndWait(new Runnable() { public void run() { try { 
      result.set(callF(f));
    
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "result.set(callF(f));"; }});
    return result.get();
  }
}
static String dropPrefix(String prefix, String s) {
  return s == null ? null : s.startsWith(prefix) ? s.substring(l(prefix)) : s;
}
static JMenuItem jmenuItem(String text, final Object r) {
  JMenuItem mi = new JMenuItem(text);
  mi.addActionListener(actionListener(r));
  return mi;
}
static String defaultThreadName_name;

static String defaultThreadName() {
  if (defaultThreadName_name == null)
    defaultThreadName_name = "A thread by " + programID();
  return defaultThreadName_name;
}
static HashMap<String,Class> findClass_cache = new HashMap();

// currently finds only inner classes of class "main"
// returns null on not found
// this is the simple version that is not case-tolerant
static Class findClass(String name) {
  synchronized(findClass_cache) {
    if (findClass_cache.containsKey(name))
      return findClass_cache.get(name);
      
    if (!isJavaIdentifier(name)) return null;
    
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
static ThreadLocal<Boolean> doPost_silently = new ThreadLocal();

static String doPost(Map urlParameters, String url) {
  return doPost(makePostData(urlParameters), url);
}

static String doPost(String urlParameters, String url) { try {
  URL _url = new URL(url);
  ping();
  return doPost(urlParameters, _url.openConnection(), _url);
} catch (Exception __e) { throw rethrow(__e); } }

static String doPost(String urlParameters, URLConnection conn, URL url) { try {
  boolean silently = isTrue(optParam(doPost_silently));
  setHeaders(conn);

  int l = lUtf8(urlParameters);
  if (!silently)
    print("Sending POST request: " + url + " (" + l + " bytes)");
      
  // connect and do POST
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
} catch (Exception __e) { throw rethrow(__e); } }
static Field setOpt_findField(Class c, String field) {
  HashMap<String, Field> map;
  synchronized(getOpt_cache) {
    map = getOpt_cache.get(c);
    if (map == null)
      map = getOpt_makeCache(c);
  }
  return map.get(field);
}

static void setOpt(Object o, String field, Object value) { try {
  if (o == null) return;
  
  
  
  Class c = o.getClass();
  HashMap<String, Field> map;
  
  synchronized(getOpt_cache) {
    map = getOpt_cache.get(c);
    if (map == null)
      map = getOpt_makeCache(c);
  }
  
  if (map == getOpt_special) {
    if (o instanceof Class) {
      setOpt((Class) o, field, value);
      return;
    }
    
    // It's probably a subclass of Map. Use raw method
    setOpt_raw(o, field, value);
    return;
  }
  
  Field f = map.get(field);
  if (f != null)
    smartSet(f, o, value); // possible improvement: skip setAccessible
} catch (Exception __e) { throw rethrow(__e); } }

static void setOpt(Class c, String field, Object value) {
  if (c == null) return;
  try {
    Field f = setOpt_findStaticField(c, field);
    if (f != null)
      smartSet(f, null, value);
  } catch (Exception e) {
    throw new RuntimeException(e);
  }
}
  
static Field setOpt_findStaticField(Class<?> c, String field) {
  Class _c = c;
  do {
    for (Field f : _c.getDeclaredFields())
      if (f.getName().equals(field) && (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0)
        return f;
    _c = _c.getSuperclass();
  } while (_c != null);
  return null;
}
static boolean isSubtypeOf(Class a, Class b) {
  return b.isAssignableFrom(a); // << always hated that method, let's replace it!
}
static RuntimeException fail() { throw new RuntimeException("fail"); }
static RuntimeException fail(Throwable e) { throw asRuntimeException(e); }
static RuntimeException fail(Object msg) { throw new RuntimeException(String.valueOf(msg)); }
static RuntimeException fail(String msg) { throw new RuntimeException(msg == null ? "" : msg); }
static RuntimeException fail(String msg, Throwable innerException) { throw new RuntimeException(msg, innerException); }

static boolean showTable_searcher = true;

static JTable showTable(Object data) {
  return dataToTable_uneditable(data);
}

static JTable showTable(String title, Object data) {
  return showTable(data, title);
}

static JTable showTable(Object data, String title) {
  return dataToTable_uneditable(data, title);
}

static JTable showTable(JTable table, Object data) {
  return showTable(table, data, autoFrameTitle());
}

static JTable showTable(Object data, JTable table) {
  return showTable(table, data);
}

static JTable showTable(JTable table, Object data, String title) {
  if (table == null)
    table = showTable(data, title);
  else {
    setFrameTitle(table, title);
    dataToTable_uneditable(table, data);
  }
  return table;
}

static JTable showTable() {
  return showTable(new ArrayList<List<String>>(), new ArrayList<String>());
}

static JTable showTable(String title) {
  return showTable(new ArrayList<List<String>>(), new ArrayList<String>(), title);
}

static JTable showTable(List<List<String>> rows, List<String> cols) {
  return showTable(rows, cols, autoFrameTitle());
}
  
static JTable showTable(List<List<String>> rows, List<String> cols, String title) {
  JTable tbl = sexyTable();
  fillTableWithStrings(tbl, rows, cols);
  showFrame(title, tbl);
  return tbl;
}
static void swingNowOrLater(Runnable r) {
  if (isAWTThread())
    r.run();
  else
    swingLater(r);
}
static TableWithTooltips tableWithToolTips() {
  return tableWithTooltips();
}

static java.util.Timer doLater_daemon(long delay, final Object r) {
  final java.util.Timer timer = new java.util.Timer(true);
  timer.schedule(timerTask(r), delay);
  return timer;
}

static boolean isJavaIdentifier(String s) {
  if (empty(s) || !Character.isJavaIdentifierStart(s.charAt(0)))
    return false;
  for (int i = 1; i < s.length(); i++)
    if (!Character.isJavaIdentifierPart(s.charAt(i)))
      return false;
  return true;
}
static float parseFloat(String s) {
  return Float.parseFloat(s);
}
static void restartWithDelay(int delay) {
  Object j = getJavaX();
  call(j, "preKill");
  call(j, "nohupJavax", smartJoin((String[]) get(j, "fullArgs")), call(j, "fullVMArguments"));
  sleep(delay);
  System.exit(0);
  sleep();
}
static volatile boolean ping_pauseAll;
static int ping_sleep = 100; // poll pauseAll flag every 100

static volatile boolean ping_anyActions;
static Map<Thread, Object> ping_actions = newWeakHashMap();


// always returns true
static boolean ping() {
  if (ping_pauseAll  || ping_anyActions ) ping_impl();
  return true;
}

// returns true when it slept
static boolean ping_impl() { try {
  if (ping_pauseAll && !isAWTThread()) {
    do
      Thread.sleep(ping_sleep);
    while (ping_pauseAll);
    return true;
  }
  
  
  if (ping_anyActions) {
    Object action;
    synchronized(ping_actions) {
      action = ping_actions.get(currentThread());
      if (action instanceof Runnable)
        ping_actions.remove(currentThread());
      if (ping_actions.isEmpty()) ping_anyActions = false;
    }
    
    if (action instanceof Runnable)
      ((Runnable) action).run();
    else if (eq(action, "cancelled"))
      throw fail("Thread cancelled.");
  }
  
  
  return false;
} catch (Exception __e) { throw rethrow(__e); } }
static List parseList(String s) {
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
static ThreadLocal<String> loadPage_charset = new ThreadLocal();
static boolean loadPage_allowGzip = true, loadPage_debug;
static boolean loadPage_anonymous; // don't send computer ID
static int loadPage_verboseness = 100000;
static int loadPage_retries = 1; //60; // seconds
static ThreadLocal<Boolean> loadPage_silent = new ThreadLocal();
static volatile int loadPage_forcedTimeout; // ms
static ThreadLocal<Integer> loadPage_forcedTimeout_byThread = new ThreadLocal(); // ms
static ThreadLocal<Map<String, List<String>>> loadPage_responseHeaders = new ThreadLocal();
static ThreadLocal<Map<String, String>> loadPage_extraHeaders = new ThreadLocal();

public static String loadPageSilently(String url) { try {
  return loadPageSilently(new URL(loadPage_preprocess(url)));
} catch (Exception __e) { throw rethrow(__e); } }

public static String loadPageSilently(URL url) { try {
  if (url.getProtocol().equals("https"))
    disableCertificateValidation();
    
  IOException e = null;
  for (int tries = 0; tries < loadPage_retries; tries++)
    try {
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
      if (tries < loadPage_retries-1) sleepSeconds(1);
    }
  throw e;
} catch (Exception __e) { throw rethrow(__e); } }

static String loadPage_preprocess(String url) {  
  if (url.startsWith("tb/")) // don't think we use this anymore
    url = tb_mainServer() + "/" + url;
  if (url.indexOf("://") < 0)
    url = "http://" + url;
  return url;
}

static String loadPage(String url) { try {
  url = loadPage_preprocess(url);
  if (!isTrue(loadPage_silent.get()))
    printWithTime("Loading: " + hideCredentials(url));
  return loadPageSilently(new URL(url));
} catch (Exception __e) { throw rethrow(__e); } }

static String loadPage(URL url) {
  return loadPage(url.toExternalForm());
}

static String loadPage(URLConnection con, URL url) throws IOException {
  return loadPage(con, url, true);
}

static String loadPage(URLConnection con, URL url, boolean addHeaders) throws IOException {
  Map<String, String> extraHeaders = getAndClearThreadLocal(loadPage_extraHeaders);
  if (addHeaders) try {
    if (!loadPage_anonymous)
      setHeaders(con);
    if (loadPage_allowGzip)
      con.setRequestProperty("Accept-Encoding", "gzip");
    con.setRequestProperty("X-No-Cookies", "1");
    for (String key : keys(extraHeaders))
      con.setRequestProperty(key, extraHeaders.get(key));
  } catch (Throwable e) {} // fails if within doPost
  loadPage_responseHeaders.set(con.getHeaderFields());
  String contentType = con.getContentType();
  if (contentType == null) {
    //printStruct("Headers: ", con.getHeaderFields());
    throw new IOException("Page could not be read: " + url);
  }
  //print("Content-Type: " + contentType);
  String charset = loadPage_charset == null ? null : loadPage_charset.get();
  if (charset == null) charset = loadPage_guessCharset(contentType);
  
  InputStream in = con.getInputStream();
  try {
    if ("gzip".equals(con.getContentEncoding())) {
      if (loadPage_debug)
        print("loadPage: Using gzip.");
      in = new GZIPInputStream(in);
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
      if ((n % loadPage_verboseness) == 0) print("  " + n + " chars read");
    }
    return buf.toString();
  } finally { in.close(); }
}

static String loadPage_guessCharset(String contentType) {
  Pattern p = Pattern.compile("text/[a-z]+;\\s*charset=([^\\s]+)\\s*");
  Matcher m = p.matcher(contentType);
  String match = m.matches() ? m.group(1) : null;
  if (loadPage_debug)
    print("loadPage: contentType=" + contentType + ", match: " + match);
  /* If Content-Type doesn't match this pre-conception, choose default and hope for the best. */
  //return or(match, "ISO-8859-1");
  return or(match, "UTF-8");
}

static URLConnection loadPage_openConnection(URL url) {
  URLConnection con = openConnection(url);
  int timeout = toInt(loadPage_forcedTimeout_byThread.get());
  if (timeout == 0) timeout = loadPage_forcedTimeout;
  if (timeout != 0)
    setURLConnectionTimeouts(con, loadPage_forcedTimeout);
  return con;
}
static JComponent showTitledForm(String title, Object... _parts) {
  return showFormTitled(title, _parts);
}

static JPanel northAndCenter(Component n, Component c) {
  return centerAndNorth(c, n);
}
static Frame getAWTFrame(final Object _o) {
  return swing(new F0<Frame>() { Frame get() { try { 
    Object o = _o;
    /*
    ifdef HaveProcessing
      if (o instanceof PApplet) o = ((PApplet) o).getSurface();
    endifdef
    */
    if (o instanceof ButtonGroup) o = first(buttonsInGroup((ButtonGroup) o));
    if (!(o instanceof Component)) return null;
    Component c = (Component) o;
    while (c != null) {
      if (c instanceof Frame) return (Frame) c;
      c = c.getParent();
    }
    return null;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "O o = _o;\r\n    /*\r\n    ifdef HaveProcessing\r\n      if (o instanceof PApplet) ..."; }});
}
static void assertTrue(Object o) {
  if (!(eq(o, true) /*|| isTrue(pcallF(o))*/))
    throw fail(str(o));
}
  
static boolean assertTrue(String msg, boolean b) {
  if (!b)
    throw fail(msg);
  return b;
}

static boolean assertTrue(boolean b) {
  if (!b)
    throw fail("oops");
  return b;
}
static <A> ArrayList<A> litlist(A... a) {
  ArrayList l = new ArrayList(a.length);
  for (A x : a) l.add(x);
  return l;
}
// get purpose 1: access a list/array/map (safer version of x.get(y))

static <A> A get(List<A> l, int idx) {
  return l != null && idx >= 0 && idx < l(l) ? l.get(idx) : null;
}

// seems to conflict with other signatures
/*static <A, B> B get(Map<A, B> map, A key) {
  ret map != null ? map.get(key) : null;
}*/

static <A> A get(A[] l, int idx) {
  return idx >= 0 && idx < l(l) ? l[idx] : null;
}

// default to false
static boolean get(boolean[] l, int idx) {
  return idx >= 0 && idx < l(l) ? l[idx] : false;
}

// get purpose 2: access a field by reflection or a map

static Object get(Object o, String field) {
  try {
    if (o instanceof Class) return get((Class) o, field);
    
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

static Object get_raw(Object o, String field) {
  try {
    Field f = get_findField(o.getClass(), field);
    f.setAccessible(true);
    return f.get(o);
  } catch (Exception e) {
    throw new RuntimeException(e);
  }
}

static Object get(Class c, String field) {
  try {
    Field f = get_findStaticField(c, field);
    f.setAccessible(true);
    return f.get(null);
  } catch (Exception e) {
    throw new RuntimeException(e);
  }
}

static Field get_findStaticField(Class<?> c, String field) {
  Class _c = c;
  do {
    for (Field f : _c.getDeclaredFields())
      if (f.getName().equals(field) && (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0)
        return f;
    _c = _c.getSuperclass();
  } while (_c != null);
  throw new RuntimeException("Static field '" + field + "' not found in " + c.getName());
}

static Field get_findField(Class<?> c, String field) {
  Class _c = c;
  do {
    for (Field f : _c.getDeclaredFields())
      if (f.getName().equals(field))
        return f;
    _c = _c.getSuperclass();
  } while (_c != null);
  throw new RuntimeException("Field '" + field + "' not found in " + c.getName());
}

static long toMS(double seconds) {
  return (long) (seconds*1000);
}
static int makeBot(String greeting) {
  return makeAndroid3(greeting).port;
}

static Android3 makeBot(Android3 a) {
  makeAndroid3(a);
  return a;
}

static Android3 makeBot(String greeting, Object responder) {
  Android3 a = new Android3(greeting);
  a.responder = makeResponder(responder);
  makeBot(a);
  return a;
}

static Android3 makeBot() {
  return makeAndroid3(getProgramTitle() + ".");
}
static boolean isLongConstant(String s) {
  if (!s.endsWith("L")) return false;
  s = s.substring(0, l(s)-1);
  return isInteger(s);
}
static String autoFrameTitle() {
  return getProgramTitle();
}

static void autoFrameTitle(Component c) {
  setFrameTitle(getFrame(c), autoFrameTitle());
}
static JFrame showFrame() {
  return makeFrame();
}

static JFrame showFrame(Object content) {
  return makeFrame(content);
}

static JFrame showFrame(String title) {
  return makeFrame(title);
}

static JFrame showFrame(String title, Object content) {
  return makeFrame(title, content);
}

static JFrame showFrame(final JFrame frame) {
  if (frame != null) { swing(new Runnable() { public void run() { try { 
    if (frameTooSmall(frame)) frameStandardSize(frame);
    frame.setVisible(true);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (frameTooSmall(frame)) frameStandardSize(frame);\r\n    frame.setVisible(true);"; }}); }
  return frame;
}

// make or update frame
static JFrame showFrame(String title, Object content, JFrame frame) {
  if (frame == null)
    return showFrame(title, content);
  else {
    frame.setTitle(title);
    setFrameContents(frame, content);
    return frame;
  }
}
static String internIfLongerThan(String s, int l) {
  return s == null ? null : l(s) >= l ? intern(s) : s;
}
static void pcallOpt_noArgs(Object o, String method) {
  try { callOpt_noArgs(o, method); } catch (Throwable __e) { printStackTrace2(__e); }
}
static void fillJMenu(JMenu m, Object... x) {
  if (x == null) return;
  for (int i = 0; i < l(x); i++) {
    Object o = x[i], y = get(x, i+1);
    if (o instanceof List)
      fillJMenu(m, asArray((List) o));
    else if (eqOneOf(o, "***", "---", "===", ""))
      m.addSeparator();
    else if (o instanceof String && isRunnableX(y)) {
      m.add(jmenuItem((String) o, y));
      ++i;
    } else if (o instanceof JMenuItem)
      m.add((JMenuItem) o); // "call" might use wrong method
    else if (o instanceof String || o instanceof Action || o instanceof Component)
      call(m, "add", o);
    else
      print("Unknown menu item: " + o);
  }
}
static List<String> javaTokC(String s) {
  if (s == null) return null;
  int l = s.length();
  ArrayList<String> tok = new ArrayList();
  
  int i = 0;
  while (i < l) {
    int j = i;
    char c, d;
    
    // scan for whitespace
    while (j < l) {
      c = s.charAt(j);
      d = j+1 >= l ? '\0' : s.charAt(j+1);
      if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
        ++j;
      else if (c == '/' && d == '*') {
        do ++j; while (j < l && !s.substring(j, Math.min(j+2, l)).equals("*/"));
        j = Math.min(j+2, l);
      } else if (c == '/' && d == '/') {
        do ++j; while (j < l && "\r\n".indexOf(s.charAt(j)) < 0);
      } else
        break;
    }
    
    i = j;
    if (i >= l) break;
    c = s.charAt(i);
    d = i+1 >= l ? '\0' : s.charAt(i+1);

    // scan for non-whitespace
    if (c == '\'' || c == '"') {
      char opener = c;
      ++j;
      while (j < l) {
        if (s.charAt(j) == opener || s.charAt(j) == '\n') { // end at \n to not propagate unclosed string literal errors
          ++j;
          break;
        } else if (s.charAt(j) == '\\' && j+1 < l)
          j += 2;
        else
          ++j;
      }
    } else if (Character.isJavaIdentifierStart(c))
      do ++j; while (j < l && (Character.isJavaIdentifierPart(s.charAt(j)) || "'".indexOf(s.charAt(j)) >= 0)); // for stuff like "don't"
    else if (Character.isDigit(c)) {
      do ++j; while (j < l && Character.isDigit(s.charAt(j)));
      if (j < l && s.charAt(j) == 'L') ++j; // Long constants like 1L
    } else if (c == '[' && d == '[') {
      do ++j; while (j+1 < l && !s.substring(j, j+2).equals("]]"));
      j = Math.min(j+2, l);
    } else if (c == '[' && d == '=' && i+2 < l && s.charAt(i+2) == '[') {
      do ++j; while (j+2 < l && !s.substring(j, j+3).equals("]=]"));
      j = Math.min(j+3, l);
    } else
      ++j;
      
    tok.add(javaTok_substringC(s, i, j));
    i = j;
  }
  
  return tok;
}
static <A> A assertEquals(Object x, A y) {
  return assertEquals(null, x, y);
}

static <A> A assertEquals(String msg, Object x, A y) {
  if (!(x == null ? y == null : x.equals(y)))
    throw fail((msg != null ? msg + ": " : "") + y + " != " + x);
  return y;
}
static String autoRestart_localMD5;

static String autoRestart_localMD5() { 
  if (autoRestart_localMD5 == null)
    autoRestart_localMD5 = md5(loadCachedTranspilation(programID()));
  return autoRestart_localMD5;
}
static String programTitle() {
  return getProgramName();
}
// TODO: all the fringe cases (?)
static <A> JPanel hvgrid(List<List<A>> components) {
  if (empty(components)) return new JPanel();
  int h = l(components), w = l(first(components));
  JPanel panel = new JPanel();
  panel.setLayout(new GridLayout(h, w));
  for (List<A> row : components)
    smartAdd(panel, row);
  return panel;  
}

static <A> JPanel hvgrid(List<List<A>> components, int gap) {
  JPanel panel = hvgrid(components);
  GridLayout g = (GridLayout) ( panel.getLayout());
  g.setHgap(gap);
  g.setVgap(gap);
  return panel;
}
static long parseLong(String s) {
  if (s == null) return 0;
  return Long.parseLong(dropSuffix("L", s));
}

static long parseLong(Object s) {
  return Long.parseLong((String) s);
}
// action can be Runnable or a function name
static JButton newButton(final String text, final Object action) {
  return swing(new F0<JButton>() { JButton get() { try { 
    JButton btn = new JButton(text);
    // submitButtonOnEnter(btn); // test this first
    if (action != null)
      btn.addActionListener(actionListener(action));
    return btn;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "JButton btn = new JButton(text);\r\n    // submitButtonOnEnter(btn); // test th..."; }});
}
// action = runnable or method name
static void onUpdate(JComponent c, final Object r) {
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
  else if (c instanceof ItemSelectable) // JCheckBox and others
    ((ItemSelectable) c).addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        call(r);
      }
    });
  else
    print("Warning: onUpdate doesn't know " + getClassName(c));
}

static void onUpdate(List<? extends JComponent> l, Object r) {
  for (JComponent c : l)
    onUpdate(c, r);
}
static void messageBox(final String msg) {
  if (headless()) print(msg);
  else { swing(new Runnable() { public void run() { try { 
    JOptionPane.showMessageDialog(null, msg, "JavaX", JOptionPane.INFORMATION_MESSAGE);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "JOptionPane.showMessageDialog(null, msg, \"JavaX\", JOptionPane.INFORMATION_MES..."; }}); }
}

static void messageBox(Throwable e) {
  showConsole();
  printStackTrace(e);
  messageBox(hideCredentials(innerException(e)));
}
static String md5(String text) { try {
  if (text == null) return "-";
  return bytesToHex(md5_impl(text.getBytes("UTF-8"))); // maybe different than the way PHP does it...
} catch (Exception __e) { throw rethrow(__e); } }

static String md5(byte[] data) {
  if (data == null) return "-";
  return bytesToHex(md5_impl(data));
}

static MessageDigest md5_md;

/*static byte[] md5_impl(byte[] data) ctex {
  if (md5_md == null)
    md5_md = MessageDigest.getInstance("MD5");
  return ((MessageDigest) md5_md.clone()).digest(data);
}*/

static byte[] md5_impl(byte[] data) { try {
  return MessageDigest.getInstance("MD5").digest(data);
} catch (Exception __e) { throw rethrow(__e); } }

static String md5(File file) { try {
  return md5(loadBinaryFile(file));
} catch (Exception __e) { throw rethrow(__e); } }
static <A> List<A> ll(A... a) {
  ArrayList l = new ArrayList(a.length);
  for (A x : a) l.add(x);
  return l;
}
// TODO: send hard errors to core

static AtomicLong _handleError_nonVMErrors = new AtomicLong();
static AtomicLong _handleError_vmErrors = new AtomicLong();
static AtomicLong _handleError_outOfMemoryErrors = new AtomicLong();
static volatile long _handleError_lastOutOfMemoryError;
static volatile Error _handleError_lastHardError;

static void _handleError(Error e) {
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
static Object pcallF(Object f, Object... args) {
  return pcallFunction(f, args);
}


static <A, B> B pcallF(F1<A, B> f, A a) { try {
  return f == null ? null : f.get(a);
} catch (Throwable __e) { return null; } }

static JPanel jFullCenter(final Component c) {
  return swing(new F0<JPanel>() { JPanel get() { try { 
    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(c);
    return panel;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "JPanel panel = new JPanel(new GridBagLayout);\r\n    panel.add(c);\r\n    ret panel;"; }});
}
static boolean anyValueContainsIgnoreCase(Map map, String pat) {
  for (Object val : values(map))
    if (val instanceof String && containsIgnoreCase((String) val, pat))
      return true;
  return false;
}
static Class mc() {
  return main.class;
}
static int[] toIntArray(List<Integer> l) {
  int[] a = new int[l(l)];
  for (int i = 0; i < a.length; i++)
    a[i] = l.get(i);
  return a;
}
static boolean neq(Object a, Object b) {
  return !eq(a, b);
}
static List<Map<String, Object>> rawTableData(JTable t) {
  int n = tableRows(t);
  List l = new ArrayList();
  for (int i = 0; i < n; i++)
    l.add(rawTableLineAsMap(t, i));
  return l;
}
static <A> int indexOf(List<A> l, A a, int startIndex) {
  if (l == null) return -1;
  for (int i = startIndex; i < l(l); i++)
    if (eq(l.get(i), a))
      return i;
  return -1;
}

static <A> int indexOf(List<A> l, int startIndex, A a) {
  return indexOf(l, a, startIndex);
}

static <A> int indexOf(List<A> l, A a) {
  if (l == null) return -1;
  return l.indexOf(a);
}

static int indexOf(String a, String b) {
  return a == null || b == null ? -1 : a.indexOf(b);
}

static int indexOf(String a, String b, int i) {
  return a == null || b == null ? -1 : a.indexOf(b, i);
}

static int indexOf(String a, char b) {
  return a == null ? -1 : a.indexOf(b);
}

static int indexOf(String a, char b, int i) {
  return a == null ? -1 : a.indexOf(b, i);
}

static int indexOf(String a, int i, String b) {
  return a == null || b == null ? -1 : a.indexOf(b, i);
}

static <A> int indexOf(A[] x, A a) {
  if (x == null) return -1;
  for (int i = 0; i < l(x); i++)
    if (eq(x[i], a))
      return i;
  return -1;
}
static void quoteToPrintWriter(String s, PrintWriter out) {
  if (s == null) { out.print("null"); return; }
  out.print('"');
  int l = s.length();
  for (int i = 0; i < l; i++) {
    char c = s.charAt(i);
    if (c == '\\' || c == '"') {
      out.print('\\'); out.print(c);
    } else if (c == '\r')
      out.print("\\r");
    else if (c == '\n')
      out.print("\\n");
    else
      out.print(c);
  }
  out.print('"');
}
static void printStructure(String prefix, Object o) {
  if (endsWithLetter(prefix)) prefix += ": ";
  print(prefix + structureForUser(o));
}

static void printStructure(Object o) {
  print(structureForUser(o));
}

static <A, B> void mapPut2(Map<A, B> map, A key, B value) {
  if (map != null && key != null)
    if (value != null) map.put(key, value);
    else map.remove(key);
}
static JButton setButtonImage(BufferedImage img, JButton btn) {
  btn.setIcon(imageIcon(img));
  return btn;
}
static boolean eqOneOf(Object o, Object... l) {
  for (Object x : l) if (eq(o, x)) return true; return false;
}
static void preloadProgramTitle() {
  { Thread _t_0 = new Thread() {
public void run() { try { programTitle(); } catch (Throwable __e) { printStackTrace2(__e); } }
};
startThread(_t_0); }
}
static String strUnnull(Object o) {
  return o == null ? "" : str(o);
}
static JTextField onEnter(JTextField tf, final Object action) {
  if (action == null || tf == null) return tf;
  tf.addActionListener(actionListener(action));
  return tf;
}

static JButton onEnter(JButton btn, final Object action) {
  if (action == null || btn == null) return btn;
  btn.addActionListener(actionListener(action));
  return btn;
}

static JList onEnter(JList list, Object action) {
  list.addKeyListener(enterKeyListener(action));
  return list;
}

// editable only
static JComboBox onEnter(final JComboBox cb, final Object action) {
  JTextField text = (JTextField) cb.getEditor().getEditorComponent();
  /*onEnter(text, r {
    cb.hidePopup();
    pcallF(action);
  });*/
  onEnter(text, action);
  return cb;
}
// returns from C to C
static String jextract(String pat, String s) {
  List<String> tok = javaTok(s);
  List<String> tokpat = javaTok(pat);
  jfind_preprocess(tokpat);
  int i = jfind(tok, tokpat);
  if (i < 0) return null;
  int j = i + l(tokpat) - 2;
  return join(subList(tok, i, j));
}

static Object nuObject(String className, Object... args) { try {
  return nuObject(classForName(className), args);
} catch (Exception __e) { throw rethrow(__e); } }

// too ambiguous - maybe need to fix some callers
/*static O nuObject(O realm, S className, O... args) {
  ret nuObject(_getClass(realm, className), args);
}*/

static <A> A nuObject(Class<A> c, Object... args) { try {
  if (args.length == 0) return nuObjectWithoutArguments(c); // cached!
  
  Constructor m = nuObject_findConstructor(c, args);
  m.setAccessible(true);
  return (A) m.newInstance(args);
} catch (Exception __e) { throw rethrow(__e); } }

static Constructor nuObject_findConstructor(Class c, Object... args) {
  for (Constructor m : c.getDeclaredConstructors()) {
    if (!nuObject_checkArgs(m.getParameterTypes(), args, false))
      continue;
    return m;
  }
  throw fail("Constructor " + c.getName() + getClasses(args) + " not found"
    + (args.length == 0 && (c.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0 ? " - hint: it's a non-static class!" : ""));
}

 static boolean nuObject_checkArgs(Class[] types, Object[] args, boolean debug) {
    if (types.length != args.length) {
      if (debug)
        System.out.println("Bad parameter length: " + args.length + " vs " + types.length);
      return false;
    }
    for (int i = 0; i < types.length; i++)
      if (!(args[i] == null || isInstanceX(types[i], args[i]))) {
        if (debug)
          System.out.println("Bad parameter " + i + ": " + args[i] + " vs " + types[i]);
        return false;
      }
    return true;
  }
static String getClassName(Object o) {
  return o == null ? "null" : o instanceof Class ? ((Class) o).getName() : o.getClass().getName();
}
static boolean isMainProgram() {
  return creator() == null;
}
static Object deref(Object o) {
  if (o instanceof Derefable) o = ((Derefable) o).get();
  return o;
}

static <A, B> Set<A> keys(Map<A, B> map) {
  return map == null ? new HashSet() : map.keySet();
}

static Set keys(Object map) {
  return keys((Map) map);
}






static Object callOpt(Object o) {
  if (o == null) return null;
  return callF(o);
}

static Object callOpt(Object o, String method, Object... args) {
  try {
    if (o == null) return null;
    if (o instanceof Class) {
      Method m = callOpt_findStaticMethod((Class) o, method, args, false);
      if (m == null) return null;
      m.setAccessible(true);
      return invokeMethod(m, null, args);
    } else {
      Method m = callOpt_findMethod(o, method, args, false);
      if (m == null) return null;
      m.setAccessible(true);
      return invokeMethod(m, o, args);
    }
  } catch (Exception e) {
    //fail(e.getMessage() + " | Method: " + method + ", receiver: " + className(o) + ", args: (" + join(", ", map(f className, args) + ")");
    throw rethrow(e);
  }
}

static Method callOpt_findStaticMethod(Class c, String method, Object[] args, boolean debug) {
  Class _c = c;
  while (c != null) {
    for (Method m : c.getDeclaredMethods()) {
      if (debug)
        System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");;
      if (!m.getName().equals(method)) {
        if (debug) System.out.println("Method name mismatch: " + method);
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

static Method callOpt_findMethod(Object o, String method, Object[] args, boolean debug) {
  Class c = o.getClass();
  while (c != null) {
    for (Method m : c.getDeclaredMethods()) {
      if (debug)
        System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");;
      if (m.getName().equals(method) && callOpt_checkArgs(m, args, debug))
        return m;
    }
    c = c.getSuperclass();
  }
  return null;
}

private static boolean callOpt_checkArgs(Method m, Object[] args, boolean debug) {
  Class<?>[] types = m.getParameterTypes();
  if (types.length != args.length) {
    if (debug)
      System.out.println("Bad parameter length: " + args.length + " vs " + types.length);
    return false;
  }
  for (int i = 0; i < types.length; i++)
    if (!(args[i] == null || isInstanceX(types[i], args[i]))) {
      if (debug)
        System.out.println("Bad parameter " + i + ": " + args[i] + " vs " + types[i]);
      return false;
    }
  return true;
}


static JFrame getFrame(final Object _o) {
  return swing(new F0<JFrame>() { JFrame get() { try { 
    Object o = _o;
    if (o instanceof ButtonGroup) o = first(buttonsInGroup((ButtonGroup) o));
    if (!(o instanceof Component)) return null;
    Component c = (Component) o;
    while (c != null) {
      if (c instanceof JFrame) return (JFrame) c;
      c = c.getParent();
    }
    return null;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "O o = _o;\r\n    if (o instanceof ButtonGroup) o = first(buttonsInGroup((Button..."; }});
}
static void copyImageToClipboard(Image img) {
  TransferableImage trans = new TransferableImage(img);
  Toolkit.getDefaultToolkit().getSystemClipboard().setContents( trans, null);
  print("Copied image to clipboard (" + img.getWidth(null) + "*" + img.getHeight(null) + " px)");
}
static void callMainAsChild(Object child, String... args) {
  moveThisThreadToChild(child);
  callMain(child, args);
}
static <A> A restructure(A a) {
  return (A) unstructure(structure(a));
}
static String substring(String s, int x) {
  return substring(s, x, l(s));
}

static String substring(String s, int x, int y) {
  if (s == null) return null;
  if (x < 0) x = 0;
  if (x >= s.length()) return "";
  if (y < x) y = x;
  if (y > s.length()) y = s.length();
  return s.substring(x, y);
}


static double parseDouble(String s) {
  return Double.parseDouble(s);
}
static Object derefRef(Object o) {
  if (o instanceof Concept.Ref) o = ((Concept.Ref) o).get();
  return o;
}

static <A extends Concept> Object[] expandParams(Class<A> c, Object[] params) {
  if (l(params) == 1)
    params = new Object[] { singleFieldName(c), params[0] };
  else
    warnIfOddCount(params);
  return params;
}

static Class<?> hotwire(String src) {
  assertFalse(_inCore());
  Class j = getJavaX();
  if (isAndroid()) {
    synchronized(j) { // hopefully this goes well...
      List<File> libraries = new ArrayList<File>();
      File srcDir = (File) call(j, "transpileMain", src, libraries);
      if (srcDir == null)
        throw fail("transpileMain returned null (src=" + quote(src) + ")");
    
      Object androidContext = get(j, "androidContext");
      return (Class) call(j, "loadx2android", srcDir, src);
    }
  } else {
    
    
    Class c = (Class) ( call(j, "hotwire", src));
    hotwire_copyOver(c);
    return c;
    
  }
}
static boolean isMD5(String s) {
  return l(s) == 32 && isLowerHexString(s);
}
static void revalidateFrame(Component c) {
  revalidate(getFrame(c));
}
static int randomID_defaultLength = 12;

static String randomID(int length) {
  return makeRandomID(length);
}

static String randomID() {
  return randomID(randomID_defaultLength);
}
static String getProgramName_cache;

static synchronized String getProgramName() {
  if (getProgramName_cache == null)
    getProgramName_cache = getSnippetTitleOpt(programID());
  return getProgramName_cache;
}
static int stdcompare(Number a, Number b) {
  return cmp(a, b);
}

static int stdcompare(String a, String b) {
  return cmp(a, b);
}

static int stdcompare(long a, long b) {
  return a < b ? -1 : a > b ? 1 : 0;
}

static int stdcompare(Object a, Object b) {
  return cmp(a, b);
}

static <A> A swingConstruct(final Class<A> c, final Object... args) {
  return swing(new F0<A>() { A get() { try { return  nuObject(c, args) ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "nuObject(c, args)"; }});
}
static boolean startsWith(String a, String b) {
  return a != null && a.startsWith(b);
}

static boolean startsWith(String a, char c) {
  return nempty(a) && a.charAt(0) == c;
}


  static boolean startsWith(String a, String b, Matches m) {
    if (!startsWith(a, b)) return false;
    m.m = new String[] {substring(a, l(b))};
    return true;
  }


static boolean startsWith(List a, List b) {
  if (a == null || l(b) > l(a)) return false;
  for (int i = 0; i < l(b); i++)
    if (neq(a.get(i), b.get(i)))
      return false;
  return true;
}




static <A extends Window> A disposeWindowAfter(int delay, final A w) {
  if (w != null)
    swingLater(delay, new Runnable() { public void run() { try { 
      w.dispose();
    
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "w.dispose();"; }});
  return w;
}
static String reverseString(String s) {
  return new StringBuilder(s).reverse().toString();
}
static void printWithTime(String s) {
  print(hmsWithColons() + ": " + s);
}
static boolean isRunnableX(Object o) {
  if (o == null) return false;
  if (o instanceof String) return hasMethod(mc(), (String) o);
  return o instanceof Runnable || hasMethod(o, "get");
}
static List<Object> record_list = synchroList();

static void record(Object o) {
  record_list.add(o);
}
static boolean isLowerHexString(String s) {
  for (int i = 0; i < l(s); i++) {
    char c = s.charAt(i);
    if (c >= '0' && c <= '9' || c >= 'a' && c <= 'f') {
      // ok
    } else
      return false;
  }
  return true;
}
static Throwable printStackTrace(Throwable e) {
  // we go to system.out now - system.err is nonsense
  print(getStackTrace(e));
  return e;
}

static void printStackTrace() {
  printStackTrace(new Throwable());
}

static void printStackTrace(String msg) {
  printStackTrace(new Throwable(msg));
}

/*static void printStackTrace(S indent, Throwable e) {
  if (endsWithLetter(indent)) indent += " ";
  printIndent(indent, getStackTrace(e));
}*/
static List<String> getPlural_specials = ll("sheep", "fish");

static String getPlural(String s) {
  if (containsIgnoreCase(getPlural_specials, s)) return s;
  if (ewic(s, "y")) return dropSuffixIgnoreCase("y", s) + "ies";
  if (ewic(s, "ss")) return s + "es";
  if (ewic(s, "s")) return s;
  return s + "s";
}
static String replacePrefix(String prefix, String replacement, String s) {
  if (!startsWith(s, prefix)) return s;
  return replacement + substring(s, l(prefix));
}
static JWindow showInTopRightCorner(Component c) {
  JWindow w = new JWindow();
  w.add(c);
  w.pack();
  moveToTopRightCorner(w);
  w.setVisible(true);
  return w;
}
static boolean endsWith(String a, String b) {
  return a != null && a.endsWith(b);
}

static boolean endsWith(String a, char c) {
  return nempty(a) && lastChar(a) == c;
}


  static boolean endsWith(String a, String b, Matches m) {
    if (!endsWith(a, b)) return false;
    m.m = new String[] {dropLast(l(b), a)};
    return true;
  }


static String quickSubstring(String s, int i, int j) {
  if (i == j) return "";
  return s.substring(i, j);
}
static void showConsole() {
  showFrame(consoleFrame());
}
// TODO: extended multi-line strings

static int javaTok_n, javaTok_elements;
static boolean javaTok_opt;

static List<String> javaTok(String s) {
  ++javaTok_n;
  ArrayList<String> tok = new ArrayList();
  int l = s.length();
  
  int i = 0, n = 0;
  while (i < l) {
    int j = i;
    char c, d;
    
    // scan for whitespace
    while (j < l) {
      c = s.charAt(j);
      d = j+1 >= l ? '\0' : s.charAt(j+1);
      if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
        ++j;
      else if (c == '/' && d == '*') {
        do ++j; while (j < l && !s.substring(j, Math.min(j+2, l)).equals("*/"));
        j = Math.min(j+2, l);
      } else if (c == '/' && d == '/') {
        do ++j; while (j < l && "\r\n".indexOf(s.charAt(j)) < 0);
      } else
        break;
    }
    
    tok.add(javaTok_substringN(s, i, j));
    ++n;
    i = j;
    if (i >= l) break;
    c = s.charAt(i);
    d = i+1 >= l ? '\0' : s.charAt(i+1);

    // scan for non-whitespace
    
    // Special JavaX syntax: 'identifier
    if (c == '\'' && Character.isJavaIdentifierStart(d) && i+2 < l && "'\\".indexOf(s.charAt(i+2)) < 0) {
      j += 2;
      while (j < l && Character.isJavaIdentifierPart(s.charAt(j)))
        ++j;
    } else if (c == '\'' || c == '"') {
      char opener = c;
      ++j;
      while (j < l) {
        if (s.charAt(j) == opener /*|| s.charAt(j) == '\n'*/) { // allow multi-line strings
          ++j;
          break;
        } else if (s.charAt(j) == '\\' && j+1 < l)
          j += 2;
        else
          ++j;
      }
    } else if (Character.isJavaIdentifierStart(c))
      do ++j; while (j < l && (Character.isJavaIdentifierPart(s.charAt(j)) || "'".indexOf(s.charAt(j)) >= 0)); // for stuff like "don't"
    else if (Character.isDigit(c)) {
      do ++j; while (j < l && Character.isDigit(s.charAt(j)));
      if (j < l && s.charAt(j) == 'L') ++j; // Long constants like 1L
    } else if (c == '[' && d == '[') {
      do ++j; while (j+1 < l && !s.substring(j, j+2).equals("]]"));
      j = Math.min(j+2, l);
    } else if (c == '[' && d == '=' && i+2 < l && s.charAt(i+2) == '[') {
      do ++j; while (j+2 < l && !s.substring(j, j+3).equals("]=]"));
      j = Math.min(j+3, l);
    } else
      ++j;
      
    tok.add(javaTok_substringC(s, i, j));
    ++n;
    i = j;
  }
  
  if ((tok.size() % 2) == 0) tok.add("");
  javaTok_elements += tok.size();
  return tok;
}

static List<String> javaTok(List<String> tok) {
  return javaTokWithExisting(join(tok), tok);
}
static List<AbstractButton> buttonsInGroup(ButtonGroup g) {
  if (g == null) return ll();
  return asList(g.getElements());
}
static ActionListener actionListener(final Object runnable) {
  if (runnable instanceof ActionListener) return (ActionListener) runnable;
  return new java.awt.event.ActionListener() { public void actionPerformed(java.awt.event.ActionEvent _evt) { try { callF(runnable); } catch (Throwable __e) { messageBox(__e); }}};
}
static void incAtomicLong(AtomicLong l) {
  l.incrementAndGet();
}
static Class __javax;

static Class getJavaX() {
  return __javax;
}
static void setHeaders(URLConnection con) throws IOException {
  
  String computerID = getComputerID_quick();
  if (computerID != null) try {
    con.setRequestProperty("X-ComputerID", computerID);
    con.setRequestProperty("X-OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
  } catch (Throwable e) {
    //printShortException(e);
  }
  
}
static volatile boolean licensed_yes = true;

static boolean licensed() {
  ping();
  return licensed_yes;
}

static void licensed_off() {
  licensed_yes = false;
}
static String exceptionToStringShort(Throwable e) {
  lastException(e);
  e = getInnerException(e);
  String msg = unnull(e.getMessage());
  if (msg.indexOf("Error") < 0 && msg.indexOf("Exception") < 0)
    return baseClassName(e) + prependIfNempty(": ", msg);
  else
    return msg;
}
static Throwable getInnerException(Throwable e) {
  while (e.getCause() != null)
    e = e.getCause();
  return e;
}
static Map<Class, Constructor> nuObjectWithoutArguments_cache = newDangerousWeakHashMap();

static Object nuObjectWithoutArguments(String className) { try {
  return nuObjectWithoutArguments(classForName(className));
} catch (Exception __e) { throw rethrow(__e); } }

static <A> A nuObjectWithoutArguments(Class<A> c) { try {
  Constructor m;
  m = nuObjectWithoutArguments_cache.get(c);
  if (m == null)
    nuObjectWithoutArguments_cache.put(c, m = nuObjectWithoutArguments_findConstructor(c));
  return (A) m.newInstance();
} catch (Exception __e) { throw rethrow(__e); } }

static Constructor nuObjectWithoutArguments_findConstructor(Class c) {
  for (Constructor m : c.getDeclaredConstructors())
    if (empty(m.getParameterTypes())) {
      m.setAccessible(true);
      return m;
    }
  throw fail("No default constructor found in " + c.getName());
}

static <A> A optParam(ThreadLocal<A> tl, A defaultValue) {
  return optPar(tl, defaultValue);
}

static <A> A optParam(ThreadLocal<A> tl) {
  return optPar(tl);
}
// c = JComponent or something implementing swing()
static JComponent wrap(Object swingable) {
  if (swingable == null) return null;
  JComponent c = (JComponent) ( swingable instanceof JComponent ? swingable : callOpt(swingable, "swing"));
  if (c instanceof JTable || c instanceof JList
    || c instanceof JTextArea || c instanceof JEditorPane
    || c instanceof JTextPane || c instanceof JTree)
    return jscroll(c);
  return c;
}

static boolean hasField(Object o, String field) {
  return findField2(o, field) != null;
}
// match2 matches multiple "*" (matches a single token) wildcards and zero or one "..." wildcards (matches multiple tokens)

static String[] match2(List<String> pat, List<String> tok) {
  // standard case (no ...)
  int i = pat.indexOf("...");
  if (i < 0) return match2_match(pat, tok);
  
  pat = new ArrayList<String>(pat); // We're modifying it, so copy first
  pat.set(i, "*");
  while (pat.size() < tok.size()) {
    pat.add(i, "*");
    pat.add(i+1, ""); // doesn't matter
  }
  
  return match2_match(pat, tok);
}

static String[] match2_match(List<String> pat, List<String> tok) {
  List<String> result = new ArrayList<String>();
  if (pat.size() != tok.size()) {
    
    return null;
  }
  for (int i = 1; i < pat.size(); i += 2) {
    String p = pat.get(i), t = tok.get(i);
    
    if (eq(p, "*"))
      result.add(t);
    else if (!equalsIgnoreCase(unquote(p), unquote(t))) // bold change - match quoted and unquoted now
      return null;
  }
  return result.toArray(new String[result.size()]);
}

static Boolean isHeadless_cache;

static boolean isHeadless() {
  if (isHeadless_cache != null) return isHeadless_cache;
  if (GraphicsEnvironment.isHeadless()) return isHeadless_cache = true;
  
  // Also check if AWT actually works.
  // If DISPLAY variable is set but no X server up, this will notice.
  
  try {
    SwingUtilities.isEventDispatchThread();
    return isHeadless_cache = false;
  } catch (Throwable e) { return isHeadless_cache = true; }
}
static Class<?> _getClass(String name) {
  try {
    return Class.forName(name);
  } catch (ClassNotFoundException e) {
    return null; // could optimize this
  }
}

static Class _getClass(Object o) {
  return o == null ? null
    : o instanceof Class ? (Class) o : o.getClass();
}

static Class _getClass(Object realm, String name) { try {
  return getClass(realm).getClassLoader().loadClass(classNameToVM(name));
} catch (Exception __e) { throw rethrow(__e); } }
static boolean instanceOf(Object o, String className) {
  if (o == null) return false;
  String c = o.getClass().getName();
  return eq(c, className) || eq(c, "main$" + className);
}

static boolean instanceOf(Object o, Class c) {
  if (c == null) return false;
  return c.isInstance(o);
}
static String getStackTrace(Throwable throwable) {
  lastException(throwable);
  return getStackTrace_noRecord(throwable);
}

static String getStackTrace_noRecord(Throwable throwable) {
  StringWriter writer = new StringWriter();
  throwable.printStackTrace(new PrintWriter(writer));
  return hideCredentials(writer.toString());
}

static String getStackTrace() {
  return getStackTrace_noRecord(new Throwable());
}
static String getProgramTitle() {
  return getProgramName();
}
static String uploadToImageServer_new(BufferedImage img, String name) {
  byte[] imgData = toPNG(img);
  String page = postPage("http://ai1.space/images/raw/upload", "data", bytesToHex(imgData), "name", name);
  print(page);
  Matcher m = Pattern.compile("/raw/([0-9]+)").matcher(page);
  if (!m.find()) return null;
  return "http://ai1.lol/images/raw/" + m.group(1);
}
static File getCodeProgramDir() {
  return getCodeProgramDir(getProgramID());
}

static File getCodeProgramDir(String snippetID) {
  return new File(javaxCodeDir(), formatSnippetID(snippetID));
}

static File getCodeProgramDir(long snippetID) {
  return getCodeProgramDir(formatSnippetID(snippetID));
}
static URLConnection openConnection(URL url) { try {
  ping();
  
  callOpt(javax(), "recordOpenURLConnection", str(url));
  
  return url.openConnection();
} catch (Exception __e) { throw rethrow(__e); } }
static boolean hasMethod(Object o, String method, Object... args) {
  return findMethod(o, method, args) != null;
}
static KeyListener enterKeyListener(final Object action) {
  return new KeyAdapter() {
    public void keyReleased(KeyEvent ke) {
      if (ke.getKeyCode() == KeyEvent.VK_ENTER)
        pcallF(action);
    }
  };
}
static void setContentPane(final JFrame frame, final Container c) {
  { swing(new Runnable() { public void run() { try { 
    frame.setContentPane(c);
    revalidate(frame);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "frame.setContentPane(c);\r\n    revalidate(frame);"; }}); }
}
static File infoBoxesLogFile() {
  return new File(javaxDataDir(), "Logs/infoBoxes.txt");
}
static AtomicInteger dialogServer_clients = new AtomicInteger();
static boolean dialogServer_printConnects;
static ThreadLocal<Boolean> startDialogServer_quiet = new ThreadLocal();

static Set<String> dialogServer_knownClients = synchroTreeSet();

static int startDialogServerOnPortAbove(int port, DialogHandler handler) {
  while (!forbiddenPort(port) && !startDialogServerIfPortAvailable(port, handler))
    ++port;
  return port;
}

static int startDialogServerOnPortAboveDaemon(int port, DialogHandler handler) {
  while (!forbiddenPort(port) && !startDialogServerIfPortAvailable(port, handler, true))
    ++port;
  return port;
}

static void startDialogServer(int port, DialogHandler handler) {
  if (!startDialogServerIfPortAvailable(port, handler))
    throw fail("Can't start dialog server on port " + port);
}

static boolean startDialogServerIfPortAvailable(int port, final DialogHandler handler) {
  return startDialogServerIfPortAvailable(port, handler, false);
}

static ServerSocket startDialogServer_serverSocket;
  
static boolean startDialogServerIfPortAvailable(int port, final DialogHandler handler, boolean daemon) {
  ServerSocket serverSocket = null;
  try {
    serverSocket = new ServerSocket(port);
  } catch (IOException e) {
    // probably the port number is used - let's assume there already is a chat server.
    return false;
  }
  final ServerSocket _serverSocket = serverSocket;
  startDialogServer_serverSocket = serverSocket;

  Thread thread = new Thread("Socket accept port " + port) { public void run() {
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
            final BufferedReader in = new BufferedReader(
              new InputStreamReader(s.getInputStream(), "UTF-8"));
              
            DialogIO io = new DialogIO() {
            
              // This should be the same as #1001076 (talkTo)
            
  boolean isLocalConnection() {
    return s.getInetAddress().isLoopbackAddress();
  }
  
  boolean isStillConnected() {
    return !(eos || s.isClosed());
  }
  
  void sendLine(String line) { try {
    w.write(line + "\n");
    w.flush();
  } catch (Exception __e) { throw rethrow(__e); } }
  
  String readLineImpl() { try {
    return in.readLine();
  } catch (Exception __e) { throw rethrow(__e); } }
  
  void close() {
    try {
      s.close();
    } catch (IOException e) {
      // whatever
    }
  }
  
  Socket getSocket() {
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
            //print("client disconnect - " + dialogServer_clients.decrementAndGet() + " remaining");
          }
         }
        }; // Thread t2
        t2.setDaemon(true); // ?
        t2.start();
      } catch (SocketTimeoutException e) {
      }
    }   
   } catch (IOException e) {
     print("[internal] " + e);
   }
  }};
  if (daemon) thread.setDaemon(true);
  thread.start();
 
  if (!isTrue(getAndClearThreadLocal(startDialogServer_quiet)))
    print("Dialog server on port " + port + " started."); 
  return true;
}
static void frameStandardSize(JFrame frame) {
  frame.setBounds(300, 100, 500, 400);
}
static void logQuotedWithDate(String s) {
  logQuotedWithTime(s);
}

static void logQuotedWithDate(String logFile, String s) {
  logQuotedWithTime(logFile, s);
}

static void logQuotedWithDate(File logFile, String s) {
  logQuotedWithTime(logFile, s);
}
static String md5OfRGBImage(RGBImage img) { try {
  MessageDigest m = MessageDigest.getInstance("MD5");
  m.update(intToBytes(img.getWidth()));
  int[] pixels = img.getPixels();
  for (int i = 0; i < l(pixels); i++)
    m.update(intToBytes(pixels[i]));
  return bytesToHex(m.digest());
} catch (Exception __e) { throw rethrow(__e); } }
static String makeRandomID(int length) {
  Random random = new Random();
  char[] id = new char[length];
  for (int i = 0; i < id.length; i++)
    id[i] = (char) ((int) 'a' + random.nextInt(26));
  return new String(id);
}
static boolean containsIgnoreCase(Collection<String> l, String s) {
  for (String x : l)
    if (eqic(x, s))
      return true;
  return false;
}

static boolean containsIgnoreCase(String[] l, String s) {
  for (String x : l)
    if (eqic(x, s))
      return true;
  return false;
}

static boolean containsIgnoreCase(String s, char c) {
  return indexOfIgnoreCase(s, String.valueOf(c)) >= 0;
}

static boolean containsIgnoreCase(String a, String b) {
  return indexOfIgnoreCase(a, b) >= 0;
}
public static byte[] loadBinaryFile(String fileName) {
 try {
  if (!new File(fileName).exists())
    return null;

  FileInputStream in = new FileInputStream(fileName);

  byte buf[] = new byte[1024];
  ByteArrayOutputStream out = new ByteArrayOutputStream();
  int l;
  while (true) {
    l = in.read(buf);
    if (l <= 0) break;
    out.write(buf, 0, l);
  }
  in.close();
  return out.toByteArray();
 } catch (IOException e) { throw new RuntimeException(e); }
}

public static byte[] loadBinaryFile(File file) {
  return loadBinaryFile(file.getPath());
}

static Runnable _topLevelErrorHandling(final Runnable runnable) {
  return new Runnable() { public void run() { try {  try { runnable.run(); } catch (Throwable __e) { printStackTrace2(__e); } 
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "pcall { runnable.run(); }"; }};
}
static String struct(Object o) {
  return structure(o);
}

static String struct(Object o, structure_Data data) {
  return structure(o, data);
}
static Class getOuterClass(Class c) { try {
  String s = c.getName();
  int i = s.lastIndexOf('$');
  return Class.forName(substring(s, 0, i));
} catch (Exception __e) { throw rethrow(__e); } }
static <A> ArrayList<A> cloneList(Collection<A> l) {
  if (l == null) return new ArrayList();
  synchronized(collectionMutex(l)) {
    return new ArrayList<A>(l);
  }
}
static JTextArea jtextarea() {
  return jTextArea();
}

static JTextArea jtextarea(String text) {
  return jTextArea(text);
}
static Object safeUnstructure(String s) {
  return unstructure(s, true);
}
static boolean odd(int i) {
  return (i & 1) != 0;
}

static boolean odd(long i) {
  return (i & 1) != 0;
}
static List _registerWeakMap_preList;

static <A> A _registerWeakMap(A map) {
  if (javax() == null) {
    // We're in class init
    if (_registerWeakMap_preList == null) _registerWeakMap_preList = synchroList();
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

static void _onLoad_registerWeakMap() {
  assertNotNull(javax());
  if (_registerWeakMap_preList == null) return;
  for (Object o : _registerWeakMap_preList)
    _registerWeakMap(o);
  _registerWeakMap_preList = null;
}
static TimerTask timerTask(final Object r, final java.util.Timer timer) {
  return new TimerTask() {
    public void run() {
      if (!licensed())
        timer.cancel();
      else
        pcallF(r);
    }
  };
}

static TimerTask timerTask(final Object r) {
  return new TimerTask() {
    public void run() {
      ping();
      pcallF(r);
    }
  };
}
static int cmp(Number a, Number b) {
  return a == null ? b == null ? 0 : -1 : cmp(a.doubleValue(), b.doubleValue());
}

static int cmp(double a, double b) {
  return a < b ? -1 : a == b ? 0 : 1;
}

static int cmp(String a, String b) {
  return a == null ? b == null ? 0 : -1 : a.compareTo(b);
}

static int cmp(Object a, Object b) {
  if (a == null) return b == null ? 0 : -1;
  if (b == null) return 1;
  return ((Comparable) a).compareTo(b);
}
static String myJavaSource_code;

static String myJavaSource() {
  return myJavaSource_code;
}
static boolean _inCore() {
  return false;
}
static int jfind(String s, String in) {
  return jfind(javaTok(s), in);
}

static int jfind(List<String> tok, String in) {
  return jfind(tok, 1, in);
}

static int jfind(List<String> tok, int startIdx, String in) {
  return jfind(tok, startIdx, in, null);
}

static int jfind(List<String> tok, String in, Object condition) {
  return jfind(tok, 1, in, condition);
}

static int jfind(List<String> tok, int startIdx, String in, Object condition) {
  List<String> tokin = javaTok(in);
  jfind_preprocess(tokin);
  return jfind(tok, startIdx, tokin, condition);
}

// assumes you preprocessed tokin
static int jfind(List<String> tok, List<String> tokin) {
  return jfind(tok, 1, tokin);
}

static int jfind(List<String> tok, int startIdx, List<String> tokin) {
  return jfind(tok, startIdx, tokin, null);
}

static int jfind(List<String> tok, int startIdx, List<String> tokin, Object condition) {
  return findCodeTokens(tok, startIdx, false, toStringArray(codeTokensOnly(tokin)), condition);
}

static List<String> jfind_preprocess(List<String> tok) {
  for (String type : litlist("quoted", "id", "int"))
    replaceSublist(tok, ll("<", "", type, "", ">"), ll("<" + type + ">"));
  replaceSublist(tok, ll("\\", "", "*"), ll("\\*"));
  return tok;
}
static String parse3_cached_s;
static List<String> parse3_cached_l;

  static synchronized List<String> parse3_cached(String s) {
    if (neq(s, parse3_cached_s))
      parse3_cached_l = parse3(parse3_cached_s = s);
    return parse3_cached_l;
  }
static boolean endsWithLetter(String s) {
  return nempty(s) && isLetter(last(s));
}
static WeakReference<Class> creator_class;

static Class creator() {
  return creator_class == null ? null : creator_class.get();
}
static String getDBProgramID_id;

static String getDBProgramID() {
  return nempty(getDBProgramID_id) ? getDBProgramID_id : programIDWithCase();
}
static int max(int a, int b) { return Math.max(a, b); }
static int max(int a, int b, int c) { return max(max(a, b), c); }
static long max(int a, long b) { return Math.max((long) a, b); }
static long max(long a, long b) { return Math.max(a, b); }
static double max(int a, double b) { return Math.max((double) a, b); }
static float max(float a, float b) { return Math.max(a, b); }
static double max(double a, double b) { return Math.max(a, b); }

static int max(Collection<Integer> c) {
  int x = Integer.MIN_VALUE;
  for (int i : c) x = max(x, i);
  return x;
}

static double max(double[] c) {
  if (c.length == 0) return Double.MIN_VALUE;
  double x = c[0];
  for (int i = 1; i < c.length; i++) x = Math.max(x, c[i]);
  return x;
}

static float max(float[] c) {
  if (c.length == 0) return Float.MAX_VALUE;
  float x = c[0];
  for (int i = 1; i < c.length; i++) x = Math.max(x, c[i]);
  return x;
}

static byte max(byte[] c) {
  byte x = -128;
  for (byte d : c) if (d > x) x = d;
  return x;
}

static short max(short[] c) {
  short x = -0x8000;
  for (short d : c) if (d > x) x = d;
  return x;
}

static int max(int[] c) {
  int x = Integer.MIN_VALUE;
  for (int d : c) if (d > x) x = d;
  return x;
}




static volatile boolean disableCertificateValidation_attempted;

static void disableCertificateValidation() { try {
  if (disableCertificateValidation_attempted) return;
  disableCertificateValidation_attempted = true;
  
  try {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] { 
      new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() { 
          return new X509Certificate[0]; 
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
    }};
  
    // Ignore differences between given hostname and certificate hostname
    HostnameVerifier hv = new HostnameVerifier() {
      public boolean verify(String hostname, SSLSession session) { return true; }
    };
  
    // Install the all-trusting trust manager
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(hv);
  } catch (Throwable __e) { printStackTrace2(__e); }
} catch (Exception __e) { throw rethrow(__e); } }
static Field getOpt_findField(Class<?> c, String field) {
  Class _c = c;
  do {
    for (Field f : _c.getDeclaredFields())
      if (f.getName().equals(field))
        return f;
    _c = _c.getSuperclass();
  } while (_c != null);
  return null;
}
static void onClick(JComponent c, final Object runnable) {
  c.addMouseListener(new MouseAdapter() {
    public void mouseClicked(MouseEvent e) {
      callF(runnable, e);
    }
  });
}

// re-interpreted for buttons
static void onClick(JButton btn, final Object runnable) {
  onEnter(btn, runnable);
}
static boolean publicCommOn() {
  return "1".equals(loadTextFile(new File(userHome(), ".javax/public-communication")));
}


static <A extends JTextComponent> A jenableUndoRedo(A textcomp) {
  final UndoManager undo = new UndoManager();
  textcomp.getDocument().addUndoableEditListener(new UndoableEditListener() {
    public void undoableEditHappened(UndoableEditEvent evt) {
      undo.addEdit(evt.getEdit());
    }
  });
  
  textcomp.getActionMap().put("Undo", abstractAction("Undo", new Runnable() { public void run() { try { 
    if (undo.canUndo()) undo.undo()
  ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (undo.canUndo()) undo.undo()"; }}));
  textcomp.getActionMap().put("Redo", abstractAction("Redo", new Runnable() { public void run() { try { 
    if (undo.canRedo()) undo.redo()
  ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (undo.canRedo()) undo.redo()"; }}));
  textcomp.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
  textcomp.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
  return textcomp;
}
static String singleFieldName(Class c) {
  Set<String> l = listFields(c);
  if (l(l) != 1)
    throw fail("No single field found in " + c + " (have " + n(l(l), "fields") + ")");
  return first(l);
}
static JWindow showWindow(Component c) {
  JWindow w = new JWindow();
  w.add(wrap(c));
  return w;
}
static <A> void scanForComponents(Component c, Class<A> theClass, List<A> l) {
  if (theClass.isInstance(c))
    l.add((A) c);
  if (c instanceof Container)
    for (Component comp : ((Container) c).getComponents())
      scanForComponents(comp, theClass, l);
}
static Map synchroMap() {
  return synchroHashMap();
}

static <A, B> Map<A, B> synchroMap(Map<A, B> map) {
  return Collections.synchronizedMap(map);
}
static boolean tableSetColumnPreferredWidths_debug;

static void tableSetColumnPreferredWidths(final JTable table, final Map<String, Integer> widths) {
  { swing(new Runnable() { public void run() { try {  try {
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
  } catch (Throwable __e) { printStackTrace2(__e); }
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "pcall {\r\n    TableColumnModel tcm = table.getColumnModel();\r\n    int n = tcm...."; }}); }
}

static void tableSetColumnPreferredWidths(JTable table, Object... widths) {
  tableSetColumnPreferredWidths(table, litorderedmap(widths));
}

static volatile Android3 dbBot_instance;

static Android3 dbBot() {
  return dbBot(dbBotStandardName());
}

static Android3 dbBot(String name) {
  ensureDBNotRunning(name);
  assertNotNull(mainConcepts);
  return dbBot_instance = methodsBot2(name, mainConcepts, exposedDBMethods, mainConcepts.lock);
}
//static final Map<Class, HashMap<S, Field>> getOpt_cache = newDangerousWeakHashMap(f getOpt_special_init);

static class getOpt_Map extends WeakHashMap {
  getOpt_Map() {
    if (getOpt_special == null) getOpt_special = new HashMap();
    clear();
  }
  
  public void clear() {
    super.clear();
    //print("getOpt clear");
    put(Class.class, getOpt_special);
    put(String.class, getOpt_special);
  }
}

static final Map<Class, HashMap<String, Field>> getOpt_cache = _registerDangerousWeakMap(synchroMap(new getOpt_Map()));
//static final Map<Class, HashMap<S, Field>> getOpt_cache = _registerWeakMap(synchroMap(new getOpt_Map));
static HashMap getOpt_special; // just a marker

/*static void getOpt_special_init(Map map) {
  map.put(Class.class, getOpt_special);
  map.put(S.class, getOpt_special);
}*/

static Object getOpt_cached(Object o, String field) { try {
  if (o == null) return null;

  Class c = o.getClass();
  HashMap<String, Field> map;
  synchronized(getOpt_cache) {
    map = getOpt_cache.get(c);
    if (map == null)
      map = getOpt_makeCache(c);
  }
  
  if (map == getOpt_special) {
    if (o instanceof Class)
      return getOpt((Class) o, field);
    /*if (o instanceof S)
      ret getOpt(getBot((S) o), field);*/
    if (o instanceof Map)
      return ((Map) o).get(field);
  }
    
  Field f = map.get(field);
  if (f != null) return f.get(o);
  
    if (o instanceof DynamicObject)
      return ((DynamicObject) o).fieldValues.get(field);
  
  return null;
} catch (Exception __e) { throw rethrow(__e); } }

// used internally - we are in synchronized block
static HashMap<String, Field> getOpt_makeCache(Class c) {
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
static String dbBotStandardName() {
  return dbBotName(getDBProgramID()) + ".";
}
static Object sleepQuietly_monitor = new Object();

static void sleepQuietly() { try {
  assertFalse(isAWTThread());
  synchronized(sleepQuietly_monitor) { sleepQuietly_monitor.wait(); }
} catch (Exception __e) { throw rethrow(__e); } }
static Map<String, Object> rawTableLineAsMap(JTable tbl, int row) {
  if (row >= 0 && row < tbl.getModel().getRowCount()) {
    Map<String, Object> map = litorderedmap(); // keep order of columns
    for (int i = 0; i < tbl.getModel().getColumnCount(); i++)
      mapPut(map, tbl.getModel().getColumnName(i),
        tbl.getModel().getValueAt(row, i));
    return map;
  }
  return null;
}
static String structureForUser(Object o) {
  return beautifyStructure(struct_noStringSharing(o));
}
static int toInt(Object o) {
  if (o == null) return 0;
  if (o instanceof Number)
    return ((Number) o).intValue();
  if (o instanceof String)
    return parseInt((String) o);
  throw fail("woot not int: " + getClassName(o));
}

static int toInt(long l) {
  if (l != (int) l) throw fail("Too large for int: " + l);
  return (int) l;
}
static <A> A or(A a, A b) {
  return a != null ? a : b;
}
public static String rtrim(String s) {
  if (s == null) return null;
  int i = s.length();
  while (i > 0 && " \t\r\n".indexOf(s.charAt(i-1)) >= 0)
    --i;
  return i < s.length() ? s.substring(0, i) : s;
}
static String dropSuffix(String suffix, String s) {
  return s.endsWith(suffix) ? s.substring(0, l(s)-l(suffix)) : s;
}
static String callStaticAnswerMethod(List bots, String s) {
  for (Object c : bots) try {
    String answer = callStaticAnswerMethod(c, s);
    if (!empty(answer)) return answer;
  } catch (Throwable e) {
    print("Error calling " + getProgramID(c));
    e.printStackTrace();
  }
  return null;
}

static String callStaticAnswerMethod(Object c, String s) {
  String answer = (String) callOpt(c, "answer", s, litlist(s));
  if (answer == null)
    answer = (String) callOpt(c, "answer", s);
  return emptyToNull(answer);
}

static String callStaticAnswerMethod(String s) {
  return callStaticAnswerMethod(mc(), s);
}

static String callStaticAnswerMethod(String s, List<String> history) {
  return callStaticAnswerMethod(mc(), s, history);
}

static String callStaticAnswerMethod(Object c, String s, List<String> history) {
  String answer = (String) callOpt(c, "answer", s, history);
  if (answer == null)
    answer = (String) callOpt(c, "answer", s);
  return emptyToNull(answer);
}
static void smartSet(Field f, Object o, Object value) throws Exception {
  f.setAccessible(true);
  
  // take care of common case (long to int)
  if (f.getType() == int.class && value instanceof Long)
    value = ((Long) value).intValue();
    
  try {
    f.set(o, value);
  } catch (Exception e) {
    
      try {
        if (f.getType() == Concept.Ref.class) {
          f.set(o, ((Concept) o).new Ref((Concept) value));
          return;
        }
        if (o instanceof Concept.Ref) {
          f.set(o, ((Concept.Ref) o).get());
          return;
        }
      } catch (Throwable _e) {}
    
    throw e;
  }
}
static String tb_mainServer_default = "http://tinybrain.de:8080";
static Object tb_mainServer_override; // func -> S

static String tb_mainServer() {
  if (tb_mainServer_override != null) return (String) callF(tb_mainServer_override);
  return trim(loadTextFile(tb_mainServer_file(),
    tb_mainServer_default));
}

static File tb_mainServer_file() {
  return getProgramFile("#1001638", "mainserver.txt");
}

static boolean tb_mainServer_isDefault() {
  return eq(tb_mainServer(), tb_mainServer_default);
}
static Map<String, Integer> tableColumnWidthsByName(JTable table) {
  TableColumnModel tcm = table.getColumnModel();
  int n = tcm.getColumnCount();
  TreeMap<String,Integer> map = new TreeMap();
  for (int i = 0; i < n; i++) {
    TableColumn tc = tcm.getColumn(i);
    map.put(str(tc.getHeaderValue()), tc.getWidth());
  }
  return map;
}
static int moveToTopRightCorner_inset = 20;

static <A extends Component> A moveToTopRightCorner(A a) {
  Window w = getWindow(a);
  if (w != null)
    w.setLocation(getScreenSize().width-w.getWidth()-moveToTopRightCorner_inset, moveToTopRightCorner_inset);
  return a;
}

static Object callFunction(Object f, Object... args) {
  return callF(f, args);
}
static JPanel infoMessage_makePanel(String text) {
  final JTextArea ta = wrappedTextArea(text);
  onClick(ta, new Runnable() { public void run() { try {  disposeWindow(ta) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "disposeWindow(ta)"; }});
  int size = 14;
  if (l(text) <= 50) size *= 2;
  else if (l(text) < 100) size = iround(size*1.5);
  ta.setFont(typeWriterFont(size));
  JScrollPane sp = jscroll(ta);
  return withMargin(sp);
}
static HashMap<String, List<Method>> callMC_cache = new HashMap();
static String callMC_key;
static Method callMC_value;

// varargs assignment fixer for a single string array argument
static Object callMC(String method, String[] arg) {
  return callMC(method, new Object[] {arg});
}

static Object callMC(String method, Object... args) { try {
  Method me;
  if (callMC_cache == null) callMC_cache = new HashMap(); // initializer time workaround
  synchronized(callMC_cache) {
    me = method == callMC_key ? callMC_value : null;
  }
  if (me != null) try {
    return invokeMethod(me, null, args);
  } catch (IllegalArgumentException e) {
    throw new RuntimeException("Can't call " + me + " with arguments " + classNames(args), e);
  }

  List<Method> m;
  synchronized(callMC_cache) {
    m = callMC_cache.get(method);
  }
  if (m == null) {
    if (callMC_cache.isEmpty()) {
      callMC_makeCache();
      m = callMC_cache.get(method);
    }
    if (m == null) throw fail("Method named " + method + " not found in main");
  }
  int n = m.size();
  if (n == 1) {
    me = m.get(0);
    synchronized(callMC_cache) {
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
} catch (Exception __e) { throw rethrow(__e); } }

static void callMC_makeCache() {
  synchronized(callMC_cache) {
    callMC_cache.clear();
    Class _c = (Class) mc(), c = _c;
    while (c != null) {
      for (Method m : c.getDeclaredMethods())
        if ((m.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0) {
          m.setAccessible(true);
          multiMapPut(callMC_cache, m.getName(), m);
        }
      c = c.getSuperclass();
    }
  }
}
static Method fastIntern_method;

static String fastIntern(String s) { try {
  if (s == null) return null;
  if (fastIntern_method == null) {
    fastIntern_method = findMethodNamed(javax(), "internPerProgram");
    if (fastIntern_method == null) upgradeJavaXAndRestart();
  }
    
  return (String) fastIntern_method.invoke(null, s);
} catch (Exception __e) { throw rethrow(__e); } }
static void setFrameContents(final Component c, final Object contents) {
  { swing(new Runnable() { public void run() { try { 
    JFrame frame = getFrame(c);
    frame.getContentPane().removeAll();
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(wrap(contents));
    revalidate(frame);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "JFrame frame = getFrame(c);\r\n    frame.getContentPane().removeAll();\r\n    fra..."; }}); }
}
// TODO: test if android complains about this
static boolean isAWTThread() {
  if (isAndroid()) return false;
  if (isHeadless()) return false;
  return isAWTThread_awt();
}

static boolean isAWTThread_awt() {
  return SwingUtilities.isEventDispatchThread();
}
static Thread currentThread() {
  return Thread.currentThread();
}
static JPanel smartAdd(JPanel panel, List parts) {
  for (Object o : parts)
    addToContainer(panel, wrapForSmartAdd(o));
  return panel;
}

static JPanel smartAdd(JPanel panel, Object... parts) {
  return smartAdd(panel, asList(parts));
}

static void removeFromMultiPort(long vport) {
  if (vport == 0) return;
  for (Object port : getMultiPorts())
    call(port, "removePort", vport);
}
 // Substance
 // Trident (required by Substance)




static void enableSubstance_impl(final String skinName) {
  if (headless()) return;
  { swing(new Runnable() { public void run() { try { 
    if (!substanceLookAndFeelEnabled())
      enableSubstance_impl_2(skinName);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (!substanceLookAndFeelEnabled())\r\n      enableSubstance_impl_2(skinName);"; }}); }
}

static void enableSubstance_impl_2(String skinName) { try {
  boolean wasEnabled = substanceLookAndFeelEnabled();
  ClassLoader cl = main.class.getClassLoader();
  UIManager.getDefaults().put("ClassLoader", cl);
  Thread.currentThread().setContextClassLoader(cl);
  String skinClassName = "org.pushingpixels.substance.api.skin." + addSuffix(skinName, "Skin");
  SubstanceSkin skin = (SubstanceSkin) nuObject(cl.loadClass(skinClassName));
  SubstanceLookAndFeel.setSkin(skin);
  JFrame.setDefaultLookAndFeelDecorated(true);  
  updateLookAndFeelOnAllWindows_noRenew();
  if (!wasEnabled) renewConsoleFrame();
  
  if (substanceLookAndFeelEnabled())
    print("Substance L&F enabled.");
  else
    print("Could not enable Substance L&F?");
} catch (Exception __e) { throw rethrow(__e); } }
static JPanel westAndCenter(final Component w, final Component c) {
  return swing(new F0<JPanel>() { JPanel get() { try { 
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(BorderLayout.WEST, wrap(w));
    panel.add(BorderLayout.CENTER, wrap(c));
    return panel;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "JPanel panel = new JPanel(new BorderLayout);\r\n    panel.add(BorderLayout.WEST..."; }});
}
static Map<String, String> singular_specials = litmap(
  "children", "child", "images", "image", "chess", "chess");
  
static Set<String> singular_specials2 = litset("time", "machine", "line");

static String singular(String s) {
  if (s == null) return null;
  { String _a_742 = singular_specials.get(s); if (!empty(_a_742)) return _a_742; }
  { String _a_743 = hippoSingulars().get(lower(s)); if (!empty(_a_743)) return _a_743; }
  if (singular_specials2.contains(dropSuffix("s", lastWord(s))))
    return dropSuffix("s", s);
  if (s.endsWith("ness")) return s;
  if (s.endsWith("ges")) return dropSuffix("s", s);
  if (endsWith(s, "bases")) return dropLast(s);
  s = dropSuffix("es", s);
  s = dropSuffix("s", s);
  return s;
}
static Object addToMultiPort_responder;

static long addToMultiPort(final String botName) {
  return addToMultiPort(botName, new Object() {
    public String answer(String s, List<String> history) {
      String answer = (String) ( callOpt(getMainClass(), "answer", s, history));
      if (answer != null) return answer;
      answer = (String) callOpt(getMainClass(), "answer", s);
      if (answer != null) return answer;
      if (match3("get injection id", s))
        return getInjectionID();
      return null;
    }
  });
}

static long addToMultiPort(final String botName, final Object responder) {
  //print(botName);
  addToMultiPort_responder = responder;
  startMultiPort();
  List ports = getMultiPorts();
  if (ports == null) return 0;
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

static <A> A liftLast(List<A> l) {
  if (l.isEmpty()) return null;
  int i = l(l)-1;
  A a = l.get(i);
  l.remove(i);
  return a;
}
static String programID;

static String getProgramID() {
  return nempty(programID) ? formatSnippetIDOpt(programID) : "?";
}


// TODO: ask JavaX instead
static String getProgramID(Class c) {
  String id = (String) getOpt(c, "programID");
  if (nempty(id))
    return formatSnippetID(id);
  return "?";
}


static String getProgramID(Object o) {
  return getProgramID(getMainClass(o));
}
static String unnull(String s) {
  return s == null ? "" : s;
}

static <A> List<A> unnull(List<A> l) {
  return l == null ? emptyList() : l;
}

static <A, B> Map<A, B> unnull(Map<A, B> l) {
  return l == null ? emptyMap() : l;
}

static <A> Iterable<A> unnull(Iterable<A> i) {
  return i == null ? emptyList() : i;
}

static <A> A[] unnull(A[] a) {
  return a == null ? (A[]) new Object[0] : a;
}

static BitSet unnull(BitSet b) {
  return b == null ? new BitSet() : b;
}


static Pt unnull(Pt p) {
  return p == null ? new Pt() : p;
}



static int min(int a, int b) {
  return Math.min(a, b);
}

static long min(long a, long b) {
  return Math.min(a, b);
}

static float min(float a, float b) { return Math.min(a, b); }
static float min(float a, float b, float c) { return min(min(a, b), c); }

static double min(double a, double b) {
  return Math.min(a, b);
}

static double min(double[] c) {
  double x = Double.MAX_VALUE;
  for (double d : c) x = Math.min(x, d);
  return x;
}

static float min(float[] c) {
  float x = Float.MAX_VALUE;
  for (float d : c) x = Math.min(x, d);
  return x;
}

static byte min(byte[] c) {
  byte x = 127;
  for (byte d : c) if (d < x) x = d;
  return x;
}

static short min(short[] c) {
  short x = 0x7FFF;
  for (short d : c) if (d < x) x = d;
  return x;
}

static int min(int[] c) {
  int x = Integer.MAX_VALUE;
  for (int d : c) if (d < x) x = d;
  return x;
}
static String hideCredentials(URL url) { return url == null ? null : hideCredentials(str(url)); }

static String hideCredentials(String url) {
  return url.replaceAll("([&?])_pass=[^&\\s\"]*", "$1_pass=<hidden>");
}

static String hideCredentials(Object o) {
  return hideCredentials(str(o));
}
static Object[] flattenArray2(Object... a) {
  List l = new ArrayList();
  for (Object x : a)
    if (x instanceof Object[])
      l.addAll(asList((Object[]) x));
    else if (x instanceof Collection)
      l.addAll((Collection) x);
    else
      l.add(x);
  return asObjectArray(l);
}
static String getSnippetTitleOpt(String s) {
  return isSnippetID(s) ? getSnippetTitle(s) : s;
}
static String strOrEmpty(Object o) {
  return o == null ? "" : str(o);
}
static Rectangle defaultNewFrameBounds_r = new Rectangle(300, 100, 500, 400);

static Rectangle defaultNewFrameBounds() {
  return swing(new F0<Rectangle>() { Rectangle get() { try { 
    defaultNewFrameBounds_r.translate(60, 20);
    if (!screenRectangle().contains(defaultNewFrameBounds_r))
      defaultNewFrameBounds_r.setLocation(30+random(30), 20+random(20));
    return new Rectangle(defaultNewFrameBounds_r);
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "defaultNewFrameBounds_r.translate(60, 20);\r\n    if (!screenRectangle().contai..."; }});
}
static boolean frameTooSmall(JFrame frame) {
  return frame.getWidth() < 100 || frame.getHeight() < 50;
}
static String indentx(String s) {
  return indentx(indent_default, s);
}

static String indentx(int n, String s) {
  return dropSuffix(repeat(' ', n), indent(n, s));
}

static String indentx(String indent, String s) {
  return dropSuffix(indent, indent(indent, s));
}
static int systemHashCode(Object o) {
  return identityHashCode(o);
}
static String getInjectionID() {
  return (String) call(getJavaX(), "getInjectionID", getMainClass());
}
static TableWithTooltips tableWithTooltips() {
  return (TableWithTooltips) swing(new F0<Object>() { Object get() { try { return  new TableWithTooltips() ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "new TableWithTooltips"; }});
}

static class TableWithTooltips extends JTable {
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
static long imageServerCheckMD5(String md5) {
  assertTrue(isMD5(md5));
  String s = loadPage("http://ai1.space/images/raw/checkmd5/" + md5);
  return parseLongOpt(s);
}
static void revalidate(final Component c) {
  if (c == null || !c.isShowing()) return;
  { swing(new Runnable() { public void run() { try { 
    // magic combo to actually relayout and repaint
    c.revalidate();
    c.repaint();
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "// magic combo to actually relayout and repaint\r\n    c.revalidate();\r\n    c.r..."; }}); }
}
static void standardTitlePopupMenu(final JFrame frame) {
  // standard right-click behavior on titles
  if (isSubstanceLAF())
    titlePopupMenu(frame,
      new VF1<JPopupMenu>() { void get(JPopupMenu menu) { try { 
        boolean alwaysOnTop = frame.isAlwaysOnTop();
        menu.add(jmenuItem("Restart Program", "restart"));
        menu.add(jmenuItem("Duplicate Program", "duplicateThisProgram"));
        menu.add(jmenuItem("Show Console", "showConsole"));
        menu.add(jCheckBoxMenuItem("Always On Top", alwaysOnTop, new Runnable() { public void run() { try { 
          toggleAlwaysOnTop(frame) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "toggleAlwaysOnTop(frame)"; }}));
        menu.add(jMenuItem("Shoot Window", new Runnable() { public void run() { try {  shootWindowGUI_external(frame, 500) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "shootWindowGUI_external(frame, 500)"; }}));
       } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "bool alwaysOnTop = frame.isAlwaysOnTop();\r\n        menu.add(jmenuItem(\"Restar..."; }});
}





// first delay = delay
static Timer installTimer(JComponent component, Object r, long delay) {
  return installTimer(component, r, delay, delay);
}

// first delay = delay
static Timer installTimer(RootPaneContainer frame, long delay, Object r) {
  return installTimer(frame.getRootPane(), r, delay, delay);
}

// first delay = delay
static Timer installTimer(JComponent component, long delay, Object r) {
  return installTimer(component, r, delay, delay);
}

static Timer installTimer(JComponent component, long delay, long firstDelay, Object r) {
  return installTimer(component, r, delay, firstDelay);
}

static Timer installTimer(final JComponent component, final Object r, final long delay, final long firstDelay) {
  return installTimer(component, r, delay, firstDelay, true);
}

static Timer installTimer(final JComponent component, final Object r, final long delay, final long firstDelay, final boolean repeats) {
  return (Timer) swingAndWait(new F0<Object>() { Object get() { try { 
    final Var<Timer> timer = new Var();
    timer.set(new Timer(toInt(delay), new java.awt.event.ActionListener() { public void actionPerformed(java.awt.event.ActionEvent _evt) { try {
      try {
        if (!allPaused())
          if (isFalse(callF(r)))
            cancelTimer(timer.get());
      } catch (Throwable __e) { printStackTrace2(__e); }
    } catch (Throwable __e) { messageBox(__e); }}}));
    timer.get().setInitialDelay(toInt(firstDelay));
    timer.get().setRepeats(repeats);
    bindTimerToComponent(timer.get(), component);
    return timer.get();
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "final new Var<Timer> timer;\r\n    timer.set(new Timer(toInt(delay), actionList..."; }});
}

static Timer installTimer(RootPaneContainer frame, long delay, long firstDelay, Object r) {
  return installTimer(frame.getRootPane(), delay, firstDelay, r);
}

static void fillTableWithStrings(final JTable table, List<List<String>> rows, List<String> colNames) {
  fillTableWithStrings(table, rows, toStringArray(colNames));
}

// thread-safe
static void fillTableWithStrings(final JTable table, List<List<String>> rows, String... colNames) {
  final DefaultTableModel model = fillTableWithStrings_makeModel(rows, colNames);
  
  swingNowOrLater(new Runnable() { public void run() { try { 
    setTableModel(table, model);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "setTableModel(table, model);"; }});
}

static DefaultTableModel fillTableWithStrings_makeModel(List<List<String>> rows, String... colNames) {
  Object[][] data = new Object[rows.size()][];
  int w = 0;
  for (int i = 0; i < rows.size(); i++) {
    List<String> l = rows.get(i);
    Object[] r = new Object[l.size()];
    for (int j = 0; j < l.size(); j++)
      r[j] = l.get(j);
    data[i] = r;
    w = Math.max(w, l.size());
  }
  Object[] columnNames = new Object[w];
  for (int i = 0; i < w; i++)
    columnNames[i] = i < l(colNames) ? colNames[i] : "?";
  return new DefaultTableModel(data, columnNames);
}

static volatile boolean readLine_noReadLine;

static String readLine_lastInput;
static String readLine_prefix = "[] ";

static String readLine() {
  if (readLine_noReadLine) return null;
  String s = readLineHidden();
  if (s != null) {
    readLine_lastInput = s;
    print(readLine_prefix + s);
  }
  return s;
}
static boolean headless() {
  return isHeadless();
}
static String makeResponder_callAnswerMethod(Object bot, String s, List<String> history) {
  String answer = (String) callOpt(bot, "answer", s, history);
  if (answer == null)
    answer = (String) callOpt(bot, "answer", s);
  return answer;
}

static Responder makeResponder(final Object bot) {
  if (bot instanceof Responder) return (Responder) bot;
  
  if (bot instanceof String) {
    String f = (String) ( bot);
    return new Responder() {
      String answer(String s, List<String> history) {
        String answer = (String) callOptMC((String) bot, s, history);
        if (answer == null)
          answer = (String) callOptMC((String) bot, s);
        return answer;
      }
    };
  }
  
  return new Responder() {
    String answer(String s, List<String> history) {
      return makeResponder_callAnswerMethod(bot, s, history);
    }
  };
}
static String[] dropFirst(int n, String[] a) {
  return drop(n, a);
}

static String[] dropFirst(String[] a) {
  return drop(1, a);
}

static Object[] dropFirst(Object[] a) {
  return drop(1, a);
}

static <A> List<A> dropFirst(List<A> l) {
  return dropFirst(1, l);
}

static <A> List<A> dropFirst(Iterable<A> i) {
  return dropFirst(toList(i));
}

static <A> List<A> dropFirst(int n, List<A> l) {
  return n <= 0 ? l : new ArrayList(l.subList(Math.min(n, l.size()), l.size()));
}

static <A> List<A> dropFirst(List<A> l, int n) {
  return dropFirst(n, l);
}

static String dropFirst(int n, String s) { return substring(s, n); }
static String dropFirst(String s) { return substring(s, 1); }
static JPanel centerAndNorth(final Component c, final Component n) {
  return swing(new F0<JPanel>() { JPanel get() { try { 
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(BorderLayout.CENTER, wrap(c));
    panel.add(BorderLayout.NORTH, wrap(n));
    return panel;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "JPanel panel = new JPanel(new BorderLayout);\r\n    panel.add(BorderLayout.CENT..."; }});
}
static <A> A listGet(List<A> l, int idx) {
  return l != null && idx >= 0 && idx < l(l) ? l.get(idx) : null;
}
static String javaTok_substringC(String s, int i, int j) {
  return s.substring(i, j);
}
static String loadTextFilePossiblyGZipped(String fileName) {
  return loadTextFilePossiblyGZipped(fileName, null);
}
  
static String loadTextFilePossiblyGZipped(String fileName, String defaultContents) {
  File gz = new File(fileName + ".gz");
  return gz.exists() ? loadGZTextFile(gz) : loadTextFile(fileName, defaultContents);
}

static String loadTextFilePossiblyGZipped(File fileName) {
  return loadTextFilePossiblyGZipped(fileName, null);
}

static String loadTextFilePossiblyGZipped(File fileName, String defaultContents) {
  return loadTextFilePossiblyGZipped(fileName.getPath(), defaultContents);
}

static URLConnection setURLConnectionTimeouts(URLConnection con, long timeout) {
  con.setConnectTimeout(toInt(timeout));
  con.setReadTimeout(toInt(timeout));
  if (con.getConnectTimeout() != timeout || con.getReadTimeout() != timeout)
    print("Warning: Timeouts not set by JDK.");
  return con;
}
static void setOpt_raw(Object o, String field, Object value) { try {
  if (o == null) return;
  if (o instanceof Class) setOpt_raw((Class) o, field, value);
  else {
    Field f = setOpt_raw_findField(o.getClass(), field);
    if (f != null)
      smartSet(f, o, value);
  }
} catch (Exception __e) { throw rethrow(__e); } }

static void setOpt_raw(Class c, String field, Object value) { try {
  if (c == null) return;
  Field f = setOpt_raw_findStaticField(c, field);
  if (f != null)
    smartSet(f, null, value);
} catch (Exception __e) { throw rethrow(__e); } }
  
static Field setOpt_raw_findStaticField(Class<?> c, String field) {
  Class _c = c;
  do {
    for (Field f : _c.getDeclaredFields())
      if (f.getName().equals(field) && (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0)
        return f;
    _c = _c.getSuperclass();
  } while (_c != null);
  return null;
}

static Field setOpt_raw_findField(Class<?> c, String field) {
  Class _c = c;
  do {
    for (Field f : _c.getDeclaredFields())
      if (f.getName().equals(field))
        return f;
    _c = _c.getSuperclass();
  } while (_c != null);
  return null;
}
static byte[] boolArrayToBytes(boolean[] a) {
  byte[] b = new byte[(l(a)+7)/8];
  for (int i = 0; i < l(a); i++)
    if (a[i])
      b[i/8] |= 1 << (i & 7);
  return b;
}
static int packFrame_minw = 150, packFrame_minh = 50;

static <A extends Component> A packFrame(final A c) {
  { swing(new Runnable() { public void run() { try { 
    JFrame frame = getFrame(c);
    if (frame != null) {
      frame.pack();
      int maxW = getScreenWidth()-50, maxH = getScreenHeight()-50;
      frame.setSize(
        min(maxW, max(frame.getWidth(), packFrame_minw)),
        min(maxH, max(frame.getHeight(), packFrame_minh)));
    }
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "JFrame frame = getFrame(c);\r\n    if (frame != null) {\r\n      frame.pack();\r\n ..."; }}); }
  return c;
}

static JFrame packFrame(ButtonGroup g) {
  return packFrame(getFrame(g));
}
static String shortenClassName(String name) {
  if (name == null) return null;
  int i = lastIndexOf(name, "$");
  if (i < 0) i = lastIndexOf(name, ".");
  return i < 0 ? name : substring(name, i+1);
}
  static List<String> parse3(String s) {
    return dropPunctuation(javaTokPlusPeriod(s));
  }
static String getInnerMessage(Throwable e) {
  if (e == null) return null;
  return getInnerException(e).getMessage();
}
static Component wrapForSmartAdd(Object o) {
  if (o == null) return new JPanel();
  if (o instanceof String) return jlabel((String) o);
  return wrap(o);
}
static List<Class> getClasses(Object[] array) {
  List<Class> l = new ArrayList();
  for (Object o : array) l.add(_getClass(o));
  return l;
}
static int iround(double d) {
  return (int) Math.round(d);
}
static boolean sexyTable_drag = true;

static JTable sexyTable() {
  final JTable table = sexyTableWithoutDrag();
  if (sexyTable_drag)
    tableEnableTextDrag(table); // TODO: seems to interfere with double clicks
  return table;
}
static Object pcallFunction(Object f, Object... args) {
  try { return callFunction(f, args); } catch (Throwable __e) { printStackTrace2(__e); }
  return null;
}
static void ensureDBNotRunning(String name) {
  if (hasBot(name)) {
    try {
      String id = fsI(dropAfterSpace(name));
      String framesBot = id + " Frames";
      print("Trying to activate frames of running DB: " + id);
      if (isOK(sendOpt(framesBot, "activate frames")) && isMainProgram())
        cleanKill();
    } catch (Throwable __e) { printStackTrace2(__e); }
    throw fail("Already running: " + name);
  }
}
// Try to get the quoting right...

static String smartJoin(String[] args) {
  if (args.length == 1) return args[0];
  
  String[] a = new String[args.length];
  for (int i = 0; i < a.length; i++)
    a[i] = !isJavaIdentifier(args[i]) && !isQuoted(args[i]) ? quote(args[i]) : args[i];
  return join(" ", a);
}

static String smartJoin(List<String> args) {
  return smartJoin(toStringArray(args));
}
static final boolean loadPageThroughProxy_enabled = false;

static String loadPageThroughProxy(String url) {
  return null;
}
static void hotwire_copyOver(Class c) {
  synchronized(StringBuffer.class) {
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
static <A, B> List<A> keysWithoutHidden(Map<A, B> map) {
  return filter(keys(map) , new F1<Object, Boolean>() { Boolean get(Object o) { try { return  !isStringStartingWith(o, "[hidden] ") ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "!isStringStartingWith(o, \"[hidden] \")"; }});
}
static Map<Class, HashMap<String, Method>> callOpt_noArgs_cache = newDangerousWeakHashMap();

static Object callOpt_noArgs(Object o, String method) { try {
  if (o == null) return null;
  if (o instanceof Class)
    return callOpt(o, method); // not optimized
  
  Class c = o.getClass();
  HashMap<String, Method> map;
  synchronized(callOpt_noArgs_cache) {
    map = callOpt_noArgs_cache.get(c);
    if (map == null)
      map = callOpt_noArgs_makeCache(c);
  }

  Method m = map.get(method);
  return m != null ? m.invoke(o) : null;
} catch (Exception __e) { throw rethrow(__e); } }

// used internally - we are in synchronized block
static HashMap<String, Method> callOpt_noArgs_makeCache(Class c) {
  HashMap<String,Method> map = new HashMap();
  Class _c = c;
  do {
    for (Method m : c.getDeclaredMethods())
      if (m.getParameterTypes().length == 0) {
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
static RuntimeException asRuntimeException(Throwable t) {
  
  if (t instanceof Error)
    _handleError((Error) t);
  
  return t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
}
static String makePostData(Map<Object, Object> map) {
  List<String> l = new ArrayList();
  for (Map.Entry<Object, Object> e : map.entrySet()) {
    String key = (String) ( e.getKey());
    Object val = e.getValue();
    if (val != null) {
      String value = str(val); //structureOrText(val);
      l.add(urlencode(key) + "=" + urlencode(escapeMultichars(value)));
    }
  }
  return join("&", l);
}

static String makePostData(Object... params) {
  return makePostData(litorderedmap(params));
}

static void moveThisThreadToChild(Object child) {
  Thread t = currentThread();
  callOpt(child, "_registerThread", t);
  _unregisterThread(t);
}
// extended over Class.isInstance() to handle primitive types
static boolean isInstanceX(Class type, Object arg) {
  if (type == boolean.class) return arg instanceof Boolean;
  if (type == int.class) return arg instanceof Integer;
  if (type == long.class) return arg instanceof Long;
  if (type == float.class) return arg instanceof Float;
  if (type == short.class) return arg instanceof Short;
  if (type == char.class) return arg instanceof Character;
  if (type == byte.class) return arg instanceof Byte;
  if (type == double.class) return arg instanceof Double;
  return type.isInstance(arg);
}
static Object invokeMethod(Method m, Object o, Object... args) { try {
  try {
    return m.invoke(o, args);
  } catch (InvocationTargetException e) {
    throw rethrow(getExceptionCause(e));
  } catch (IllegalArgumentException e) {
    throw new IllegalArgumentException(e.getMessage() + " - was calling: " + m + ", args: " + joinWithSpace(classNames(args)));
  }
} catch (Exception __e) { throw rethrow(__e); } }
static int tableRows(JTable table) {
  return table.getRowCount();
}
static void sleepSeconds(double s) {
  if (s > 0) sleep(round(s*1000));
}
static <A, B> Collection<B> values(Map<A, B> map) {
  return map == null ? emptyList() : map.values();
}




static void assertFalse(Object o) {
  if (!(eq(o, false) /*|| isFalse(pcallF(o))*/))
    throw fail(str(o));
}
  
static boolean assertFalse(boolean b) {
  if (b) throw fail("oops");
  return b;
}

static boolean assertFalse(String msg, boolean b) {
  if (b) throw fail(msg);
  return b;
}

static JSplitPane jhsplit(Component l, Component r) {
  return setSplitPaneLater(
    swingConstruct(JSplitPane.class, JSplitPane.HORIZONTAL_SPLIT, wrap(l), wrap(r)),
    0.5);
}
static JTable dataToTable_uneditable(Object data, final JTable table) {
  return dataToTable_uneditable(table, data);
}

static JTable dataToTable_uneditable(final JTable table, final Object data) {
  { swing(new Runnable() { public void run() { try { 
    dataToTable(table, data, true);
    makeTableUneditable(table);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "dataToTable(table, data, true);\r\n    makeTableUneditable(table);"; }}); }
  return table;
}

static JTable dataToTable_uneditable(final Object data) {
  return dataToTable_uneditable(showTable(), data);
}

static JTable dataToTable_uneditable(Object data, String title) {
  return dataToTable_uneditable(showTable(title), data);
}
public static long parseSnippetID(String snippetID) {
  long id = Long.parseLong(shortenSnippetID(snippetID));
  if (id == 0) throw fail("0 is not a snippet ID");
  return id;
}
static long sysNow() {
  return System.nanoTime()/1000000;
}
static Throwable innerException(Throwable e) {
  return getInnerException(e);
}
static ImageIcon imageIcon(String imageID) { try {
  return new ImageIcon(loadBinarySnippet(imageID).toURI().toURL());
} catch (Exception __e) { throw rethrow(__e); } }

static ImageIcon imageIcon(BufferedImage img) {
  return new ImageIcon(img);
}


  static ImageIcon imageIcon(RGBImage img) {
    return imageIcon(img.getBufferedImage());
  }

static Object[] toObjectArray(Collection c) {
  List l = asList(c);
  return l.toArray(new Object[l.size()]);
}

static String processID_cached;

// try to get our current process ID
static String getPID() {
  if (processID_cached == null) {
    String name = ManagementFactory.getRuntimeMXBean().getName();
    processID_cached = name.replaceAll("@.*", "");
  }
  return processID_cached;
}
static int lUtf8(String s) {
  return l(utf8(s));
}
static Map<String, Class> classForName_cache = synchroHashMap();

static Class classForName(String name) { try {
  Class c = classForName_cache.get(name);
  if (c == null)
    classForName_cache.put(name, c = Class.forName(name));
  return c;
} catch (Exception __e) { throw rethrow(__e); } }
static int parseHexByte(String s) {
  return Integer.parseInt(s, 16);
}
static int isAndroid_flag;

static boolean isAndroid() {
  if (isAndroid_flag == 0)
    isAndroid_flag = System.getProperty("java.vendor").toLowerCase().indexOf("android") >= 0 ? 1 : -1;
  return isAndroid_flag > 0;
}



static Throwable getExceptionCause(Throwable e) {
  Throwable c = e.getCause();
  return c != null ? c : e;
}
static String sendOpt(String bot, String text, Object... args) {
  return sendToLocalBotOpt(bot, text, args);
}
static String[] dropLast(String[] a, int n) {
  n = Math.min(n, a.length);
  String[] b = new String[a.length-n];
  System.arraycopy(a, 0, b, 0, b.length);
  return b;
}

static <A> List<A> dropLast(List<A> l) {
  return subList(l, 0, l(l)-1);
}

static <A> List<A> dropLast(int n, List<A> l) {
  return subList(l, 0, l(l)-n);
}

static <A> List<A> dropLast(Iterable<A> l) {
  return dropLast(asList(l));
}

static String dropLast(String s) {
  return substring(s, 0, l(s)-1);
}

static String dropLast(String s, int n) {
  return substring(s, 0, l(s)-n);
}

static String dropLast(int n, String s) {
  return dropLast(s, n);
}

static String shortenSnippetID(String snippetID) {
  if (snippetID.startsWith("#"))
    snippetID = snippetID.substring(1);
  String httpBlaBla = "http://tinybrain.de/";
  if (snippetID.startsWith(httpBlaBla))
    snippetID = snippetID.substring(httpBlaBla.length());
  return "" + parseLong(snippetID);
}
static <A> HashSet<A> litset(A... items) {
  return lithashset(items);
}
static <A> Set<A> synchroTreeSet() {
  return Collections.synchronizedSet(new TreeSet<A>());
}

static String dropSuffixIgnoreCase(String suffix, String s) {
  return ewic(s, suffix) ? s.substring(0, l(s)-l(suffix)) : s;
}
// menuMaker = voidfunc(JPopupMenu)
static void titlePopupMenu(final Component c, final Object menuMaker) {
  swingNowOrLater(new Runnable() { public void run() { try { 
    if (!isSubstanceLAF())
      print("Can't add title right click!");
    else {
      JComponent titleBar = getTitlePaneComponent(getFrame(c));
      componentPopupMenu(titleBar, menuMaker);
    }
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (!isSubstanceLAF())\r\n      print(\"Can't add title right click!\");\r\n    els..."; }});
}
static String programIDWithCase() {
  return nempty(caseID())
    ? programID() + "/" + quoteUnlessIdentifierOrInteger(caseID())
    : programID();
}
static void duplicateThisProgram() {
  nohupJavax(trim(programID() + " " + smartJoin((String[]) get(getJavaX(), "fullArgs"))));
}
static String escapeMultichars(String s) {
  StringBuilder buf = new StringBuilder();
  for (int i = 0; i < l(s); i++)
    if (isExtendedUnicodeCharacter(s, i)) {
      buf.append("[xchar " + intToHex(s.codePointAt(i)) + "]");
      ++i;
    } else
      buf.append(s.charAt(i));
  return str(buf);
}
static <A, B> Map<A, B> newDangerousWeakHashMap() {
  return _registerDangerousWeakMap(synchroMap(new WeakHashMap()));
}

// initFunction: voidfunc(Map) - is called initially, and after clearing the map
static <A, B> Map<A, B> newDangerousWeakHashMap(Object initFunction) {
  return _registerDangerousWeakMap(synchroMap(new WeakHashMap()), initFunction);
}
static Class javax() {
  return getJavaX();
}
// supports the usual quotings (", variable length double brackets) except ' quoting
static boolean isQuoted(String s) {
  
  
  if (isNormalQuoted(s)) return true; // use the exact version
  
  return isMultilineQuoted(s);
}
static Thread _unregisterThread(Thread t) {
  _registerThread_threads.remove(t);
  return t;
}

static void _unregisterThread() { _unregisterThread(currentThread()); }
static boolean forbiddenPort(int port) {
  return port == 5037; // adb
}

static String dbBotName(String progID) {
  return fsI(progID) + " Concepts";
}
static String prependIfNempty(String prefix, String s) {
  return empty(s) ? s : prefix + s;
}
static boolean isOK(String s) {
  s = trim(s);
  return swic(s, "ok ") || eqic(s, "ok") || matchStart("ok", s);
}
static List<String> javaTokWithExisting(String s, List<String> existing) {
  ++javaTok_n;
  int nExisting = javaTok_opt && existing != null ? existing.size() : 0;
  ArrayList<String> tok = existing != null ? new ArrayList(nExisting) : new ArrayList();
  int l = s.length();
  
  int i = 0, n = 0;
  while (i < l) {
    int j = i;
    char c, d;
    
    // scan for whitespace
    while (j < l) {
      c = s.charAt(j);
      d = j+1 >= l ? '\0' : s.charAt(j+1);
      if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
        ++j;
      else if (c == '/' && d == '*') {
        do ++j; while (j < l && !s.substring(j, Math.min(j+2, l)).equals("*/"));
        j = Math.min(j+2, l);
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
    if (i >= l) break;
    c = s.charAt(i);
    d = i+1 >= l ? '\0' : s.charAt(i+1);

    // scan for non-whitespace
    
    // Special JavaX syntax: 'identifier
    if (c == '\'' && Character.isJavaIdentifierStart(d) && i+2 < l && "'\\".indexOf(s.charAt(i+2)) < 0) {
      j += 2;
      while (j < l && Character.isJavaIdentifierPart(s.charAt(j)))
        ++j;
    } else if (c == '\'' || c == '"') {
      char opener = c;
      ++j;
      while (j < l) {
        if (s.charAt(j) == opener /*|| s.charAt(j) == '\n'*/) { // allow multi-line strings
          ++j;
          break;
        } else if (s.charAt(j) == '\\' && j+1 < l)
          j += 2;
        else
          ++j;
      }
    } else if (Character.isJavaIdentifierStart(c))
      do ++j; while (j < l && (Character.isJavaIdentifierPart(s.charAt(j)) || "'".indexOf(s.charAt(j)) >= 0)); // for stuff like "don't"
    else if (Character.isDigit(c)) {
      do ++j; while (j < l && Character.isDigit(s.charAt(j)));
      if (j < l && s.charAt(j) == 'L') ++j; // Long constants like 1L
    } else if (c == '[' && d == '[') {
      do ++j; while (j+1 < l && !s.substring(j, j+2).equals("]]"));
      j = Math.min(j+2, l);
    } else if (c == '[' && d == '=' && i+2 < l && s.charAt(i+2) == '[') {
      do ++j; while (j+2 < l && !s.substring(j, j+3).equals("]=]"));
      j = Math.min(j+3, l);
    } else
      ++j;
      
    if (n < nExisting && javaTokWithExisting_isCopyable(existing.get(n), s, i, j))
      tok.add(existing.get(n));
    else
      tok.add(javaTok_substringC(s, i, j));
    ++n;
    i = j;
  }
  
  if ((tok.size() % 2) == 0) tok.add("");
  javaTok_elements += tok.size();
  return tok;
}

static boolean javaTokWithExisting_isCopyable(String t, String s, int i, int j) {
  return t.length() == j-i
    && s.regionMatches(i, t, 0, j-i); // << could be left out, but that's brave
}
// This is made for NL parsing.
// It's javaTok extended with "..." token, "$n" and "#n" and
// special quotes (which are converted to normal ones).

static List<String> javaTokPlusPeriod(String s) {
  List<String> tok = new ArrayList<String>();
  int l = s.length();
  
  int i = 0;
  while (i < l) {
    int j = i;
    char c; String cc;
    
    // scan for whitespace
    while (j < l) {
      c = s.charAt(j);
      cc = s.substring(j, Math.min(j+2, l));
      if (c == ' ' || c == '\t' || c == '\r' || c == '\n')
        ++j;
      else if (cc.equals("/*")) {
        do ++j; while (j < l && !s.substring(j, Math.min(j+2, l)).equals("*/"));
        j = Math.min(j+2, l);
      } else if (cc.equals("//")) {
        do ++j; while (j < l && "\r\n".indexOf(s.charAt(j)) < 0);
      } else
        break;
    }
    
    tok.add(s.substring(i, j));
    i = j;
    if (i >= l) break;
    c = s.charAt(i);
    cc = s.substring(i, Math.min(i+2, l));

    // scan for non-whitespace
    if (c == '\u201C' || c == '\u201D') c = '"'; // normalize quotes
    if (c == '\'' || c == '"') {
      char opener = c;
      ++j;
      while (j < l) {
        char _c = s.charAt(j);
        if (_c == '\u201C' || _c == '\u201D') _c = '"'; // normalize quotes
        if (_c == opener) {
          ++j;
          break;
        } else if (s.charAt(j) == '\\' && j+1 < l)
          j += 2;
        else
          ++j;
      }
      if (j-1 >= i+1) {
        tok.add(opener + s.substring(i+1, j-1) + opener);
        i = j;
        continue;
      }
    } else if (Character.isJavaIdentifierStart(c))
      do ++j; while (j < l && (Character.isJavaIdentifierPart(s.charAt(j)) || s.charAt(j) == '\'')); // for things like "this one's"
    else if (Character.isDigit(c))
      do ++j; while (j < l && Character.isDigit(s.charAt(j)));
    else if (cc.equals("[[")) {
      do ++j; while (j+1 < l && !s.substring(j, j+2).equals("]]"));
      j = Math.min(j+2, l);
    } else if (cc.equals("[=") && i+2 < l && s.charAt(i+2) == '[') {
      do ++j; while (j+2 < l && !s.substring(j, j+3).equals("]=]"));
      j = Math.min(j+3, l);
    } else if (s.substring(j, Math.min(j+3, l)).equals("..."))
      j += 3;
    else if (c == '$' || c == '#')
      do ++j; while (j < l && Character.isDigit(s.charAt(j)));
    else
      ++j;

    tok.add(s.substring(i, j));
    i = j;
  }
  
  if ((tok.size() % 2) == 0) tok.add("");
  return tok;
}

static List emptyList() {
  return new ArrayList();
  //ret Collections.emptyList();
}

static List emptyList(int capacity) {
  return new ArrayList(capacity);
}

// Try to match capacity
static List emptyList(Iterable l) {
  return l instanceof Collection ? emptyList(((Collection) l).size()) : emptyList();
}
  static Method findMethod(Object o, String method, Object... args) {
    try {
      if (o == null) return null;
      if (o instanceof Class) {
        Method m = findMethod_static((Class) o, method, args, false);
        if (m == null) return null;
        m.setAccessible(true);
        return m;
      } else {
        Method m = findMethod_instance(o, method, args, false);
        if (m == null) return null;
        m.setAccessible(true);
        return m;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static Method findMethod_static(Class c, String method, Object[] args, boolean debug) {
    Class _c = c;
    while (c != null) {
      for (Method m : c.getDeclaredMethods()) {
        if (debug)
          System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");;
        if (!m.getName().equals(method)) {
          if (debug) System.out.println("Method name mismatch: " + method);
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

  static Method findMethod_instance(Object o, String method, Object[] args, boolean debug) {
    Class c = o.getClass();
    while (c != null) {
      for (Method m : c.getDeclaredMethods()) {
        if (debug)
          System.out.println("Checking method " + m.getName() + " with " + m.getParameterTypes().length + " parameters");;
        if (m.getName().equals(method) && findMethod_checkArgs(m, args, debug))
          return m;
      }
      c = c.getSuperclass();
    }
    return null;
  }

  static boolean findMethod_checkArgs(Method m, Object[] args, boolean debug) {
    Class<?>[] types = m.getParameterTypes();
    if (types.length != args.length) {
      if (debug)
        System.out.println("Bad parameter length: " + args.length + " vs " + types.length);
      return false;
    }
    for (int i = 0; i < types.length; i++)
      if (!(args[i] == null || isInstanceX(types[i], args[i]))) {
        if (debug)
          System.out.println("Bad parameter " + i + ": " + args[i] + " vs " + types[i]);
        return false;
      }
    return true;
  }


static Object[] asObjectArray(List l) {
  return toObjectArray(l);
}
static Map synchroHashMap() {
  return Collections.synchronizedMap(new HashMap());
}

static String urlencode(String x) {
  try {
    return URLEncoder.encode(unnull(x), "UTF-8");
  } catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
}
static List<String> classNames(Collection l) {
  return getClassNames(l);
}

static List<String> classNames(Object[] l) {
  return getClassNames(Arrays.asList(l));
}
static File javaxCodeDir_dir; // can be set to work on different base dir

static File javaxCodeDir() {
  return javaxCodeDir_dir != null ? javaxCodeDir_dir : new File(userHome(), "JavaX-Code");
}
static int identityHashCode(Object o) {
  return System.identityHashCode(o);
}
// This is a bit rough... finds static and non-static methods.

static Method findMethodNamed(Object obj, String method) {
  if (obj == null) return null;
  if (obj instanceof Class)
    return findMethodNamed((Class) obj, method);
  return findMethodNamed(obj.getClass(), method);
}

static Method findMethodNamed(Class c, String method) {
  while (c != null) {
    for (Method m : c.getDeclaredMethods())
      if (m.getName().equals(method)) {
        m.setAccessible(true);
        return m;
      }
    c = c.getSuperclass();
  }
  return null;
}
static boolean isSubstanceLAF() {
  return substanceLookAndFeelEnabled();
}
static String lower(String s) {
  return s == null ? null : s.toLowerCase();
}

static char lower(char c) {
  return Character.toLowerCase(c);
}


static void bindTimerToComponent(final Timer timer, JFrame f) {
  bindTimerToComponent(timer, f.getRootPane());
}

static void bindTimerToComponent(final Timer timer, JComponent c) {
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
static void cleanKill() {
  cleanKillVM();
}
static int getScreenWidth() {
  return getScreenSize().width;
}
static void cancelTimer(javax.swing.Timer timer) {
  if (timer != null) timer.stop();
}

static void cancelTimer(java.util.Timer timer) {
  if (timer != null) timer.cancel();
}
static boolean ewic(String a, String b) {
  return endsWithIgnoreCase(a, b);
}

static boolean ewic(String a, String b, Matches m) {
  return endsWithIgnoreCase(a, b, m);
}
static String getSnippetTitle(String id) { try {
  if (id == null) return null;
  if (!isSnippetID(id)) return "?";
  long parsedID = parseSnippetID(id);
  String url;
  if (isImageServerSnippet(parsedID))
    url = "http://ai1.space/images/raw/title/" + parsedID;
  else
    url = tb_mainServer() + "/tb-int/getfield.php?id=" + parsedID + "&field=title" + standardCredentials();
  return trim(loadPageSilently(url));
} catch (Exception __e) { throw rethrow(__e); } }

static String getSnippetTitle(long id) {
  return getSnippetTitle(fsI(id));
}

static <A> ArrayList<A> toList(A[] a) { return asList(a); }
static ArrayList<Integer> toList(int[] a) { return asList(a); }
static <A> ArrayList<A> toList(Set<A> s) { return asList(s); }
static <A> ArrayList<A> toList(Iterable<A> s) { return asList(s); }
static JCheckBoxMenuItem jCheckBoxMenuItem(String text, boolean checked, final Object r) {
  JCheckBoxMenuItem mi = new JCheckBoxMenuItem(text, checked);
  mi.addActionListener(actionListener(r));
  return mi;
}
static void tableEnableTextDrag(final JTable table) {
  TransferHandler th = new TransferHandler() {
    @Override
    public int getSourceActions(JComponent c) { return COPY; }

    @Override
    protected Transferable createTransferable(JComponent c) {
      //print("Row/Column: " + table.getSelectedRow() + " / " + table.getSelectedColumn());
      Object o = selectedTableCell(table);
      //print("Value: " + o);
      return new StringSelection(str(o));
    }
  };
  tableEnableDrag(table, th);
}
static byte[] intToBytes(int i) {
  return new byte[] {
          (byte) (i >>> 24),
          (byte) (i >>> 16),
          (byte) (i >>> 8),
          (byte) i};
}
static List<Object> getMultiPorts() {
  return (List) callOpt(getJavaX(), "getMultiPorts");
}
static String emptyToNull(String s) {
  return eq(s, "") ? null : s;
}
static JTextArea jTextArea() {
  return jTextArea("");
}

static JTextArea jTextArea(final String text) {
  return swing(new F0<JTextArea>() { JTextArea get() { try { return  new JTextArea(text) ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "new JTextArea(text)"; }});
}
static File javaxDataDir_dir; // can be set to work on different base dir

static File javaxDataDir() {
  return javaxDataDir_dir != null ? javaxDataDir_dir : new File(userHome(), "JavaX-Data");
}
static int indent_default = 2;

static String indent(int indent) {
  return repeat(' ', indent);
}

static String indent(int indent, String s) {
  return indent(repeat(' ', indent), s);
}

static String indent(String indent, String s) {
  return indent + s.replace("\n", "\n" + indent);
}

static String indent(String s) {
  return indent(indent_default, s);
}

static List<String> indent(String indent, List<String> lines) {
  List<String> l = new ArrayList();
  for (String s : lines)
    l.add(indent + s);
  return l;
}
static String repeat(char c, int n) {
  n = Math.max(n, 0);
  char[] chars = new char[n];
  for (int i = 0; i < n; i++)
    chars[i] = c;
  return new String(chars);
}

static <A> List<A> repeat(A a, int n) {
  n = Math.max(n, 0);
  List<A> l = new ArrayList(n);
  for (int i = 0; i < n; i++)
    l.add(a);
  return l;
}

static <A> List<A> repeat(int n, A a) {
  return repeat(a, n);
}
static void shootWindowGUI_external(JFrame frame) {
  call(hotwireOnce("#1007178"), "shootWindowGUI", frame);
}

static void shootWindowGUI_external(final JFrame frame, int delay) {
  call(hotwireOnce("#1007178"), "shootWindowGUI", frame, delay);
}

static List<String> dropPunctuation_keep = litlist("*", "<", ">");

static List<String> dropPunctuation(List<String> tok) {
  tok = new ArrayList<String>(tok);
  for (int i = 1; i < tok.size(); i += 2) {
    String t = tok.get(i);
    if (t.length() == 1 && !Character.isLetter(t.charAt(0)) && !Character.isDigit(t.charAt(0)) && !dropPunctuation_keep.contains(t)) {
      tok.set(i-1, tok.get(i-1) + tok.get(i+1));
      tok.remove(i);
      tok.remove(i);
      i -= 2;
    }
  }
  return tok;
}

static String dropPunctuation(String s) {
  return join(dropPunctuation(nlTok(s)));
}
// start multi-port if none exists in current VM.
static void startMultiPort() {
  List mp = getMultiPorts();
  if (mp != null && mp.isEmpty()) {
    nohupJavax("#1001639");
    throw fail("Upgrading JavaX, please restart this program afterwards.");
    //callMain(hotwire("#1001672"));
  }
}
static void logQuotedWithTime(String s) {
  logQuotedWithTime(standardLogFile(), s);
}

static void logQuotedWithTime(File logFile, String s) {
  logQuoted(logFile, logQuotedWithTime_format(s));
}

static void logQuotedWithTime(String logFile, String s) {
  logQuoted(logFile, logQuotedWithTime_format(s));
}

static String logQuotedWithTime_format(String s) {
  return formatGMTWithDate_24(now()) + " " + s;
}
static String dropAfterSpace(String s) {
  return s == null ? null : substring(s, 0, smartIndexOf(s, ' '));
}
// This is for main classes that are all static.
// (We don't go to base classes.)
static Set<String> listFields(Object c) {
  TreeSet<String> fields = new TreeSet();
  for (Field f : _getClass(c).getDeclaredFields())
    fields.add(f.getName());
  return fields;
}
static <A> A assertNotNull(A a) {
  assertTrue(a != null);
  return a;
}

static <A> A assertNotNull(String msg, A a) {
  assertTrue(msg, a != null);
  return a;
}
static boolean equalsIgnoreCase(String a, String b) {
  return eqic(a, b);
}

static boolean equalsIgnoreCase(char a, char b) {
  return eqic(a, b);
}
static boolean allPaused() {
  return ping_pauseAll;
}
static File loadBinarySnippet(String snippetID) { try {
  long id = parseSnippetID(snippetID);
  File f = DiskSnippetCache_getLibrary(id);
  if (fileSize(f) == 0)
    f = loadDataSnippetToFile(snippetID);
  return f;
} catch (Exception __e) { throw rethrow(__e); } }
static Object mainBot;

static Object getMainBot() {
  return mainBot;
}
static String postPage(String url, Object... params) {
  return doPost(litmap(params), url);
}
static Class getMainClass() {
  return main.class;
}

static Class getMainClass(Object o) { try {
  return (o instanceof Class ? (Class) o : o.getClass()).getClassLoader().loadClass("main");
} catch (Exception __e) { throw rethrow(__e); } }
static int getScreenHeight() {
  return getScreenSize().height;
}
static void renewConsoleFrame() {
  setConsoleFrame(renewFrame(consoleFrame()));
}
static JScrollPane jscroll(final Component c) {
  return swing(new F0<JScrollPane>() { JScrollPane get() { try { return  new JScrollPane(c) ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "new JScrollPane(c)"; }});
}
static String readLineHidden() { try {
  if (get(javax(), "readLine_reader") == null)
    set(javax(), "readLine_reader" , new BufferedReader(new InputStreamReader(System.in, "UTF-8")));
  try {
    return ((BufferedReader) get(javax(), "readLine_reader")).readLine();
  } finally {
    consoleClearInput();
  }
} catch (Exception __e) { throw rethrow(__e); } }
static void makeTableUneditable(JTable table) {
  for (int c = 0; c < table.getColumnCount(); c++) {
    Class<?> col_class = table.getColumnClass(c);
    //O ed = table.getDefaultEditor(col_class);
    //print("Column " + c + " class: " + col_class + " editor: " + ed);
    table.setDefaultEditor(col_class, null); // remove editor
  }
}
static <A> A last(List<A> l) {
  return empty(l) ? null : l.get(l.size()-1);
}

static char last(String s) {
  return empty(s) ? '#' : s.charAt(l(s)-1);
}

static int last(int[] a) {
  return l(a) != 0 ? a[l(a)-1] : 0;
}

static <A> A last(A[] a) {
  return l(a) != 0 ? a[l(a)-1] : null;
}

static <A> A last(Iterator<A> it) {
  A a = null;
  while  (it.hasNext()) { ping(); a = it.next(); }
  return a;
}
static String classNameToVM(String name) {
  return name.replace(".", "$");
}
static void printException(Throwable e) {
  printStackTrace(e);
}
static void updateLookAndFeelOnAllWindows_noRenew() {
  for (Window window : Window.getWindows())
    SwingUtilities.updateComponentTreeUI(window);
}
static String beautifyStructure(String s) {
  return structure_addTokenMarkers(s);
}
static String[] drop(int n, String[] a) {
  n = Math.min(n, a.length);
  String[] b = new String[a.length-n];
  System.arraycopy(a, n, b, 0, b.length);
  return b;
}

static Object[] drop(int n, Object[] a) {
  n = Math.min(n, a.length);
  Object[] b = new Object[a.length-n];
  System.arraycopy(a, n, b, 0, b.length);
  return b;
}
static <A> List<A> synchroList() {
  return Collections.synchronizedList(new ArrayList<A>());
}

static <A> List<A> synchroList(List<A> l) {
  return Collections.synchronizedList(l);
}

static File getProgramFile(String progID, String fileName) {
  if (new File(fileName).isAbsolute())
    return new File(fileName);
  return new File(getProgramDir(progID), fileName);
}

static File getProgramFile(String fileName) {
  return getProgramFile(getProgramID(), fileName);
}

static boolean eqic(String a, String b) {
  
  
    if ((a == null) != (b == null)) return false;
    if (a == null) return true;
    return a.equalsIgnoreCase(b);
  
}



static boolean eqic(char a, char b) {
  if (a == b) return true;
  
    char u1 = Character.toUpperCase(a);
    char u2 = Character.toUpperCase(b);
    if (u1 == u2) return true;
  
  return Character.toLowerCase(u1) == Character.toLowerCase(u2);
}
static int lastIndexOf(String a, String b) {
  return a == null || b == null ? -1 : a.lastIndexOf(b);
}

static int lastIndexOf(String a, char b) {
  return a == null ? -1 : a.lastIndexOf(b);
}
static Field findField2(Object o, String field) {
  Class c = o.getClass();
  HashMap<String, Field> map;
  synchronized(getOpt_cache) {
    map = getOpt_cache.get(c);
    if (map == null)
      map = getOpt_makeCache(c);
  }
  
  if (map == getOpt_special) {
    if (o instanceof Class)
      return findField2_findStaticField((Class) o, field);
  }
    
  return map.get(field);
}

static Field findField2_findStaticField(Class<?> c, String field) {
  Class _c = c;
  do {
    for (Field f : _c.getDeclaredFields())
      if (f.getName().equals(field) && (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0)
        return f;
    _c = _c.getSuperclass();
  } while (_c != null);
  return null;
}
static volatile Throwable lastException_lastException;

static Throwable lastException() {
  return lastException_lastException;
}

static void lastException(Throwable e) {
  lastException_lastException = e;
}
static String struct_noStringSharing(Object o) {
  structure_Data d = new structure_Data();
  d.noStringSharing = true;
  return structure(o, d);
}
static String joinWithSpace(Collection<String> c) {
  return join(" ", c);
}

static String joinWithSpace(String... c) {
  return join(" ", c);
}

static Object callOptMC(String method, Object... args) {
  return callOpt(mc(), method, args);
}
static String javaTok_substringN(String s, int i, int j) {
  if (i == j) return "";
  if (j == i+1 && s.charAt(i) == ' ') return " ";
  return s.substring(i, j);
}
static String getComputerID_quick() {
  return computerID();
}
static <A, B> void put(Map<A, B> map, A a, B b) {
  if (map != null) map.put(a, b);
}
static boolean isStringStartingWith(Object o, String prefix) {
  return o instanceof String && ((String) o).startsWith(prefix);
}
static boolean substanceLookAndFeelEnabled() {
  return startsWith(getLookAndFeel(), "org.pushingpixels.");
}
static <A> A optPar(ThreadLocal<A> tl, A defaultValue) {
  A a = tl.get();
  if (a != null) {
    tl.set(null);
    return a;
  }
  return defaultValue;
}

static <A> A optPar(ThreadLocal<A> tl) {
  return optPar(tl, null);
}
static <A> List<A> replaceSublist(List<A> l, List<A> x, List<A> y) {
  if (x == null) return l;
  
  int i = 0;
  while (true) {
    i = indexOfSubList(l, x, i);
    if (i < 0) break;
    
    // It's inefficient :D
    for (int j = 0; j < l(x); j++) l.remove(i);
    l.addAll(i, y);
    i += l(y);
  }
  return l;
}

static <A> List<A> replaceSublist(List<A> l, int fromIndex, int toIndex, List<A> y) {
  // inefficient
  while (toIndex > fromIndex) l.remove(--toIndex);
  l.addAll(fromIndex, y);
  return l;
}
  static List<String> codeTokensOnly(List<String> tok) {
    List<String> l = new ArrayList();
    for (int i = 1; i < tok.size(); i += 2)
      l.add(tok.get(i));
    return l;
  }
static void upgradeJavaXAndRestart() {
  run("#1001639");
  restart();
  sleep();
}
static List<Pair> _registerDangerousWeakMap_preList;

static <A> A _registerDangerousWeakMap(A map) {
  return _registerDangerousWeakMap(map, null);
}

static <A> A _registerDangerousWeakMap(A map, Object init) {
  callF(init, map);
  
  if (init instanceof String) {
    final String f = (String) ( init);
    init = new VF1<Map>() { void get(Map map) { try {  callMC(f, map) ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "callMC(f, map)"; }};
  }
    
  if (javax() == null) {
    // We're in class init
    if (_registerDangerousWeakMap_preList == null) _registerDangerousWeakMap_preList = synchroList();
    _registerDangerousWeakMap_preList.add(pair(map, init));
    return map;
  }
  
  try {
    call(javax(), "_registerDangerousWeakMap", map, init);
  } catch (Throwable e) { printException(e);
    upgradeJavaXAndRestart();
  }
  return map;
}

static void _onLoad_registerDangerousWeakMap() {
  assertNotNull(javax());
  if (_registerDangerousWeakMap_preList == null) return;
  for (Pair p : _registerDangerousWeakMap_preList)
    _registerDangerousWeakMap(p.a, p.b);
  _registerDangerousWeakMap_preList = null;
}
static byte[] toPNG(BufferedImage img) {
  return convertToPNG(img);
}
static String addSuffix(String s, String suffix) {
  return s.endsWith(suffix) ? s : s + suffix;
}
static byte[] utf8(String s) {
  return toUtf8(s);
}
static String _userHome;
static String userHome() {
  if (_userHome == null) {
    if (isAndroid())
      _userHome = "/storage/sdcard0/";
    else
      _userHome = System.getProperty("user.home");
    //System.out.println("userHome: " + _userHome);
  }
  return _userHome;
}

static File userHome(String path) {
  return new File(userDir(), path);
}
static Map emptyMap() {
  return new HashMap();
}
static String baseClassName(String className) {
  return substring(className, className.lastIndexOf('.')+1);
}

static String baseClassName(Object o) {
  return baseClassName(getClassName(o));
}
static Map<String, String> hippoSingulars() {
  return pairsToMap((List<Pair<String, String>>) scanStructureLog("#1011041", "singulars"));
}
static void restart() {
  Object j = getJavaX();
  call(j, "cleanRestart", get(j, "fullArgs"));
}
static List filter(Iterable c, Object pred) {
  
  List x = new ArrayList();
  if (c != null) for (Object o : c)
    if (isTrue(callF(pred, o)))
      x.add(o);
  return x;
}

static List filter(Object pred, Iterable c) {
  return filter(c, pred);
}

static <A, B extends A> List filter(Iterable<B> c, F1<A, Boolean> pred) {
  List x = new ArrayList();
  if (c != null) for (B o : c)
    if (pred.get(o).booleanValue())
      x.add(o);
  return x;
}

static <A, B extends A> List filter(F1<A, Boolean> pred, Iterable<B> c) {
  return filter(c, pred);
}

// works on lists and strings and null

static int indexOfIgnoreCase(List<String> a, String b) {
  return indexOfIgnoreCase(a, b, 0);
}

static int indexOfIgnoreCase(List<String> a, String b, int i) {
  int n = l(a);
  for (; i < n; i++)
    if (eqic(a.get(i), b)) return i;
  return -1;
}

static int indexOfIgnoreCase(String a, String b) {
  return indexOfIgnoreCase_manual(a, b);
  /*Matcher m = Pattern.compile(b, Pattern.CASE_INSENSITIVE + Pattern.LITERAL).matcher(a);
  if (m.find()) return m.start(); else ret -1;*/
}
static Class<?> getClass(String name) {
  try {
    return Class.forName(name);
  } catch (ClassNotFoundException e) {
    return null;
  }
}

static Class getClass(Object o) {
  return o instanceof Class ? (Class) o : o.getClass();
}

static Class getClass(Object realm, String name) { try {
  try {
    return getClass(realm).getClassLoader().loadClass(classNameToVM(name));
  } catch (ClassNotFoundException e) {
    return null;
  }
} catch (Exception __e) { throw rethrow(__e); } }
static String emptySymbol_value;

static String emptySymbol() {
  if (emptySymbol_value == null) emptySymbol_value = symbol("");
  return emptySymbol_value;
}
static void addToContainer(Container a, Component b) {
  if (a != null && b != null) a.add(b);
}
static <A, B> void mapPut(Map<A, B> map, A key, B value) {
  if (map != null && key != null && value != null) map.put(key, value);
}
static <A, B> void multiMapPut(Map<A, List<B>> map, A a, B b) {
  List<B> l = map.get(a);
  if (l == null)
    map.put(a, l = new ArrayList());
  l.add(b);
}
static int findCodeTokens(List<String> tok, String... tokens) {
  return findCodeTokens(tok, 1, false, tokens);
}

static int findCodeTokens(List<String> tok, boolean ignoreCase, String... tokens) {
  return findCodeTokens(tok, 1, ignoreCase, tokens);
}

static int findCodeTokens(List<String> tok, int startIdx, boolean ignoreCase, String... tokens) {
  return findCodeTokens(tok, startIdx, ignoreCase, tokens, null);
}

static List<String> findCodeTokens_specials = litlist("*", "<quoted>", "<id>", "<int>", "\\*");
static boolean findCodeTokens_debug;
static int findCodeTokens_indexed, findCodeTokens_unindexed;
static int findCodeTokens_bails, findCodeTokens_nonbails;

static int findCodeTokens(List<String> tok, int startIdx, boolean ignoreCase, String[] tokens, Object condition) {
  if (findCodeTokens_debug) {
    if (eq(getClassName(tok), "main$IndexedList2"))
      findCodeTokens_indexed++;
    else
      findCodeTokens_unindexed++;
  }
  // bail out early if first token not found (works great with IndexedList)
  if (!findCodeTokens_specials.contains(tokens[0])
    && !tok.contains(tokens[0] /*, startIdx << no signature in List for this, unfortunately */)) {
      ++findCodeTokens_bails;
      return -1;
    }
  ++findCodeTokens_nonbails;
  
  outer: for (int i = startIdx | 1; i+tokens.length*2-2 < tok.size(); i += 2) {
    for (int j = 0; j < tokens.length; j++) {
      String p = tokens[j], t = tok.get(i+j*2);
      boolean match;
      if (eq(p, "*")) match = true;
      else if (eq(p, "<quoted>")) match = isQuoted(t);
      else if (eq(p, "<id>")) match = isIdentifier(t);
      else if (eq(p, "<int>")) match = isInteger(t);
      else if (eq(p, "\\*")) match = eq("*", t);
      else match = ignoreCase ? eqic(p, t) : eq(p, t);
      
      if (!match)
        continue outer;
    }
    
    if (condition == null || checkTokCondition(condition, tok, i-1)) // pass N index
      return i;
  }
  return -1;
}
static JMenuItem jMenuItem(String text, Object r) {
  return jmenuItem(text, r);
}
static Rectangle screenRectangle() {
  return new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
}

static String loadGZTextFile(File file) { try {
  if (!file.isFile()) return null;
  ByteArrayOutputStream baos = new ByteArrayOutputStream();
  InputStream fis = new FileInputStream(file);
  GZIPInputStream gis = new GZIPInputStream(fis);
  try {
    byte[] buffer = new byte[1024];
    int len;
    while((len = gis.read(buffer)) != -1){
        baos.write(buffer, 0, len);
    }
  } finally {
    fis.close();
  }
  baos.close();
  return fromUtf8(baos.toByteArray()); // TODO: use a Reader
} catch (Exception __e) { throw rethrow(__e); } }
static Object collectionMutex(Object o) {
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
static String lastWord(String s) {
  return lastJavaToken(s);
}
static AbstractAction abstractAction(String name, final Object runnable) {
  return new AbstractAction(name) {
    public void actionPerformed(ActionEvent evt) {
      pcallF(runnable);
    }
  };
}
static boolean hasBot(String searchPattern) {
  DialogIO io = findBot(searchPattern);
  if (io != null) {
    io.close();
    return true;
  } else
    return false;
}
static boolean isLetter(char c) {
  return Character.isLetter(c);
}
static long parseLongOpt(String s) {
  return isInteger(s) ? parseLong(s) : 0;
}
static void toggleAlwaysOnTop(JFrame frame) {
  frame.setAlwaysOnTop(!frame.isAlwaysOnTop());
}
static Dimension getScreenSize() {
  return Toolkit.getDefaultToolkit().getScreenSize();
}
static String formatSnippetIDOpt(String s) {
  return isSnippetID(s) ? formatSnippetID(s) : s;
}


static String hmsWithColons() {
  return hmsWithColons(now());
}

static String hmsWithColons(long time) {
  return new SimpleDateFormat("HH:mm:ss").format(time);
}

static Android3 methodsBot2(String name, final Object receiver, final List<String> exposedMethods) {
  return methodsBot2(name, receiver, exposedMethods, null);
}

static Android3 methodsBot2(String name, final Object receiver, final List<String> exposedMethods, final Lock lock) {
  Android3 android = new Android3();
  android.greeting = name;
  android.console = false;
  android.responder = new Responder() {
    String answer(String s, List<String> history) {
      return exposeMethods2(receiver, s, exposedMethods, lock);
    }
  };
  return makeBot(android);
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
      while (-1 != (n = reader.read(buffer)))
        builder.append(buffer, 0, n);
        
    } finally {
      reader.close();
    }
    return builder.toString();
  }
static void clear(Collection c) {
  if (c != null) c.clear();
}
static long round(double d) {
  return Math.round(d);
}
static char lastChar(String s) {
  return empty(s) ? '\0' : s.charAt(l(s)-1);
}


static JComponent getTitlePaneComponent(Window window) {
  if (!substanceLookAndFeelEnabled()) return null;
  
	JRootPane rootPane = null;
	if (window instanceof JFrame)
		rootPane = ((JFrame) window).getRootPane();
	if (window instanceof JDialog)
		rootPane = ((JDialog) window).getRootPane();
	if (rootPane != null) {
		Object /*SubstanceRootPaneUI*/ ui = rootPane.getUI();
		return (JComponent) call(ui, "getTitlePane");
	}
	return null;
}

static boolean isImageServerSnippet(long id) {
  return id >= 1100000 && id < 1200000;
}
static class componentPopupMenu_Maker {
  List menuMakers = new ArrayList();
}

static Map<JComponent, componentPopupMenu_Maker> componentPopupMenu_map = new WeakHashMap();

static ThreadLocal<MouseEvent> componentPopupMenu_mouseEvent = new ThreadLocal();

// menuMaker = voidfunc(JPopupMenu)
static void componentPopupMenu(final JComponent component, final Object menuMaker) {
  swingNowOrLater(new Runnable() { public void run() { try { 
    componentPopupMenu_Maker maker = componentPopupMenu_map.get(component);
    if (maker == null) {
      componentPopupMenu_map.put(component, maker = new componentPopupMenu_Maker());
      component.addMouseListener(new componentPopupMenu_Adapter(maker));
    }
    maker.menuMakers.add(menuMaker);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "componentPopupMenu_Maker maker = componentPopupMenu_map.get(component);\r\n    ..."; }});
}

static class componentPopupMenu_Adapter extends MouseAdapter {
  componentPopupMenu_Maker maker;
  
  componentPopupMenu_Adapter(componentPopupMenu_Maker maker) {
  this.maker = maker;}
  
  public void mousePressed(MouseEvent e) { displayMenu(e); }
  public void mouseReleased(MouseEvent e) { displayMenu(e); }

  void displayMenu(MouseEvent e) {
    if (e.isPopupTrigger()) displayMenu2(e);
  }
    
  void displayMenu2(MouseEvent e) {
    JPopupMenu menu = new JPopupMenu();
    int emptyCount = menu.getComponentCount();
    
    componentPopupMenu_mouseEvent.set(e);
    for (Object menuMaker : maker.menuMakers)
      pcallF(menuMaker, menu);
    
    // show menu if any items in it
    if (menu.getComponentCount() != emptyCount)
      menu.show(e.getComponent(), e.getX(), e.getY());
  }
}
static void set(Object o, String field, Object value) {
  if (o instanceof Class) set((Class) o, field, value);
  else try {
    Field f = set_findField(o.getClass(), field);
    smartSet(f, o, value);
  } catch (Exception e) {
    throw new RuntimeException(e);
  }
}

static void set(Class c, String field, Object value) {
  try {
    Field f = set_findStaticField(c, field);
    smartSet(f, null, value);
  } catch (Exception e) {
    throw new RuntimeException(e);
  }
}
  
static Field set_findStaticField(Class<?> c, String field) {
  Class _c = c;
  do {
    for (Field f : _c.getDeclaredFields())
      if (f.getName().equals(field) && (f.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0)
        return f;
    _c = _c.getSuperclass();
  } while (_c != null);
  throw new RuntimeException("Static field '" + field + "' not found in " + c.getName());
}

static Field set_findField(Class<?> c, String field) {
  Class _c = c;
  do {
    for (Field f : _c.getDeclaredFields())
      if (f.getName().equals(field))
        return f;
    _c = _c.getSuperclass();
  } while (_c != null);
  throw new RuntimeException("Field '" + field + "' not found in " + c.getName());
}
static Map<String, Integer> findBot_cache = synchroHashMap();
static int findBot_timeout = 5000;

static DialogIO findBot(String searchPattern) {
  // first split off sub-bot suffix
  String subBot = null;
  int i = searchPattern.indexOf('/');
  if (i >= 0 && (isJavaIdentifier(searchPattern.substring(0, i)) || isInteger(searchPattern.substring(0, i)))) {
    subBot = searchPattern.substring(i+1);
    searchPattern = searchPattern.substring(0, i);
    if (!isInteger(searchPattern))
      searchPattern = "Multi-Port at " + searchPattern + ".";
  }
  
  // assume it's a port if it's an integer
  if (isInteger(searchPattern))
    return talkToSubBot(subBot, talkTo(parseInt(searchPattern)));
    
  if (eq(searchPattern, "remote"))
    return talkToSubBot(subBot, talkTo("second.tinybrain.de", 4999));
    
  Integer port = findBot_cache.get(searchPattern);
  if (port != null) try {
    DialogIO io = talkTo("localhost", port);
    io.waitForLine(/*findBot_timeout*/); // TODO: implement
    String line = io.readLineNoBlock();
    if (indexOfIgnoreCase(line, searchPattern) == 0) {
      call(io, "pushback", line); // put hello string back in
      return talkToSubBot(subBot, io);
    }
  } catch (Exception e) {
    e.printStackTrace();
  }
  
  List<ProgramScan.Program> bots = quickBotScan();
  
  // find top-level bots
  for (ProgramScan.Program p : bots) {
    if (indexOfIgnoreCase(p.helloString, searchPattern) == 0) { // strict matching - start of hello string only, but case-insensitive
      findBot_cache.put(searchPattern, p.port);
      return talkToSubBot(subBot, talkTo("localhost", p.port));
    }
  }
  
  // find sub-bots
  for (ProgramScan.Program p : bots) {
    String botName = firstPartOfHelloString(p.helloString);
    boolean isVM = startsWithIgnoreCase(p.helloString, "This is a JavaX VM.");
    boolean shouldRecurse = startsWithIgnoreCase(botName, "Multi-Port") || isVM;
        
    if (shouldRecurse) try {
      Map<Number, String> subBots = (Map) unstructure(sendToLocalBotQuietly(p.port, "list bots"));
      for (Number vport : subBots.keySet()) {
        String name = subBots.get(vport);
        if (startsWithIgnoreCase(name, searchPattern))
          return talkToSubBot(vport.longValue(), talkTo("localhost", p.port));
      }
    } catch (Throwable __e) { print(exceptionToStringShort(__e)); }
  }
        
  return null;
}
static boolean checkTokCondition(Object condition, List<String> tok, int i) {
  if (condition instanceof TokCondition)
    return ((TokCondition) condition).get(tok, i);
  return checkCondition(condition, tok, i);
}
static void tableEnableDrag(final JTable table, TransferHandler th) {
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
static <A> HashSet<A> lithashset(A... items) {
  HashSet<A> set = new HashSet();
  for (A a : items) set.add(a);
  return set;
}
static byte[] toUtf8(String s) { try {
  return s.getBytes("UTF-8");
} catch (Exception __e) { throw rethrow(__e); } }
static void logQuoted(String logFile, String line) {
  logQuoted(getProgramFile(logFile), line);
}

static void logQuoted(File logFile, String line) {
  appendToFile(logFile, quote(line) + "\n");
}
static List<String> nlTok(String s) {
  return javaTokPlusPeriod(s);
}
static Object selectedTableCell(JTable t, int col) {
  return getTableCell(t, t.getSelectedRow(), col);
}

static Object selectedTableCell(JTable t) {
  return selectedTableCell(t, t.getSelectedColumn());
}
static String asString(Object o) {
  return o == null ? null : o.toString();
}
static String lastJavaToken(String s) {
  return last(javaTokC(s));
}
static boolean endsWithIgnoreCase(String a, String b) {
  int la = l(a), lb = l(b);
  return la >= lb && regionMatchesIC(a, la-lb, b, 0, lb);
}

static boolean endsWithIgnoreCase(String a, String b, Matches m) {
  if (!endsWithIgnoreCase(a, b)) return false;
  m.m = new String[] { substring(a, 0, l(a)-l(b)) };
  return true;
}
static void setConsoleFrame(JFrame frame) {
   setOpt(get(getJavaX(), "console"), "frame", frame);
}
static void nohupJavax(final String javaxargs) {
  { Thread _t_0 = new Thread() {
public void run() { try { call(hotwireOnce("#1008562"), "nohupJavax", javaxargs); } catch (Throwable __e) { printStackTrace2(__e); } }
};
startThread(_t_0); }
}

static void nohupJavax(final String javaxargs, final String vmArgs) {
  { Thread _t_1 = new Thread() {
public void run() { try { call(hotwireOnce("#1008562"), "nohupJavax", javaxargs, vmArgs); } catch (Throwable __e) { printStackTrace2(__e); } }
};
startThread(_t_1); }
}
static boolean isIdentifier(String s) {
  return isJavaIdentifier(s);
}
static List scanStructureLog(String progID, String fileName) {
  return scanStructureLog(getProgramFile(progID, fileName));
}

static List scanStructureLog(String fileName) {
  return scanStructureLog(getProgramFile(fileName));
}

static List scanStructureLog(File file) {
  List l = new ArrayList();
  for (String s : scanLog(file)) try {
    l.add(unstructure(s));
  } catch (Throwable __e) { printStackTrace2(__e); }
  return l;
}
static String getLookAndFeel() {
  return getClassName(UIManager.getLookAndFeel());
}
static String quoteUnlessIdentifierOrInteger(String s) {
  return quoteIfNotIdentifierOrInteger(s);
}
static void cleanKillVM() {
  ping();
  cleanKillVM_noSleep();
  sleep();
}

static void cleanKillVM_noSleep() {
  call(getJavaX(), "cleanKill");
}
// returns l(s) if not found
static int smartIndexOf(String s, String sub, int i) {
  if (s == null) return 0;
  i = s.indexOf(sub, min(i, l(s)));
  return i >= 0 ? i : l(s);
}

static int smartIndexOf(String s, int i, char c) {
  return smartIndexOf(s, c, i);
}

static int smartIndexOf(String s, char c, int i) {
  if (s == null) return 0;
  i = s.indexOf(c, min(i, l(s)));
  return i >= 0 ? i : l(s);
}

static int smartIndexOf(String s, String sub) {
  return smartIndexOf(s, sub, 0);
}

static int smartIndexOf(String s, char c) {
  return smartIndexOf(s, c, 0);
}

static <A> int smartIndexOf(List<A> l, A sub) {
  return smartIndexOf(l, sub, 0);
}

static <A> int smartIndexOf(List<A> l, int start, A sub) {
  return smartIndexOf(l, sub, start);
}

static <A> int smartIndexOf(List<A> l, A sub, int start) {
  int i = indexOf(l, sub, start);
  return i < 0 ? l(l) : i;
}
static String standardCredentials() {
  String user = standardCredentialsUser();
  String pass = standardCredentialsPass();
  if (nempty(user) && nempty(pass))
    return "&_user=" + urlencode(user) + "&_pass=" + urlencode(pass);
  return "";
}
static volatile String caseID_caseID;

static String caseID() { return caseID_caseID; }

static void caseID(String id) {
  caseID_caseID = id;
}
static boolean swic(String a, String b) {
  return startsWithIgnoreCase(a, b);
}


  static boolean swic(String a, String b, Matches m) {
    if (!swic(a, b)) return false;
    m.m = new String[] {substring(a, l(b))};
    return true;
  }

static File standardLogFile() {
  return getProgramFile("log");
}


static String symbol(String s) {
  
  return s;
  
  
}

static String symbol(CharSequence s) {
  if (s == null) return null;
  
  return str(s);
  
  
}

static String symbol(Object o) {
  return symbol((CharSequence) o);
}
static <A> int indexOfSubList(List<A> x, List<A> y) {
  return indexOfSubList(x, y, 0);
}

static <A> int indexOfSubList(List<A> x, List<A> y, int i) {
  outer: for (; i+l(y) <= l(x); i++) {
    for (int j = 0; j < l(y); j++)
      if (neq(x.get(i+j), y.get(j)))
        continue outer;
    return i;
  }
  return -1;
}

static <A> int indexOfSubList(List<A> x, A[] y, int i) {
  outer: for (; i+l(y) <= l(x); i++) {
    for (int j = 0; j < l(y); j++)
      if (neq(x.get(i+j), y[j]))
        continue outer;
    return i;
  }
  return -1;
}
static <A, B> HashMap<A, B> pairsToMap(Collection<? extends Pair<A, B>> l) {
  HashMap<A,B> map = new HashMap();
  if (l != null) for (Pair<A, B> p : l)
    map.put(p.a, p.b);
  return map;
}
static JFrame renewFrame(final JFrame frame) {
  if (frame == null) return null;
  return (JFrame) swing(new F0<Object>() { Object get() { try { 
    Container content = frame.getContentPane();
    JFrame frame2 = makeFrame(frame.getTitle());
    frame2.setBounds(frame.getBounds());
    try { frame2.setIconImages(frame.getIconImages()); } catch (Throwable __e) { printStackTrace2(__e); }
    frame2.setDefaultCloseOperation(frame.getDefaultCloseOperation());
    for (WindowListener wl : frame.getWindowListeners())
      frame2.addWindowListener(wl);
    frame.setContentPane(new JPanel());
    frame2.setContentPane(content);
    frame2.setVisible(true);
    frame.dispose();
    return frame2;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "Container content = frame.getContentPane();\r\n    JFrame frame2 = makeFrame(fr..."; }});
}
static String _computerID;
public static String computerID() { try {
  if (_computerID == null) {
    File file = new File(userHome(), ".tinybrain/computer-id");
    _computerID = loadTextFile(file.getPath(), null);
    if (_computerID == null) {
      _computerID = makeRandomID(12);
      saveTextFile(file.getPath(), _computerID);
    }
  }
  return _computerID;
} catch (Exception __e) { throw rethrow(__e); } }
static String structure_addTokenMarkers(String s) {
  List<String> tok = javaTok(s);
  
  // find references
  
  TreeSet<Integer> refs = new TreeSet();
  for (int i = 1; i < l(tok); i += 2) {
    String t = tok.get(i);
    if (t.startsWith("t") && isInteger(t.substring(1)))
      refs.add(parseInt(t.substring(1)));
  }
  
  if (empty(refs)) return s;
  
  // add markers
  for (int i : refs) {
    int idx = i*2+1;
    String t = "";
    if (endsWithLetterOrDigit(tok.get(idx-1))) t = " ";
    tok.set(idx, t + "m" + i + " " + tok.get(idx));
  }
  
  return join(tok);
}
static boolean exposeMethods2_debug;

static String exposeMethods2(Object receiver, String s, List<String> methodNames) {
  return exposeMethods2(receiver, s, methodNames, null);
}

static String exposeMethods2(Object receiver, String s, List<String> methodNames,
  Lock lock) {
  Matches m = new Matches();
  if (exposeMethods2_debug) print("Received: " + s);
  if (match("call *", s, m)) {
    List l;
    if (isIdentifier(m.unq(0)))
      l = ll(m.unq(0));
    else
      l = (List) unstructure(m.unq(0)); // we used to have safeUnstructure here
    String method = getString(l, 0);
    if (!contains(methodNames, method))
      throw fail("Method not allowed: " + method);
    if (lock != null) lock.lock();
    try {
      if (exposeMethods2_debug) print("Calling: " + method);
      Object o = call(receiver, method, asObjectArray(subList(l, 1)));
      if (exposeMethods2_debug) print("Got: " + getClassName(o));
      return ok2(structure(o));
    } finally {
      if (lock != null) lock.unlock();
    }
  }
  if (match("list methods", s))
    return ok2(structure(methodNames));
  return null;
}
static byte[] convertToPNG(BufferedImage img) { try {
  ByteArrayOutputStream stream = new ByteArrayOutputStream();
  ImageIO.write(img, "png", stream);
  return stream.toByteArray();
} catch (Exception __e) { throw rethrow(__e); } }
static boolean isExtendedUnicodeCharacter(String s, int idx) {
  return Character.charCount(s.codePointAt(idx)) > 1;
}
static File loadDataSnippetToFile(String snippetID) { try {
  snippetID = fsI(snippetID);
  File f = DiskSnippetCache_file(parseSnippetID(snippetID));
  try {
    URL url = new URL(dataSnippetLink(snippetID));
    print("Loading library: " + hideCredentials(url));
    try {
      loadBinaryPageToFile(openConnection(url), f);
      if (fileSize(f) == 0) throw fail();
    } catch (Throwable _e) {
      url = new URL("http://data.tinybrain.de/blobs/"
        + psI(snippetID));
      print("Trying other server: " + hideCredentials(url));
      loadBinaryPageToFile(openConnection(url), f);
      print("Got bytes: " + fileSize(f));
    }
    // TODO: check if we hit the "LOADING" message
    if (fileSize(f) == 0) throw fail();
    System.err.println("Bytes loaded: " + fileSize(f));
  } catch (Throwable e) {
    printStackTrace(e);
    throw fail("Binary snippet " + snippetID + " not found or not public");
  }
  return f;
} catch (Exception __e) { throw rethrow(__e); } }
static File newFile(File base, String... names) {
  for (String name : names) base = new File(base, name);
  return base;
}

static File newFile(String name) {
  return name == null ? null : new File(name);
}
static <A, B> Pair<A, B> pair(A a, B b) {
  return new Pair(a, b);
}

static <A> Pair<A, A> pair(A a) {
  return new Pair(a, a);
}
static void consoleClearInput() {
  consoleSetInput("");
}
static String intToHex(int i) {
  return bytesToHex(intToBytes(i));
}
static File DiskSnippetCache_file(long snippetID) {
  return new File(getGlobalCache(), "data_" + snippetID + ".jar");
}
  
  // Data files are immutable, use centralized cache
public static File DiskSnippetCache_getLibrary(long snippetID) throws IOException {
  File file = DiskSnippetCache_file(snippetID);
  return file.exists() ? file : null;
}

public static void DiskSnippetCache_putLibrary(long snippetID, byte[] data) throws IOException {
  saveBinaryFile(DiskSnippetCache_file(snippetID), data);
}

static byte[] loadDataSnippetImpl(String snippetID) throws IOException {
  byte[] data;
  try {
    URL url = new URL("http://eyeocr.sourceforge.net/filestore/filestore.php?cmd=serve&file=blob_"
      + parseSnippetID(snippetID) + "&contentType=application/binary");
    System.err.println("Loading library: " + url);
    try {
      data = loadBinaryPage(url.openConnection());
    } catch (RuntimeException e) {
      data = null;
    }
    
    if (data == null || data.length == 0) {
      url = new URL("http://data.tinybrain.de/blobs/"
        + parseSnippetID(snippetID));
      System.err.println("Loading library: " + url);
      data = loadBinaryPage(url.openConnection());
    }
    System.err.println("Bytes loaded: " + data.length);
  } catch (FileNotFoundException e) {
    throw new IOException("Binary snippet #" + snippetID + " not found or not public");
  }
  return data;
}
  static boolean matchStart(String pat, String s) {
    return matchStart(pat, s, null);
  }
  
  // matches are as you expect, plus an extra item for the rest string
  static boolean matchStart(String pat, String s, Matches matches) {
    if (s == null) return false;
    List<String> tokpat = parse3(pat), toks = parse3(s);
    if (toks.size() < tokpat.size()) return false;
    String[] m = match2(tokpat, toks.subList(0, tokpat.size()));
    //print(structure(tokpat) + " on " + structure(toks) + " => " + structure(m));
    if (m == null)
      return false;
    else {
      if (matches != null) {
        matches.m = new String[m.length+1];
        arraycopy(m, matches.m);
        matches.m[m.length] = join(toks.subList(tokpat.size(), toks.size())); // for Matches.rest()
      }
      return true;
    }
  }
static boolean isNormalQuoted(String s) {
  int l = l(s);
  if (!(l >= 2 && s.charAt(0) == '"' && lastChar(s) == '"')) return false;
  int j = 1;
  while (j < l)
    if (s.charAt(j) == '"')
      return j == l-1;
    else if (s.charAt(j) == '\\' && j+1 < l)
      j += 2;
    else
      ++j;
  return false;
}
static boolean isMultilineQuoted(String s) {
  if (!startsWith(s, "[")) return false;
  int i = 1;
  while (i < s.length() && s.charAt(i) == '=') ++i;
  return i < s.length() && s.charAt(i) == '[';
}
static String className(Object o) {
  return getClassName(o);
}


static String formatGMTWithDate_24(long time) {
  return simpleDateFormat_GMT("yyyy/MM/dd").format(time) + " " + formatGMT_24(time);
}
static List<String> getClassNames(Collection l) {
  List<String> out = new ArrayList();
  if (l != null) for (Object o : l)
    out.add(o == null ? null : getClassName(o));
  return out;
}
static int indexOfIgnoreCase_manual(String a, String b) {
  int la = l(a), lb = l(b);
  if (la < lb) return -1;
  int n = la-lb;
  
  loop: for (int i = 0; i <= n; i++) {
    for (int j = 0; j < lb; j++) {
      char c1 = a.charAt(i+j), c2 = b.charAt(j);
      if (!eqic(c1, c2))
        continue loop;
    }
    return i;
  }
  return -1;
}
static File getProgramDir() {
  return programDir();
}

static File getProgramDir(String snippetID) {
  return programDir(snippetID);
}
static long fileSize(String path) { return getFileSize(path); }
static long fileSize(File f) { return getFileSize(f); }

static Class hotwireOnce(String programID) {
  return hotwireCached(programID, false);
}
static String fromUtf8(byte[] bytes) { try {
  return new String(bytes, "UTF-8");
} catch (Exception __e) { throw rethrow(__e); } }
static String sendToLocalBotOpt(String bot, String text, Object... args) {
  if (bot == null) return null;
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
static File userDir() {
  return new File(userHome());
}

static File userDir(String path) {
  return new File(userHome(), path);
}


static TreeMap<String,Class> hotwireCached_cache = new TreeMap();
static Lock hotwireCached_lock = lock();

static Class hotwireCached(String programID) {
  return hotwireCached(programID, true);
}

static Class hotwireCached(String programID, boolean runMain) {
  return hotwireCached(programID, runMain, false);
}

static Class hotwireCached(String programID, boolean runMain, boolean dependent) {
  Lock _lock_1227 = hotwireCached_lock; lock(_lock_1227); try {
  
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
} finally { unlock(_lock_1227); } }
static boolean contains(Collection c, Object o) {
  return c != null && c.contains(o);
}

static boolean contains(Object[] x, Object o) {
  if (x != null)
    for (Object a : x)
      if (eq(a, o))
        return true;
  return false;
}

static boolean contains(String s, char c) {
  return s != null && s.indexOf(c) >= 0;
}

static boolean contains(String s, String b) {
  return s != null && s.indexOf(b) >= 0;
}
static boolean startsWithIgnoreCase(String a, String b) {
  return regionMatchesIC(a, 0, b, 0, b.length());
}
/** writes safely (to temp file, then rename) */
static File saveTextFile(String fileName, String contents) throws IOException {
  CriticalAction action = beginCriticalAction("Saving file " + fileName + " (" + l(contents) + " chars)");
  try {
    File file = new File(fileName);
    File parentFile = file.getParentFile();
    if (parentFile != null)
      parentFile.mkdirs();
    String tempFileName = fileName + "_temp";
    File tempFile = new File(tempFileName);
    if (contents != null) {
      if (tempFile.exists()) try {
        String saveName = tempFileName + ".saved." + now();
        copyFile(tempFile, new File(saveName));
      } catch (Throwable e) { printStackTrace(e); }
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

static File saveTextFile(File fileName, String contents) { try {
  saveTextFile(fileName.getPath(), contents);
  return fileName;
} catch (Exception __e) { throw rethrow(__e); } }
static String sendToLocalBotQuietly(String bot, String text, Object... args) {
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

static String sendToLocalBotQuietly(int port, String text, Object... args) {
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
  /** writes safely (to temp file, then rename) */
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

  static void saveBinaryFile(File fileName, byte[] contents) {
    try {
      saveBinaryFile(fileName.getPath(), contents);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
static String getString(Map map, Object key) {
  return map == null ? null : (String) map.get(key);
}

static String getString(List l, int idx) {
  return (String) get(l, idx);
}

static String getString(Object o, Object key) {
  if (o instanceof Map) return getString((Map) o, key);
  if (key instanceof String)
    return (String) getOpt(o, (String) key);
  throw fail("Not a string key: " + getClassName(key));
}
static String dataSnippetLink(String snippetID) {
  long id = parseSnippetID(snippetID);
  if (id >= 1200000 && id < 1300000) { // Woody files, actually
    String pw = muricaPassword();
    if (empty(pw)) throw fail("Please set 'murica password by running #1008829");
    return "http://butter.botcompany.de:8080/1008823/raw/" + id + "?_pass=" + pw; // XXX, although it typically gets hidden when printing
  } else
    return "http://eyeocr.sourceforge.net/filestore/filestore.php?cmd=serve&file=blob_"
      + id + "&contentType=application/binary";
}


static SimpleDateFormat simpleDateFormat_GMT(String format) {
  SimpleDateFormat sdf = new SimpleDateFormat(format);
  sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
  return sdf;
}
static long getFileSize(String path) {
  return path == null ? 0 : new File(path).length();
}

static long getFileSize(File f) {
  return f == null ? 0 : f.length();
}


static String formatGMT_24(long time) {
  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
  sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
  return sdf.format(time) + " GMT";
}
static String quoteIfNotIdentifierOrInteger(String s) {
  if (s == null) return null;
  return isJavaIdentifier(s) || isInteger(s) ? s : quote(s);
}
static boolean regionMatchesIC(String a, int offsetA, String b, int offsetB, int len) {
  
  
    return a != null && a.regionMatches(true, offsetA, b, offsetB, len);
  
}
static boolean endsWithLetterOrDigit(String s) {
  return nempty(s) && isLetterOrDigit(lastCharacter(s));
}
static void loadBinaryPageToFile(String url, File file) { try {
  print("Loading " + url);
  loadBinaryPageToFile(openConnection(new URL(url)), file);
} catch (Exception __e) { throw rethrow(__e); } }

static void loadBinaryPageToFile(URLConnection con, File file) { try {
  setHeaders(con);
  loadBinaryPageToFile_noHeaders(con, file);
} catch (Exception __e) { throw rethrow(__e); } }

static void loadBinaryPageToFile_noHeaders(URLConnection con, File file) { try {
  File ftemp = new File(f2s(file) + "_temp");
  FileOutputStream buf = newFileOutputStream(mkdirsFor(ftemp));
  try {
    InputStream inputStream = con.getInputStream();
    long len = 0;
    try { len = con.getContentLengthLong(); } catch (Throwable e) { printStackTrace(e); }
    String pat = "  {*}" + (len != 0 ? "/" + len : "") + " bytes loaded.";
    copyStreamWithPrints(inputStream, buf, pat);
    inputStream.close();
    buf.close();
    file.delete();
    renameFile_assertTrue(ftemp, file);
  } finally {
    if (buf != null) buf.close();
  }
} catch (Exception __e) { throw rethrow(__e); } }

static Lock appendToFile_lock = lock();
static boolean appendToFile_keepOpen;
static HashMap<String,Writer> appendToFile_writers = new HashMap();

static void appendToFile(String path, String s) { try {
  Lock _lock_1256 = appendToFile_lock; lock(_lock_1256); try { // Let's just generally synchronize this to be safe.
  new File(path).getParentFile().mkdirs();
  path = getCanonicalPath(path);
  Writer writer = appendToFile_writers.get(path);
  if (writer == null) {
    //print("[Logging to " + path + "]");
    writer = new BufferedWriter(new OutputStreamWriter(
      newFileOutputStream(path, true), "UTF-8"));
    if (appendToFile_keepOpen)
      appendToFile_writers.put(path, writer);
  }
  writer.write(s);
  if (!appendToFile_keepOpen)
    writer.close();
} finally { unlock(_lock_1256); } } catch (Exception __e) { throw rethrow(__e); } }
  
static void appendToFile(File path, String s) {
  if (path != null)
    appendToFile(path.getPath(), s);
}

static void cleanMeUp_appendToFile() {
  Lock _lock_1257 = appendToFile_lock; lock(_lock_1257); try {
  closeAllWriters(values(appendToFile_writers));
  appendToFile_writers.clear();
} finally { unlock(_lock_1257); } }
static String ok2(String s) {
  return "ok " + s;
}
static ThreadLocal<Map<String, List<String>>> loadBinaryPage_responseHeaders = new ThreadLocal();
static ThreadLocal<Map<String, String>> loadBinaryPage_extraHeaders = new ThreadLocal();

static byte[] loadBinaryPage(String url) { try {
  print("Loading " + url);
  return loadBinaryPage(loadPage_openConnection(new URL(url)));
} catch (Exception __e) { throw rethrow(__e); } }

static byte[] loadBinaryPage(URLConnection con) { try {
  Map<String, String> extraHeaders = getAndClearThreadLocal(loadBinaryPage_extraHeaders);
  setHeaders(con);
  for (String key : keys(extraHeaders))
    con.setRequestProperty(key, extraHeaders.get(key));
  return loadBinaryPage_noHeaders(con);
} catch (Exception __e) { throw rethrow(__e); } }

static byte[] loadBinaryPage_noHeaders(URLConnection con) { try {
  ByteArrayOutputStream buf = new ByteArrayOutputStream();
  InputStream inputStream = con.getInputStream();
  loadBinaryPage_responseHeaders.set(con.getHeaderFields());
  long len = 0;
  try { len = con.getContentLengthLong(); } catch (Throwable e) { printStackTrace(e); }
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
} catch (Exception __e) { throw rethrow(__e); } }

static DialogIO talkToSubBot(final long vport, final DialogIO io) {
  return talkToSubBot(String.valueOf(vport), io);
}

static DialogIO talkToSubBot(final String subBot, final DialogIO io) {
  if (subBot == null) return io;
  return new talkToSubBot_IO(subBot, io);
}

static class talkToSubBot_IO extends DialogIO {
  String subBot;
  DialogIO io;
  
  talkToSubBot_IO(String subBot, DialogIO io) {
  this.io = io;
  this.subBot = subBot;}
  
  // delegate all but sendLine
  boolean isStillConnected() { return io.isStillConnected(); }
  String readLineImpl() { return io.readLineImpl(); }
  boolean isLocalConnection() { return io.isLocalConnection(); }
  Socket getSocket() { return io.getSocket(); }
  void close() { io.close(); }

  void sendLine(String line) {
    io.sendLine(format3("please forward to bot *: *", subBot, line));
  }
}
static void arraycopy(Object[] a, Object[] b) {
  int n = min(a.length, b.length);
  for (int i = 0; i < n; i++)
    b[i] = a[i];
}

static void arraycopy(Object src, int srcPos, Object dest, int destPos, int n) {
  System.arraycopy(src, srcPos, dest, destPos, n);
}
static List<String> scanLog(String progID, String fileName) {
  return scanLog(getProgramFile(progID, fileName));
}

static List<String> scanLog(String fileName) {
  return scanLog(getProgramFile(fileName));
}

static List<String> scanLog(File file) {
  List<String> l = new ArrayList();
  for (File f : concatLists(earlierPartsOfLogFile(file), ll(file)))
    for (String s : toLines(file))
      if (isProperlyQuoted(s)) l.add(unquote(s));
  return l;
}
static File getGlobalCache() {
  File file = new File(javaxCachesDir(), "Binary Snippets");
  file.mkdirs();
  return file;
}

static String firstPartOfHelloString(String s) {
  int i = s.lastIndexOf('/');
  return i < 0 ? s : rtrim(s.substring(0, i));
}
static File programDir_mine; // set this to relocate program's data

static File programDir() {
  return programDir(getProgramID());
}

static File programDir(String snippetID) {
  boolean me = sameSnippetID(snippetID, programID());
  if (programDir_mine != null && me)
    return programDir_mine;
  File dir = new File(javaxDataDir(), formatSnippetID(snippetID));
  if (me) {
    String c = caseID();
    if (nempty(c)) dir = newFile(dir, c);
  }
  return dir;
}

static File programDir(String snippetID, String subPath) {
  return new File(programDir(snippetID), subPath);
}
static boolean checkCondition(Object condition, Object... args) {
  return isTrue(callF(condition, args));
}
static void consoleSetInput(final String text) {
  if (headless()) return;
  setTextAndSelectAll(consoleInputField(), text);
  focusConsole();
}
static List<ProgramScan.Program> quickBotScan() {
  return ProgramScan.quickBotScan();
}

static List<ProgramScan.Program> quickBotScan(int[] preferredPorts) {
  return ProgramScan.quickBotScan(preferredPorts);
}

static List<ProgramScan.Program> quickBotScan(String searchPattern) {
  List<ProgramScan.Program> l = new ArrayList<ProgramScan.Program>();
  for (ProgramScan.Program p : ProgramScan.quickBotScan())
    if (indexOfIgnoreCase(p.helloString, searchPattern) == 0)
      l.add(p);
  return l;
}

static String standardCredentialsUser() {
  return trim(loadTextFile(new File(userHome(), ".tinybrain/username")));
}
static DialogIO talkTo(int port) {
  return talkTo("localhost", port);
}

static int talkTo_defaultTimeout = 10000; // This is the CONNECT timeout
static int talkTo_timeoutForReads = 0; // Timeout waiting for answers (0 = no timeout)

static ThreadLocal<Map<String, DialogIO>> talkTo_byThread = new ThreadLocal();

static DialogIO talkTo(String ip, int port) { try {
  String full = ip + ":" + port;
  Map<String, DialogIO> map = talkTo_byThread.get();
  if (map != null && map.containsKey(full)) return map.get(full);
  
  if (isLocalhost(ip) && port == vmPort()) return talkToThisVM();

  return new talkTo_IO(ip, port);
} catch (Exception __e) { throw rethrow(__e); } }

static class talkTo_IO extends DialogIO { 
  String ip;
  int port;
  Socket s;
  Writer w;
  BufferedReader in;
  
  talkTo_IO(String ip, int port) {
  this.port = port;
  this.ip = ip; try {
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
  } catch (Exception __e) { throw rethrow(__e); } }
  
  boolean isLocalConnection() {
    return s.getInetAddress().isLoopbackAddress();
  }
  
  boolean isStillConnected() {
    return !(eos || s.isClosed());
  }
  
  void sendLine(String line) { try {
    w.write(line + "\n");
    w.flush();
  } catch (Exception __e) { throw rethrow(__e); } }
  
  String readLineImpl() { try {
    return in.readLine();
  } catch (Exception __e) { throw rethrow(__e); } }
  
  void close() {
    try {
      if (!noClose) s.close();
    } catch (IOException e) {
      // whatever
    }
  }
  
  Socket getSocket() {
    return s;
  }
}
  static String format(String pat, Object... args) {
    return format3(pat, args);
  }

static String standardCredentialsPass() {
  return trim(loadTextFile(new File(userHome(), ".tinybrain/userpass")));
}


static Map<String, Integer> newFindBot2_cache = synchroHashMap();
static boolean newFindBot2_verbose;

static DialogIO newFindBot2(String name) {
  Integer port = newFindBot2_cache.get(name);
  if (port != null) {
    if (newFindBot2_verbose)
      print("newFindBot2: testing " + name + " => " + port);
    DialogIO io = talkTo(port);
    String q = format("has bot *", name);
    String s = io.ask(q);
    if (match("yes", s)) {
      io = talkToSubBot(name, io);
      call(io, "pushback", "?"); // put some hello string in (yes, this should be improved.)
      return io;
    }
    // bot not there anymore - remove cache entry
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
static List<CriticalAction> beginCriticalAction_inFlight = synchroList();

static class CriticalAction {
  String description;
  
  CriticalAction() {}
  CriticalAction(String description) {
  this.description = description;}
  
  void done() {
    beginCriticalAction_inFlight.remove(this);
  }
}

static CriticalAction beginCriticalAction(String description) {
  ping();
  CriticalAction c = new CriticalAction(description);
  beginCriticalAction_inFlight.add(c);
  return c;
}

static void cleanMeUp_beginCriticalAction() {
  int n = 0;
  while (nempty(beginCriticalAction_inFlight)) {
    int m = l(beginCriticalAction_inFlight);
    if (m != n) {
      n = m;
      try {
        print("Waiting for " + n(n, "critical actions") + ": " + join(", ", collect(beginCriticalAction_inFlight, "description")));
      } catch (Throwable __e) { printStackTrace2(__e); }
    }
    sleepInCleanUp(10);
  }
}
static boolean isProperlyQuoted(String s) {
  return s.length() >= 2
    && s.startsWith("\"")
    && s.endsWith("\"")
    && (!s.endsWith("\\\"") || s.endsWith("\\\\\""));
}
static JTextField setTextAndSelectAll(final JTextField tf, final String text) {
  { swing(new Runnable() { public void run() { try { 
    tf.setText(text);
    tf.selectAll();
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "tf.setText(text);\r\n    tf.selectAll();"; }}); }
  return tf;
}
static int vmPort() {
  return myVMPort();
}
static String getCanonicalPath(String path) { try {
  return new File(path).getCanonicalPath();
} catch (Exception __e) { throw rethrow(__e); } }
  static String format3(String pat, Object... args) {
    if (args.length == 0) return pat;
    
    List<String> tok = javaTokPlusPeriod(pat);
    int argidx = 0;
    for (int i = 1; i < tok.size(); i += 2)
      if (tok.get(i).equals("*"))
        tok.set(i, format3_formatArg(argidx < args.length ? args[argidx++] : "null"));
    return join(tok);
  }
  
  static String format3_formatArg(Object arg) {
    if (arg == null) return "null";
    if (arg instanceof String) {
      String s = (String) arg;
      return isIdentifier(s) || isNonNegativeInteger(s) ? s : quote(s);
    }
    if (arg instanceof Integer || arg instanceof Long) return String.valueOf(arg);
    return quote(structure(arg));
  }
  

static FileOutputStream newFileOutputStream(File path) throws IOException {
  return newFileOutputStream(path.getPath());
}

static FileOutputStream newFileOutputStream(String path) throws IOException {
  return newFileOutputStream(path, false);
}

static FileOutputStream newFileOutputStream(String path, boolean append) throws IOException {
  mkdirsForFile(path);
  FileOutputStream f = new // Line break for ancient translator
    FileOutputStream(path, append);
  
  callJavaX("registerIO", f, path, true);
  
  return f;
}
static void focusConsole(String s) {
  setConsoleInput(s);
  focusConsole();
}

static void focusConsole() {
  JComponent tf = consoleInputFieldOrComboBox();
  if (tf != null) {
    //print("Focusing console");
    tf.requestFocus();
  }
}
static void copyStreamWithPrints(InputStream in, OutputStream out, String pat) { try {
  byte[] buf = new byte[65536];
  int total = 0;
  while (true) {
    int n = in.read(buf);
    if (n <= 0) return;
    out.write(buf, 0, n);
    if ((total+n)/100000 > total/100000)
      print(pat.replace("{*}", str(roundDownTo(total, 100000))));
    total += n;
  }
} catch (Exception __e) { throw rethrow(__e); } }
static void lock(Lock lock) { try {
  ping();
  lock.lockInterruptibly();
  ping();
} catch (Exception __e) { throw rethrow(__e); } }

static void lock(Lock lock, String msg) {
  print("Locking: " + msg);
  lock(lock);
}

static void lock(Lock lock, String msg, long timeout) {
  print("Locking: " + msg);
  lockOrFail(lock, timeout);
}

static ReentrantLock lock() {
  return fairLock();
}
static void closeAllWriters(Collection<? extends Writer> l) {
  for (Writer w : unnull(l)) try {
    w.close();
  } catch (Throwable __e) { printStackTrace2(__e); }
}
static DialogIO talkToThisVM() {
  return new talkToThisVM_IO();
}

static class talkToThisVM_IO extends DialogIO { 
  List<String> answers = ll(thisVMGreeting());
  
  boolean isLocalConnection() { return true; }
  boolean isStillConnected() { return true; }
  int getPort() { return vmPort(); }
  
  void sendLine(String line) {
    answers.add(or2(sendToThisVM_newThread(line), "?"));
  }
  
  String readLineImpl() { try {
    return popFirst(answers);
  } catch (Exception __e) { throw rethrow(__e); } }
  
  void close() {}
  Socket getSocket() { return null; }
}
static void makeDependent(Object c) {
  if (c == null) return;
  assertTrue("Not a class", c instanceof Class);
  hotwire_classes.add(new WeakReference(c));
  
  Object local_log = getOpt(mc(), "local_log");
  if (local_log != null)
    setOpt(c, "local_log", local_log);
    
  Object print_byThread = getOpt(mc(), "print_byThread");
  if (print_byThread != null)
    setOpt(c, "print_byThread", print_byThread);
}

static void renameFile_assertTrue(File a, File b) { try {
  if (!a.exists()) throw fail("Source file not found: " + f2s(a));
  if (b.exists()) throw fail("Target file exists: " + f2s(b));
  mkdirsForFile(b);
  
  
  if (!a.renameTo(b))
    throw fail("Can't rename " + f2s(a) + " to " + f2s(b));
  
} catch (Exception __e) { throw rethrow(__e); } }
static String f2s(File f) {
  return f == null ? null : f.getAbsolutePath();
}
static boolean isLetterOrDigit(char c) {
  return Character.isLetterOrDigit(c);
}
static <A> A println(A a) {
  return print(a);
}
public static File mkdirsFor(File file) {
  return mkdirsForFile(file);
}

static List<File> earlierPartsOfLogFile(File file) {
  String name = file.getName() + ".part";
  try {
    Matches m = new Matches();
    TreeMap<Integer,File> map = new TreeMap();
    for (File p : listFiles(file.getParent())) try {
      String n = p.getName();
      if (startsWith(n, name, m))
        map.put(parseFirstInt(m.rest()), p);
    } catch (Throwable __e) { printStackTrace2(__e); }
    return valuesList(map);
  } catch (Throwable e) { printException(e);
    return ll();
  }
}
static void unlock(Lock lock, String msg) {
  print("Unlocking: " + msg);
  lock.unlock();
}

static void unlock(Lock lock) {
  lock.unlock();
}
static String muricaPassword() {
  return trim(loadTextFile(muricaPasswordFile()));
}
static JTextField consoleInputField() {
  Object console = get(getJavaX(), "console");
  return (JTextField) getOpt(console, "tfInput");
}
public static void copyFile(File src, File dest) { try {
  mkdirsForFile(dest);
  FileInputStream inputStream = new FileInputStream(src.getPath());
  FileOutputStream outputStream = newFileOutputStream(dest.getPath());
  try {
    copyStream(inputStream, outputStream);
    inputStream.close();
  } finally {
    outputStream.close();
  }
} catch (Exception __e) { throw rethrow(__e); } }
static File javaxCachesDir_dir; // can be set to work on different base dir

static File javaxCachesDir() {
  return javaxCachesDir_dir != null ? javaxCachesDir_dir : new File(userHome(), "JavaX-Caches");
}
static IterableIterator<String> toLines(File f) {
  return linesFromFile(f);
}

static List<String> toLines(String s) {
  List<String> lines = new ArrayList<String>();
  if (s == null) return lines;
  int start = 0;
  while (true) {
    int i = toLines_nextLineBreak(s, start);
    if (i < 0) {
      if (s.length() > start) lines.add(s.substring(start));
      break;
    }

    lines.add(s.substring(start, i));
    if (s.charAt(i) == '\r' && i+1 < s.length() && s.charAt(i+1) == '\n')
      i += 2;
    else
      ++i;

    start = i;
  }
  return lines;
}

static int toLines_nextLineBreak(String s, int start) {
  for (int i = start; i < s.length(); i++) {
    char c = s.charAt(i);
    if (c == '\r' || c == '\n')
      return i;
  }
  return -1;
}
static boolean sameSnippetID(String a, String b) {
  if (!isSnippetID(a) || !isSnippetID(b)) return false;
  return parseSnippetID(a) == parseSnippetID(b);
}
static char lastCharacter(String s) {
  return empty(s) ? 0 : s.charAt(l(s)-1);
}
static boolean isLocalhost(String ip) {
  return isLoopbackIP(ip) || eqic(ip, "localhost");
}


static List<WeakReference<Class>> hotwire_classes = synchroList();

static Class<?> hotwireDependent(String src) {
  Class c = hotwire(src);
  makeDependent(c);
  return c;
}

static File muricaPasswordFile() {
  return new File(javaxSecretDir(), "murica/muricaPasswordFile");
}
static String thisVMGreeting() {
  List record_list = (List) ( get(getJavaX(), "record_list"));
  Object android = first(record_list); // Should be of class Android3
  return getString(android, "greeting");
}
static void setConsoleInput(String text) {
  consoleSetInput(text);
}
static boolean isNonNegativeInteger(String s) {
  return s != null && Pattern.matches("\\d+", s);
}
static void sleepInCleanUp(long ms) { try {
  if (ms < 0) return;
  Thread.sleep(ms);
} catch (Exception __e) { throw rethrow(__e); } }
static File[] listFiles(File dir) {
  File[] files = dir.listFiles();
  return files == null ? new File[0] : files;
}

static File[] listFiles(String dir) {
  return listFiles(new File(dir));
}
static int roundDownTo(int x, int n) {
  return x/n*n;
}

static long roundDownTo(long x, long n) {
  return x/n*n;
}
static <A, B> List<B> valuesList(Map<A, B> map) {
  return cloneListSynchronizingOn(values(map), map);
}
static int myVMPort() {
  List records = (List) ( get(getJavaX(), "record_list"));
  Object android = records.get(records.size()-1);
  return (Integer) get(android, "port");
}
static JComponent consoleInputFieldOrComboBox() {
  Object console = get(getJavaX(), "console");
  JComboBox cb = (JComboBox) ( getOpt(console, "cbInput"));
  if (cb != null) return cb;
  return (JTextField) getOpt(console, "tfInput");
}
static int parseFirstInt(String s) {
  return parseInt(jextract("<int>", s));
}
static void lockOrFail(Lock lock, long timeout) { try {
  ping();
  if (!lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
    String s = "Couldn't acquire lock after " + timeout + " ms.";
    if (lock instanceof ReentrantLock) {
      ReentrantLock l = (ReentrantLock) ( lock);
      s += " Hold count: " + l.getHoldCount() + ", owner: " + call(l, "getOwner");
    }
    throw fail(s);
  }
  ping();
} catch (Exception __e) { throw rethrow(__e); } }
static boolean isLoopbackIP(String ip) {
  return eq(ip, "127.0.0.1");
}
static IterableIterator<String> linesFromFile(File f) { try {
  if (!f.exists()) return emptyIterableIterator();
  if (ewic(f.getName(), ".gz"))
    return linesFromReader(utf8bufferedReader(new GZIPInputStream(new FileInputStream(f))));
  return linesFromReader(utf8bufferedReader(f));
} catch (Exception __e) { throw rethrow(__e); } }
public static File mkdirsForFile(File file) {
  File dir = file.getParentFile();
  if (dir != null) // is null if file is in current dir
    dir.mkdirs();
  return file;
}

public static String mkdirsForFile(String path) {
  mkdirsForFile(new File(path));
  return path;
}
static Object callJavaX(String method, Object... args) {
  return callOpt(getJavaX(), method, args);
}
static ReentrantLock fairLock() {
  return new ReentrantLock(true);
}
static void copyStream(InputStream in, OutputStream out) { try {
  byte[] buf = new byte[65536];
  while (true) {
    int n = in.read(buf);
    if (n <= 0) return;
    out.write(buf, 0, n);
  }
} catch (Exception __e) { throw rethrow(__e); } }
static String sendToThisVM_newThread(String s, Object... args) {
  final String _s = format(s, args);
  try {
    return (String) evalInNewThread(new F0<Object>() { Object get() { try { return 
      callStaticAnswerMethod(getJavaX(), _s)
    ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "callStaticAnswerMethod(getJavaX(), _s)"; }});
  } catch (Throwable e) {
    e = getInnerException(e);
    printStackTrace(e);
    return str(e);
  }
}
static <A> A popFirst(List<A> l) {
  if (empty(l)) return null;
  A a = first(l);
  l.remove(0);
  return a;
}


static File javaxSecretDir_dir; // can be set to work on different base dir

static File javaxSecretDir() {
  return javaxSecretDir_dir != null ? javaxSecretDir_dir : new File(userHome(), "JavaX-Secret");
}
static <A> ArrayList<A> cloneListSynchronizingOn(Collection<A> l, Object mutex) {
  if (l == null) return new ArrayList();
  synchronized(mutex) {
    return new ArrayList<A>(l);
  }
}
static IterableIterator<String> linesFromReader(Reader r) {
  final BufferedReader br = bufferedReader(r);
  return iteratorFromFunction_f0(new F0<String>() { String get() { try { return  readLineFromReaderWithClose(br) ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "readLineFromReaderWithClose(br)"; }});
}
static Object evalInNewThread(final Object f) {
  final Flag flag = new Flag();
  final Var var = new Var();
  final Var<Throwable> exception = new Var();
  { Thread _t_0 = new Thread() {
public void run() { try {
    try {
      var.set(callF(f));
    } catch (Throwable e) {
      exception.set(e);
    }
    flag.raise();
  } catch (Throwable __e) { printStackTrace2(__e); } }
};
startThread(_t_0); }
  flag.waitUntilUp();
  if (exception.has()) throw rethrow(exception.get());
  return var.get();
}
static IterableIterator emptyIterableIterator_instance = new IterableIterator() {
  public Object next() { throw fail(); }
  public boolean hasNext() { return false; }
};

static <A> IterableIterator<A> emptyIterableIterator() {
  return emptyIterableIterator_instance; 
}
static BufferedReader utf8bufferedReader(InputStream in) { try {
  return new BufferedReader(new InputStreamReader(in, "UTF-8"));
} catch (Exception __e) { throw rethrow(__e); } }

static BufferedReader utf8bufferedReader(File f) { try {
  return utf8bufferedReader(newFileInputStream(f));
} catch (Exception __e) { throw rethrow(__e); } }


static String readLineFromReaderWithClose(BufferedReader r) { try {
  String s = r.readLine();
  if (s == null) r.close();
  return s;
} catch (Exception __e) { throw rethrow(__e); } }
static <A> IterableIterator<A> iteratorFromFunction_f0(final F0<A> f) {
  class IFF2 extends IterableIterator<A> {
    A a;
    boolean done;
    
    public boolean hasNext() {
      getNext();
      return !done;
    }
    
    public A next() {
      getNext();
      if (done) throw fail();
      A _a = a;
      a = null;
      return _a;
    }
    
    void getNext() {
      if (done || a != null) return;
      a = f.get();
      done = a == null;
    }
  };
  return new IFF2();
}
static BufferedReader bufferedReader(Reader r) {
  return r instanceof BufferedReader ? (BufferedReader) r : new BufferedReader(r);
}
static FileInputStream newFileInputStream(File path) throws IOException {
  return newFileInputStream(path.getPath());
}

static FileInputStream newFileInputStream(String path) throws IOException {
  FileInputStream f = new // Line break for ancient translator
    FileInputStream(path);
  //callJavaX("registerIO", f, path, true);
  return f;
}


static abstract class VF1<A> {
  abstract void get(A a);
}static class ProgramScan {
  static int threads = isWindows() ? 500 : 10;
  static int timeout = 5000; // hmm...
  static String ip = "127.0.0.1";
  
  // This range is not used anymore anyway
  static int quickScanFrom = 10000, quickScanTo = 10999;

  static int maxNumberOfVMs_android = 4; // Android will always only have one if we don't screw up
  static int maxNumberOfVMs_nonAndroid = 50; // 100;
  static int maxNumberOfVMs;
  
  static boolean verbose;
  
  static class Program {
    int port;
    String helloString;
    
    Program(int port, String helloString) {
  this.helloString = helloString;
  this.port = port;}
  }
  
  static List<Program> scan() { try {
    return scan(1, 65535);
  } catch (Exception __e) { throw rethrow(__e); } }
  
  static List<Program> scan(int fromPort, int toPort) {
    return scan(fromPort, toPort, new int[0]);
  }
  
  static List<Program> scan(int fromPort, int toPort, int[] preferredPorts) { try {
    Set<Integer> preferredPortsSet = new HashSet<Integer>(asList(preferredPorts));
    int scanSize = toPort-fromPort+1;
    String name = toPort < 10000 ? "bot" : "program";
    int threads = isWindows() ? min(500, scanSize) : min(scanSize, 10);
    final ExecutorService es = Executors.newFixedThreadPool(threads);
    if (verbose) print(firstToUpper(name) + "-scanning " + ip + " with timeout " + timeout + " ms in " + threads + " threads.");
    startTiming();
    List < Future < Program > > futures = new ArrayList();
    List<Integer> ports = new ArrayList();
    for (int port : preferredPorts) {
      futures.add(checkPort(es, ip, port, timeout));
      ports.add(port);
    }
    for (int port = fromPort; port <= toPort; port++)
      if (!preferredPortsSet.contains(port) && !forbiddenPort(port)) {
        futures.add(checkPort(es, ip, port, timeout));
        ports.add(port);
      }
    es.shutdown();
    List<Program> programs = new ArrayList();
    long time = now();
    int i = 0;
    for (final Future<Program> f : futures) {
      if (verbose) print("Waiting for port " + get(ports, i++) + " at time " + (now()-time));
      Program p = f.get();
      if (p != null)
        programs.add(p);
    }
    //stopTiming("Port Scan " + scanSize + ", " + n(threads, "threads") + ": ", 250);
    if (verbose) print("Found " + programs.size() + " " + name + "(s) on " + ip);
    return programs;
  } catch (Exception __e) { throw rethrow(__e); } }

  static Future<Program> checkPort(final ExecutorService es, final String ip, final int port, final int timeout) {
    return es.submit(new Callable<Program>() {
        @Override public Program call() {
          try {
            Socket socket = new Socket();
            try {
              socket.setSoTimeout(timeout);
              socket.connect(new InetSocketAddress(ip, port), timeout);
              //if (verbose) print("Connected to " + ip + ":" + port);
              BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
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
  
  static List<Program> quickScan() {
    return scan(quickScanFrom, quickScanTo);
  }
  
  static List<Program> quickBotScan() {
    return quickBotScan(new int[0]);
  }
  
  static List<Program> quickBotScan(int[] preferredPorts) {
    if (maxNumberOfVMs == 0)
      maxNumberOfVMs = isAndroid() ? maxNumberOfVMs_android : maxNumberOfVMs_nonAndroid;
    return scan(4999, 5000+maxNumberOfVMs-1, preferredPorts);
  }
}  static abstract class DialogIO {
    String line;
    boolean eos, loud, noClose;
    
    abstract String readLineImpl();
    abstract boolean isStillConnected();
    abstract void sendLine(String line);
    abstract boolean isLocalConnection();
    abstract Socket getSocket();
    abstract void close();
    
    int getPort() { Socket s = getSocket(); return s == null ? 0 : s.getPort(); }
    
    boolean helloRead;
    int shortenOutputTo = 500;
    
    String readLineNoBlock() {
      String l = line;
      line = null;
      return l;
    }
    
    boolean waitForLine() { try {
      if (line != null) return true;
      //print("Readline");
      line = readLineImpl();
      //print("Readline done: " + line);
      if (line == null) eos = true;
      return line != null;
    } catch (Exception __e) { throw rethrow(__e); } }
    
    String readLine() {
      waitForLine();
      helloRead = true;
      return readLineNoBlock();
    }
    
    String ask(String s, Object... args) {
      if (loud) return askLoudly(s, args);
      if (!helloRead) readLine();
      if (args.length != 0) s = format3(s, args);
      sendLine(s);
      return readLine();
    }
    
    String askLoudly(String s, Object... args) {
      if (!helloRead) readLine();
      if (args.length != 0) s = format3(s, args);
      print("> " + shorten(s, shortenOutputTo));
      sendLine(s);
      String answer = readLine();
      print("< " + shorten(answer, shortenOutputTo));
      return answer;
    }
    
    void pushback(String l) {
      if (line != null)
        throw fail();
      line = l;
      helloRead = false;
    }
  }
  
  static abstract class DialogHandler {
    abstract void run(DialogIO io);
  }static class Matches {
  String[] m;
  
  Matches() {}
  Matches(String... m) {
  this.m = m;}
  
  String get(int i) { return i < m.length ? m[i] : null; }
  String unq(int i) { return unquote(get(i)); }
  String fsi(int i) { return formatSnippetID(unq(i)); }
  String fsi() { return fsi(0); }
  String tlc(int i) { return unq(i).toLowerCase(); }
  boolean bool(int i) { return "true".equals(unq(i)); }
  String rest() { return m[m.length-1]; } // for matchStart
  int psi(int i) { return Integer.parseInt(unq(i)); }
}
static class SingleThread {
  boolean running;
  
  void run(Object r) { go(r); }
  
  synchronized boolean go(final Object runnable) {
    if (running) return false;
    running = true;
    { Thread _t_0 = new Thread("Single Thread") {
public void run() { try {
  
      try {
        callF(runnable);
      } finally {
        _done();
      }
    } catch (Throwable __e) { printStackTrace2(__e); } }
};
startThread(_t_0); }
    return true;
  }
  
  synchronized void _done() {
    running = false;
  }
  
  boolean running() { return running; }
}static class Var<A> implements IVar<A> {
  A v; // you can access this directly if you use one thread
  
  Var() {}
  Var(A v) {
  this.v = v;}
  
  public synchronized void set(A a) {
    if (v != a) {
      v = a;
      notifyAll();
    }
  }
  
  public synchronized A get() { return v; }
  public synchronized boolean has() { return v != null; }
  public synchronized void clear() { v = null; }
  
  public String toString() { return str(get()); }
}static class BaseBase {
  String globalID = aGlobalID();
  String text;
  String textForRender() { return text; }
}

static boolean traits_multiLine = true;

static class Base extends BaseBase {
  List<String> traits = new ArrayList();
  boolean hasTrait(String t) { return containsIC(traits(), t); }
  List<String> traits() { if (nempty(text) && neq(first(traits), text)) traits.add(0, text); return traits; }
  void addTraits(List<String> l) { setAddAll(traits(), l); }
  void addTrait(String t) { if (nempty(t)) setAdd(traits(), t); }
  
  String textForRender() {
    List<String> traits = traits();
    if (traits_multiLine) return lines(traits);
    if (l(traits) <= 1) return first(traits);
    return first(traits) + " [" + join(", ", dropFirst(traits)) + "]";
  }
  
  void setText(String text) {
    this.text = text;
    traits = ll(text);
  }
}

static class CirclesAndLines {
  List<Circle> circles = new ArrayList();
  List<Line> lines = new ArrayList();
  Class<? extends Arrow> arrowClass = Arrow.class;
  Class<? extends Circle> circleClass = Circle.class;
  String title;
  String globalID = aGlobalID();
  long created = nowUnlessLoading();
  transient Lock lock = fairLock();
  transient String defaultImageID = "#1007372";
  transient double imgZoom = 1; // zoom for the circle images
  transient Pt translate;
  Circle hoverCircle; // which one we are hovering over
  transient Object onUserMadeArrow, onUserMadeCircle, onLayoutChange;
  transient Object onFullLayoutChange, onDeleteCircle, onDeleteLine;
  transient Object onRenameCircle, onRenameLine, onStructureChange;
  transient BufferedImage imageForUserMadeNodes;
  static int maxDistanceToLine = 20; // for clicking
  transient String backgroundImageID = defaultBackgroundImageID;
  static String defaultBackgroundImageID = "#1007195";
  static Color defaultLineColor = Color.white;
  static boolean debugRender;
  static Object staticPopupExtender;
  transient double scale = 1; // zoom whole image
  transient boolean recordHistory = true;
  List history;

  // auto-visualize
  Circle circle_autoVis(String text, String visualizationText, double x, double y) {
    return addAndReturn(circles,
      nu(circleClass, "x", x, "y", y, "text", text,
        "quickvis" , visualizationText,
        "img" , processImage(quickVisualizeOr(visualizationText, defaultImageID))));
  }
  
  String makeVisualizationText(String text) {
    return possibleGlobalID(text) ? "" : text;
  }

  Circle circle_autoVis(String text, double x, double y) {
    return circle_autoVis(text, makeVisualizationText(text), x, y);
  }
  
  Circle circle(BufferedImage img, double x, double y, String text) {
    return addAndReturn(circles, nu(circleClass, "x", x, "y", y, "text", text, "img" , processImage(img)));
  }
  
  Circle circle(String text, BufferedImage img, double x, double y) {
    return circle(img, x, y, text);
  }
  
  Circle circle(String text, double x, double y) {
    return addAndReturn(circles, nu(circleClass, "x", x, "y", y, "text", text, "img" , processImage(imageForUserMadeNodes())));
  }
  
  Circle addCircle(String imageID, double x, double y) {
    return addCircle(imageID, x, y, "");
  }
  
  Circle addCircle(String imageID, double x, double y, String text) {
    return addAndReturn(circles, nu(circleClass, "x", x, "y", y, "text", text, "img" , processImage(loadImage2(imageID))));
  }
  
  Arrow findArrow(Circle a, Circle b) {
    for (Line l : getWhere(lines, "a", a, "b", b))
      if (l instanceof Arrow)
        return (Arrow) l;
    return null;
  }
  
  Line addLine(Circle a, Circle b) {
    Line line = findWhere(lines, "a", a, "b", b);
    if (line == null)
      lines.add(line = nu(Line.class, "a", a, "b", b));
    return line;
  }
  
  Arrow arrow(Circle a, String text, Circle b) {
    return addArrow(a, b, text);
  }
  
  Arrow addArrow(Circle a, Circle b) {
    return addArrow(a, b, "");
  }
  
  Arrow addArrow(Circle a, Circle b, String text) {
    return addAndReturn(lines, nu(arrowClass, "a", a, "b", b, "text", text));
  }
  
  BufferedImage makeImage(int w, int h) {
    BufferedImage bg = renderTiledBackground(backgroundImageID, w, h, ptX(translate), ptY(translate));
    if (!lock.tryLock()) return null;
    try {
      if (scale != 1)
        createGraphics_modulate(bg, new VF1<Graphics2D>() { void get(Graphics2D g) { try { 
          g.scale(scale, scale);
         } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "g.scale(scale, scale);"; }});
      
      // Lines
      
      if (debugRender)
        print("Have " + n(lines, "line"));
      
      HashMap<Pair<Circle, Circle>, Line> hasLine = new HashMap();
      HashMap<Line,Boolean> flipMap = new HashMap();
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
          drawThoughtLineText_multiLine(bg, l.a.img(this), iround(a.x), iround(a.y), l.b.img(this), iround(b.x), iround(b.y), text, Color.white /*l.color*/);
        }
      }
      
      // Circles
      
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
  
  Canvas showAsFrame(int w, int h) {
    Canvas canvas = showAsFrame();
    frameInnerSize(canvas, w, h);
    centerFrame(getFrame(canvas));
    return canvas;
  }
  
  Canvas showAsFrame() {
    return (Canvas) swing(new F0<Object>() { Object get() { try { 
      Canvas canvas = makeCanvas();
      showCenterFrame(canvas);
      return canvas;
     } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "Canvas canvas = makeCanvas();\r\n      showCenterFrame(canvas);\r\n      ret canvas;"; }});
  }
  
  Canvas makeCanvas() {
    final Object makeImg = new Object() { Object get(int w, int h) { try { return  makeImage(w, h) ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "makeImage(w, h)"; }};
    final Canvas canvas = jcanvas(makeImg);
    disableImageSurfaceSelector(canvas);
    new CircleDragger(this, canvas, new Runnable() { public void run() { try {  updateCanvas(canvas, makeImg) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "updateCanvas(canvas, makeImg)"; }});
    
    componentPopupMenu(canvas, new VF1<JPopupMenu>() { void get(JPopupMenu menu) { try { 
      // POPUP MENU START
      Pt p = pointFromEvent(canvas, componentPopupMenu_mouseEvent.get());
      JMenu imageMenu = jmenu("Image");
      moveAllMenuItems(menu, imageMenu);

      addMenuItem(menu, "New Circle...", new Runnable() { public void run() { try {  newCircle(canvas) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "newCircle(canvas)"; }});
      
      final Line l = findLine(canvas, p);
      if (l != null) {
        addMenuItem(menu, "Rename Relation...", new Runnable() { public void run() { try { 
          renameLine(canvas, l)
        ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "renameLine(canvas, l)"; }});
        
        addMenuItem(menu, "Delete Relation", new Runnable() { public void run() { try { 
          deleteLine(l);
          canvas.update();
        
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "deleteLine(l);\r\n          canvas.update();"; }});
      }
      
      final Circle c = findCircle(canvas, p);
      if (c != null) {
        addMenuItem(menu, "Rename Circle...", new Runnable() { public void run() { try {  renameCircle(canvas, c) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "renameCircle(canvas, c)"; }});
        addMenuItem(menu, "Delete Circle", new Runnable() { public void run() { try { 
          deleteCircle(c);
          canvas.update();
        
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "deleteCircle(c);\r\n          canvas.update();"; }});
        
        if (c.img != null || c.quickvis != null)
          addMenuItem(menu, "Delete Image", new Runnable() { public void run() { try { 
            c.img = null;
            c.quickvis = null;
            canvas.update();
          
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "c.img = null;\r\n            c.quickvis = null;\r\n            canvas.update();"; }});
          
        if (neqic(c.text, c.quickvis))
          addMenuItem(menu, "Visualize", new Runnable() { public void run() { try { 
            { Thread _t_0 = new Thread("Visualizing") {
public void run() { try {
  
              quickVisualize(c.text);
              print("Quickvis done");
              { swing(new Runnable() { public void run() { try { 
                c.img = null;
                c.quickvis = c.text;
                canvas.update();
                pcallF(onRenameCircle, c); schange();
              
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "c.img = null;\r\n                c.quickvis = c.text;\r\n                canvas.u..."; }}); }
            } catch (Throwable __e) { printStackTrace2(__e); } }
};
startThread(_t_0); }
          
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "thread \"Visualizing\" {\r\n              quickVisualize(c.text);\r\n              ..."; }});
      }
      
      addMenuItem(menu, "Copy structure to clipboard", new Runnable() { public void run() { try { 
        copyTextToClipboard(cal_simplifiedStructure(CirclesAndLines.this))
      ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "copyTextToClipboard(cal_simplifiedStructure(CirclesAndLines.this))"; }});

      addMenuItem(menu, "Paste structure", new Runnable() { public void run() { try { 
        String text = getTextFromClipboard();
        if (nempty(text)) {
          copyCAL(cal_unstructure(text), CirclesAndLines.this);
          canvas.update();
          schange();
        }
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "String text = getTextFromClipboard();\r\n        if (nempty(text)) {\r\n         ..."; }});
      
      pcallF(staticPopupExtender, CirclesAndLines.this, canvas, menu);
      
      addMenuItem(menu, imageMenu);

      // POPUP MENU END
     } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "// POPUP MENU START\r\n      Pt p = pointFromEvent(canvas, componentPopupMenu_m..."; }});
    
    return canvas;
  }
  
  void newCircle(final Canvas canvas) {
    final JTextField text = jtextfield();
    showFormTitled("New Circle", "Text", text, runnableThread(new Runnable() { public void run() { try {  { JWindow _loading_window = showLoadingAnimation(); try {
      String theText = getTextTrim(text);
      makeCircle(theText);
      canvas.update();
    } finally { disposeWindow(_loading_window); }}
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "{ JWindow _loading_window = showLoadingAnimation(); try {\r\n      String theTe..."; }}));
  }
  
  Canvas show() { return showAsFrame(); }
  Canvas show(int w, int h) { return showAsFrame(w, h); }
  
  Circle findCircle(String text) {
    for (Circle c : circles) if (eq(c.text, text)) return c;
    for (Circle c : circles) if (eqic(c.text, text)) return c;
    return null;
  }
  
  void renameCircle(final Canvas canvas, final Circle c) {
    final JTextField tf = jtextfield(c.text);
    showFormTitled("Rename circle",
      "Old name", jlabel(c.text),
      "New name", tf,
      new Runnable() { public void run() { try { 
        c.setText(getTextTrim(tf));
        canvas.update();
        pcallF(onRenameCircle, c); schange();
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "c.setText(getTextTrim(tf));\r\n        canvas.update();\r\n        pcallF(onRenam..."; }});
  }
  
  void renameLine(final Canvas canvas, final Line l) {
    final JTextField tf = jtextfield(l.text);
    showFormTitled("Rename relation",
      "Old name", jlabel(l.text),
      "New name", tf,
      new Runnable() { public void run() { try { 
        l.setText(getTextTrim(tf));
        canvas.update();
        pcallF(onRenameLine, l); schange();
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "l.setText(getTextTrim(tf));\r\n        canvas.update();\r\n        pcallF(onRenam..."; }});
  }
  
  void clear() {
    clearAll(circles, lines);
  }
  
  // only finds actually containing circles
  Circle findCircle(ImageSurface canvas, Pt p) {
    p = untranslatePt(translate, p);
    Lowest<Circle> best = new Lowest();
    for (Circle c : circles)
      if (c.contains(this, canvas, p))
        best.put(c, pointDistance(p, c.pt2(this, canvas)));
    return best.get();
  }
  
  Circle findNearestCircle(ImageSurface canvas, Pt p) {
    Lowest<Circle> best = new Lowest();
    for (Circle c : circles)
      if (c.contains(this, canvas, p))
        best.put(c, pointDistance(p, c.pt2(this, canvas)));
    return best.get();
  }
  
  BufferedImage processImage(BufferedImage img) {
    return scaleImage(img, imgZoom);
  }
  
  void deleteCircle(Circle c) {
    for (Line l : cloneList(lines))
      if (l.a == c || l.b == c) deleteLine(l);
    circles.remove(c);
    pcallF(onDeleteCircle, c); schange();
  }
  
  void deleteLine(Line l) {
    lines.remove(l);
    pcallF(onDeleteLine, l); schange();
  }
  
  void openPlusDialog(final Circle c, final ImageSurface canvas) {
    if (c == null) return;
    
    final JTextField tfFrom = jtextfield(c.text);
    final JTextField tfRel = jtextfield(web_defaultRelationName());
    final JComboBox tfTo = autoComboBox(collect(circles, "text"));
    
    showFormTitled("Add connection",
      "From node", tfFrom,
      "Connection name", tfRel,
      "To node", tfTo,
      new F0<Object>() { Object get() { try { 
        String sA = getTextTrim(tfFrom);
        Circle a = eq(sA, c.text) ? c : findOrMakeCircle(sA);
        if (a == null) { messageBox("Not found: " + getTextTrim(tfFrom)); return false; }
        Circle b = findOrMakeCircle(getTextTrim(tfTo));
        if (b == null) { messageBox("Not found: " + getTextTrim(tfTo)); return false; }
        if (a == b) { infoBox("Can't connect circle to itself for now"); return false; }
        Arrow arrow = arrow(a, getTextTrim(tfRel), b);
        ((Canvas) canvas).update();
        pcallF(onUserMadeArrow, arrow); schange();
        return null;
       } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "S sA = getTextTrim(tfFrom);\r\n        Circle a = eq(sA, c.text) ? c : findOrMa..."; }});
    awtLater(tfRel, 100, new Runnable() { public void run() { try {  requestFocus(tfRel) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "requestFocus(tfRel)"; }});
  }
  
  void schange() {
    pcallF(onStructureChange);
    logQuoted("user-web-edits", now() + " " + cal_structure(this));
    markWebsPosted();
  }
  
  Circle findOrMakeCircle(String text) {
    Circle c = findCircle(text);
    if (c != null) return c;
    return makeCircle(text);
  }

  Circle makeCircle(String text) {
    Circle c = circle(imageForUserMadeNodes(), random(0.1, 0.9), random(0.1, 0.9), text);
    pcallF(onUserMadeCircle, c); schange();
    historyLog(lisp("Made circle", text));
    return c;
  }

  BufferedImage imageForUserMadeNodes() {  
    if (imageForUserMadeNodes == null)
      imageForUserMadeNodes = whiteImage(20, 20);
    return imageForUserMadeNodes;
  }
  
  Line findLine(Canvas is, Pt p) {
    p = untranslatePt(translate, p);
    Lowest<Line> best = new Lowest();
    for (Line line : lines) {
      double d = distancePointToLineSegment(line.a.pt(this, is), line.b.pt(this, is), p);
      if (d <= maxDistanceToLine)
        best.put(line, d);
    }
    return best.get();
  }
  
  Pt pointFromEvent(ImageSurface canvas, MouseEvent e) {
    return scalePt(canvas.pointFromEvent(e), 1/scale);
  }
  
  void historyLog(Object o) {
    if (!recordHistory) return;
    if (history == null) history = new ArrayList();
    history.add(o);
  }
} // end of class CirclesAndLines

static class Circle extends Base {
  transient BufferedImage img;
  double x, y;
  //static BufferedImage defaultImage;
  String quickvis;
  
  BufferedImage img(CirclesAndLines cal) {
    if (img != null) return img;
    if (nempty(quickvis)) img = quickVisualize(quickvis);
    //if (defaultImage == null) defaultImage = loadImage2(#1007372);
    return cal.imageForUserMadeNodes();
  }
  
  Pt pt2(CirclesAndLines cal, ImageSurface is) {
    return pt2(is.getWidth(), is.getHeight(), cal);
  }
  
  Pt pt(CirclesAndLines cal, ImageSurface is) {
    return pt(is.getWidth(), is.getHeight(), cal);
  }
  
  Pt pt(int w, int h, CirclesAndLines cal) {
    return new Pt(iround(x*w/cal.scale), iround(y*h/cal.scale));
  }
  
  Pt pt2(int w, int h, CirclesAndLines cal) {
    return new Pt(iround(x*w), iround(y*h));
  }
  
  DoublePt doublePt(int w, int h, CirclesAndLines cal) {
    return new DoublePt(x*w/cal.scale, y*h/cal.scale);
  }
  
  boolean contains(CirclesAndLines cal, ImageSurface is, Pt p) {
    return pointDistance(p, pt2(cal, is)) <= iround(thoughtCircleSize(img(cal))*cal.scale)/2+1;
  }
}

static class Line extends Base {
  Circle a, b;
  transient Color color = CirclesAndLines.defaultLineColor;
  
  Line setColor(Color color) {
    this.color = color;
    return this;
  }
}

static class Arrow extends Line {}

static class CircleDragger extends MouseAdapter {
  CirclesAndLines cal;
  ImageSurface is;
  Object update;
  int dx, dy;
  Circle circle;
  Pt startPoint;
  
  CircleDragger(CirclesAndLines cal, ImageSurface is, Object update) {
  this.update = update;
  this.is = is;
  this.cal = cal;
    if (containsInstance(is.tools, CircleDragger.class)) return;
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
        dx = p.x-iround(circle.x*is.getWidth());
        dy = p.y-iround(circle.y*is.getHeight());
        //printVars("mousePressed", +dx, +dy, +p, cx := circle.x, cy := circle.y, w := is.getWidth(), h := is.getHeight());
      } else { 
        Pt t = unnull(cal.translate);
        dx = p.x-t.x;
        dy = p.y-t.y;
      }
    }
  }

  public void mouseDragged(MouseEvent e) {
    if (startPoint == null) return;
    Pt p = is.pointFromEvent(e);
    if (circle != null) {
      circle.x = (p.x-dx)/(double) is.getWidth();
      circle.y = (p.y-dy)/(double) is.getHeight();
      //printVars("mouseDragged", +dx, +dy, +p, cx := circle.x, cy := circle.y, w := is.getWidth(), h := is.getHeight());
      pcallF(cal.onLayoutChange, circle);
      callF(update);
    } else {
      cal.translate = new Pt(p.x-dx, p.y-dy);
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
}static abstract class TokCondition {
  abstract boolean get(List<String> tok, int i); // i = N Index
}static abstract class F0<A> {
  abstract A get();
}static abstract class F1<A, B> {
  abstract B get(A a);
}// you still need to implement hasNext() and next()
static abstract class IterableIterator<A> implements Iterator<A>, Iterable<A> {
  public Iterator<A> iterator() {
    return this;
  }
  
  public void remove() {
    unsupportedOperation();
  }
}/** this class is fully thread-safe */
static class Flag {
  private boolean up;

  /** returns true if flag was down before */
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

  // currently does a semi-active wait with latency = 50 ms
  public void waitForThisOr(Flag otherFlag) { try {
    while (!isUp() && !otherFlag.isUp())
      Thread.sleep(50);
  } catch (Exception __e) { throw rethrow(__e); } }
}static class BetterLabel extends JLabel {
  boolean autoToolTip = true;
  
  BetterLabel() {
    componentPopupMenu(this, new VF1<JPopupMenu>() { void get(JPopupMenu menu) { try { 
      addMenuItem(menu, "Copy text to clipboard", new Runnable() { public void run() { try { 
        copyTextToClipboard(getText());
      
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "copyTextToClipboard(getText());"; }});
     } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "addMenuItem(menu, \"Copy text to clipboard\", r {\r\n        copyTextToClipboard(..."; }});
  }
  
  BetterLabel(String text) {
    this();
    setText(text);
  }
  
  public void setText(String text) {
    super.setText(text);
    if (autoToolTip) setToolTipText(text);
  }
}



static class TransferableImage implements Transferable {
  Image i;

  TransferableImage(Image i) {
  this.i = i;}

  public Object getTransferData( DataFlavor flavor )
  throws UnsupportedFlavorException, IOException {
      if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
          return i;
      }
      else {
          throw new UnsupportedFlavorException( flavor );
      }
  }

  public DataFlavor[] getTransferDataFlavors() {
      DataFlavor[] flavors = new DataFlavor[ 1 ];
      flavors[ 0 ] = DataFlavor.imageFlavor;
      return flavors;
  }

  public boolean isDataFlavorSupported( DataFlavor flavor ) {
      DataFlavor[] flavors = getTransferDataFlavors();
      for ( int i = 0; i < flavors.length; i++ ) {
          if ( flavor.equals( flavors[ i ] ) ) {
              return true;
          }
      }

      return false;
  }
}static interface Producer<A> {
  public A next();
}static ThreadLocal<Boolean> DynamicObject_loading = new ThreadLocal();

static class DynamicObject {
  String className; // just the name, without the "main$"
  LinkedHashMap<String,Object> fieldValues = new LinkedHashMap();
  
  DynamicObject() {}
  // className = just the name, without the "main$"
  DynamicObject(String className) {
  this.className = className;}
}static class RGB {
  public float r, g, b; // can't be final cause persistence
  
  RGB() {}
  
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
    this.r = color.getRed()/255f;
    this.g = color.getGreen()/255f;
    this.b = color.getBlue()/255f;
  }

  public RGB(String hex) {
    r = Integer.parseInt(hex.substring(0, 2), 16)/255f;
    g = Integer.parseInt(hex.substring(2, 4), 16)/255f;
    b = Integer.parseInt(hex.substring(4, 6), 16)/255f;
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

  int asInt() { return getColor().getRGB() & 0xFFFFFF; }
  int getInt() { return getColor().getRGB() & 0xFFFFFF; }

  public float getBrightness() {
    return (r+g+b)/3.0f;
  }

  public String getHexString() {
    return Integer.toHexString(asInt() | 0xFF000000).substring(2).toUpperCase();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RGB)) return false;

    RGB rgb = (RGB) o;

    if (Float.compare(rgb.b, b) != 0) return false;
    if (Float.compare(rgb.g, g) != 0) return false;
    if (Float.compare(rgb.r, r) != 0) return false;

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

static class RGBImage {
  transient BufferedImage bufferedImage;
  File file;
  int width, height;
  int[] pixels;

  RGBImage() {}

  RGBImage(BufferedImage image) {
    this(image, null);
  }

  RGBImage(BufferedImage image, File file) {
    this.file = file;
    bufferedImage = image;
    width = image.getWidth();
    height = image.getHeight();
    pixels = new int[width*height];
    PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
    try {
      if (!pixelGrabber.grabPixels())
        throw new RuntimeException("Could not grab pixels");
      cleanPixels(); // set upper byte to 0
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /** We assume it's a file name to load from */
  RGBImage(String file) throws IOException {
    this(new File(file));
  }

  RGBImage(Dimension size, Color color) {
    this(size.width, size.height, color);
  }

  RGBImage(Dimension size, RGB color) {
    this(size.width, size.height, color);
  }

  private void cleanPixels() {
    for (int i = 0; i < pixels.length; i++)
      pixels[i] &= 0xFFFFFF;
  }

  RGBImage(int width, int height, int[] pixels) {
    this.width = width;
    this.height = height;
    this.pixels = pixels;
  }

  RGBImage(int w, int h, RGB[] pixels) {
    this.width = w;
    this.height = h;
    this.pixels = asInts(pixels);
  }

  public static int[] asInts(RGB[] pixels) {
    int[] ints = new int[pixels.length];
    for (int i = 0; i < pixels.length; i++)
      ints[i] = pixels[i] == null ? 0 : pixels[i].getColor().getRGB();
    return ints;
  }

  public RGBImage(int w, int h) {
    this(w, h, Color.black);
  }
  
  RGBImage(int w, int h, RGB rgb) {
    this.width = w;
    this.height = h;
    this.pixels = new int[w*h];
    int col = rgb.asInt();
    if (col != 0)
      for (int i = 0; i < pixels.length; i++)
        pixels[i] = col;
  }

  RGBImage(RGBImage image) {
    this(image.width, image.height, copyPixels(image.pixels));
  }

  RGBImage(int width, int height, Color color) {
    this(width, height, new RGB(color));
  }

  RGBImage(File file) throws IOException {
    this(javax.imageio.ImageIO.read(file));
  }

  private static int[] copyPixels(int[] pixels) {
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

  /** alias of getRGB - I kept typing getPixel instead of getRGB all the time, so I finally created it */
  RGB getPixel(int x, int y) {
    return getRGB(x, y);
  }
  
  RGB getPixel(Pt p) { return getPixel(p.x, p.y); }

  int getWidth() { return width; }
  int getHeight() { return height; }
  int w() { return width; }
  int h() { return height; }

  /** Attention: cached, i.e. does not change when image itself changes */
  /** @NotNull */
  public BufferedImage getBufferedImage() {
    if (bufferedImage == null) {
      bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      //bufferedImage.setData(Raster.createRaster(new SampleModel()));
      for (int y = 0; y < height; y++)
        for (int x = 0; x < width; x++)
          bufferedImage.setRGB(x, y, pixels[y*width+x]);
    }
    return bufferedImage;
  }

  RGBImage clip(Rect r) {
    return r == null ? null : clip(r.getRectangle());
  }
  
  RGBImage clip(Rectangle r) {
    r = fixClipRect(r);
    int[] newPixels;
    try {
      newPixels = new int[r.width*r.height];
    } catch (RuntimeException e) {
      System.out.println(r);
      throw e;
    }
    for (int y = 0; y < r.height; y++) {
      System.arraycopy(pixels, (y+r.y)*width+r.x, newPixels, y*r.width, r.width);
    }
    return new RGBImage(r.width, r.height, newPixels);
  }

  private Rectangle fixClipRect(Rectangle r) {
    r = r.intersection(new Rectangle(0, 0, width, height));
    if (r.isEmpty())
      r = new Rectangle(r.x, r.y, 0, 0);
    return r;
  }

  public File getFile() {
    return file;
  }

  /** can now also do GIF (not just JPEG) */
  public static RGBImage load(String fileName) {
    return load(new File(fileName));
  }

  /** can now also do GIF (not just JPEG) */
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
    if (name.endsWith(".png")) type = "png";
    else if (name.endsWith(".jpg") || name.endsWith(".jpeg")) type = "jpeg";
    else throw new IOException("Unknown image extension: " + name);
    javax.imageio.ImageIO.write(getBufferedImage(), type, file);
  }

  public static RGBImage dummyImage() {
    return new RGBImage(1, 1, new int[] {0xFFFFFF});
  }

  public int[] getPixels() {
    return pixels;
  }

  public void setPixel(int x, int y, RGB rgb) {
    if (x >= 0 && y >= 0 && x < width && y < height)
      pixels[y*width+x] = rgb.asInt();
  }

  public void setPixel(int x, int y, Color color) {
    setPixel(x, y, new RGB(color));
  }

  public void setPixel(int x, int y, int rgb) {
    if (x >= 0 && y >= 0 && x < width && y < height)
      pixels[y*width+x] = rgb;
  }
  
  void setPixel(Pt p, RGB rgb) { setPixel(p.x, p.y, rgb); }
  void setPixel(Pt p, Color color) { setPixel(p.x, p.y, color); }

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
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RGBImage rgbImage = (RGBImage) o;

    if (height != rgbImage.height) return false;
    if (width != rgbImage.width) return false;
    if (!Arrays.equals(pixels, rgbImage.pixels)) return false;

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
    return width*height;
  }
}

static class Pair<A, B> implements Comparable<Pair<A, B>> {
  A a;
  B b;

  Pair() {}
  Pair(A a, B b) {
  this.b = b;
  this.a = a;}
  
  public int hashCode() {
    return hashCodeFor(a) + 2*hashCodeFor(b);
  }
  
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Pair)) return false;
    Pair t = (Pair) o;
    return eq(a, t.a) && eq(b, t.b);
  }
  
  public String toString() {
    return "<" + a + ", " + b + ">";
  }
  
  public int compareTo(Pair<A, B> p) {
    if (p == null) return 1;
    int i = ((Comparable<A>) a).compareTo(p.a);
    if (i != 0) return i;
    return ((Comparable<B>) b).compareTo(p.b);
  }
}

static class Rect {
  int x, y, w, h;
  
  Rect() {}
  Rect(Rectangle r) {
    x = r.x;
    y = r.y;
    w = r.width;
    h = r.height;
  }
  Rect(int x, int y, int w, int h) {
  this.h = h;
  this.w = w;
  this.y = y;
  this.x = x;}
  
  Rectangle getRectangle() {
    return new Rectangle(x, y, w, h);
  }
  
  public boolean equals(Object o) { return stdEq2(this, o); }
public int hashCode() { return stdHash2(this); }
  
  public String toString() {
    return x + "," + y + " / " + w + "," + h;
  }
  
  int x2() { return x + w; }
  int y2() { return y + h; }
  
  boolean contains(Pt p) {
    return contains(p.x, p.y);
  }
  
  boolean contains(int _x, int _y) {
    return _x >= x && _y >= y && _x < x+w && _y < y+h;
  }
  
  boolean empty() { return w <= 0 || h <= 0; }
}static class Pt {
  int x, y;
  
  Pt() {}
  Pt(Point p) {
    x = p.x;
    y = p.y;
  }
  Pt(int x, int y) {
  this.y = y;
  this.x = x;}
  
  Point getPoint() {
    return new Point(x, y);
  }
  
  public boolean equals(Object o) { return stdEq2(this, o); }
public int hashCode() { return stdHash2(this); }
  
  public String toString() {
    return x + ", " + y;
  }
}static class Lowest<A> {
  A best;
  double score;
  transient Object onChange;
  
  boolean isNewBest(double score) {
    return best == null || score < this.score;
  }
  
  double bestScore() {
    return best == null ? Double.NaN : score;
  }
  
  float floatScore() {
    return best == null ? Float.NaN : (float) score;
  }
  
  float floatScoreOr(float defaultValue) {
    return best == null ? defaultValue : (float) score;
  }
  
  boolean put(A a, double score) {
    if (a != null && isNewBest(score)) {
      best = a;
      this.score = score;
      pcallF(onChange);
      return true;
    }
    return false;
  }
  
  A get() { return best; }
  boolean has() { return best != null; }
}static class DoublePt {
  double x, y;
  
  DoublePt() {}
  DoublePt(Point p) {
    x = p.x;
    y = p.y;
  }
  DoublePt(double x, double y) {
  this.y = y;
  this.x = x;}
  
  public boolean equals(Object o) { return stdEq2(this, o); }
public int hashCode() { return stdHash2(this); }
  
  public String toString() {
    return x + ", " + y;
  }
}static interface IVar<A> {
  void set(A a);
  A get();
  boolean has();
  void clear();
}static class ImageSurface extends Surface {
  private BufferedImage image;
  private double zoomX = 1, zoomY = 1;
  private Rectangle selection;
  List tools = new ArrayList();
  Object overlay; // voidfunc(Graphics2D)
  Runnable onSelectionChange;
  static boolean verbose;
  boolean noMinimumSize = true;
  String titleForUpload;
  Object onZoom;
  boolean specialPurposed; // true = don't show image changing commands in popup menu
  boolean zoomable = true;

  public ImageSurface() {
    this(dummyImage());
  }
  
  static BufferedImage dummyImage() {
    return new RGBImage(1, 1, new int[] { 0xFFFFFF }).getBufferedImage();
  }

  public ImageSurface(RGBImage image) {
    this(image != null ? image.getBufferedImage() : dummyImage());
  }

  public ImageSurface(BufferedImage image) {
    setImage(image);
    clearSurface = false;

    componentPopupMenu(this, new VF1<JPopupMenu>() { void get(JPopupMenu menu) { try { 
      Point p = pointFromEvent(componentPopupMenu_mouseEvent.get()).getPoint();
      fillPopupMenu(menu, p);
     } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "Point p = pointFromEvent(componentPopupMenu_mouseEvent.get()).getPoint();\r\n  ..."; }});
    
    new ImageSurfaceSelector(this);
    
    jHandleFileDrop(this, new VF1<File>() { void get(File f) { try {  setImage(loadBufferedImage(f)) ; } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "setImage(loadBufferedImage(f))"; }});
  }

  public ImageSurface(RGBImage image, double zoom) {
    this(image);
    setZoom(zoom);
  }

  // point is already in image coordinates
  protected void fillPopupMenu(JPopupMenu menu, final Point point) {
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
          zoomIn(2.0);
          centerPoint(point);
        }
      });
      menu.add(miZoomIn);
  
      JMenuItem miZoomOut = new JMenuItem("Zoom out");
      miZoomOut.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          zoomOut(2.0);
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
      addMenuItem(menu, "Show full screen", new Runnable() { public void run() { try {  showFullScreen() ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "showFullScreen()"; }});
      
      addMenuItem(menu, "Point: " + point.x + "," + point.y + " (image: " + image.getWidth() + "*" + image.getHeight() + ")", null);
  
      menu.addSeparator();
    }

    addMenuItem(menu, "Save image...", new Runnable() { public void run() { try {  saveImage() ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "saveImage()"; }});
    addMenuItem(menu, "Upload image...", new Runnable() { public void run() { try {  uploadTheImage() ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "uploadTheImage()"; }});
    addMenuItem(menu, "Copy image to clipboard", new Runnable() { public void run() { try {  copyImageToClipboard(getImage()) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "copyImageToClipboard(getImage())"; }});
    if (!specialPurposed)
      addMenuItem(menu, "Paste image from clipboard", new Runnable() { public void run() { try {  loadFromClipboard() ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "loadFromClipboard()"; }});
    if (selection != null)
      addMenuItem(menu, "Crop", new Runnable() { public void run() { try {  crop() ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "crop()"; }});
    if (!specialPurposed)
      addMenuItem(menu, "No image", new Runnable() { public void run() { try {  noImage() ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "noImage()"; }});
  }
  
  void noImage() { setImage((BufferedImage) null); }
  
  void crop() {
    if (selection == null) return;
    BufferedImage img = clipBufferedImage(getImage(), selection);
    selection = null;
    setImage(img);
  }
  
  void loadFromClipboard() {
    BufferedImage img = getImageFromClipboard();
    if (img != null)
      setImage(img);
  }

  void saveImage() {
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
    if (verbose) main.print("render");
    g.setColor(Color.white);
    if (image == null)
      g.fillRect(0, 0, w, h);
    else {
      int iw = getZoomedWidth(), ih = getZoomedHeight();
      boolean alpha = hasTransparency(image);
      if (alpha) g.fillRect(0, 0, w, h);
      g.drawImage(image, 0, 0, iw, ih, null);
      if (!alpha) {
        g.fillRect(iw, 0, w-iw, h);
        g.fillRect(0, ih, iw, h-ih);
      }
    }

    if (verbose) main.print("render overlay");
    if (overlay != null) pcallF(overlay, g);

    if (verbose) main.print("render selection");
    if (selection != null) {
      // drawRect is inclusive, selection is exclusive, so... whatever, tests show it's cool.
      drawSelectionRect(g, selection, Color.green, Color.white);
    }
  }

  public void drawSelectionRect(Graphics2D g, Rectangle selection, Color green, Color white) {
    g.setColor(green);
    int top = (int) (selection.y * zoomY);
    int bottom = (int) ((selection.y+selection.height) * zoomY);
    int left = (int) (selection.x * zoomX);
    int right = (int) ((selection.x+selection.width) * zoomX);
    g.drawRect(left-1, top-1, right-left+1, bottom-top+1);
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
    centerPoint(new Point(getImage().getWidth()/2, getImage().getHeight()/2));

    pcallF(onZoom);
  }

  public Dimension getMinimumSize() {
    if (noMinimumSize) return new Dimension(1, 1);
    int w = getZoomedWidth();
    int h = getZoomedHeight();
    Dimension min = super.getMinimumSize();
    return new Dimension(Math.max(w, min.width), Math.max(h, min.height));
  }

  private int getZoomedHeight() {
    return (int) (image.getHeight() * zoomY);
  }

  private int getZoomedWidth() {
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

  /** returns a scrollpane with the scroll-mode prevent-garbage-drawing fix applied */
  public JScrollPane makeScrollPane() {
    JScrollPane scrollPane = new JScrollPane(this);
    scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
    return scrollPane;
  }

  public void zoomToWindow() { zoomToDisplaySize(); }
  public void zoomToDisplaySize() {
    if (image == null) return;
    Dimension display = getDisplaySize();
    double xRatio = display.width/(double) image.getWidth();
    double yRatio = display.height/(double) image.getHeight();
    setZoom(Math.min(xRatio, yRatio));
    revalidate();
  }

  /** tricky magic to get parent scroll pane */
  private Dimension getDisplaySize() {
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
  
  // p is in image coordinates
  void centerPoint(Point p) {
    JScrollPane sp = enclosingScrollPane(this);
    if (sp == null) return;
      
    p = new Point((int) (p.x*getZoomX()), (int) (p.y*getZoomY()));
    final JViewport viewport = sp.getViewport();
    Dimension viewSize = viewport.getExtentSize();
    
    //_print("centerPoint " + p);
    int x = max(0, p.x-viewSize.width/2);
    int y = max(0, p.y-viewSize.height/2);
    
    //_print("centerPoint " + p + " => " + x + "/" + y);
    p = new Point(x,y);
    //_print("centerPoint " + p);
    final Point _p = p;
    awtLater(new Runnable() { public void run() { try { 
      viewport.setViewPosition(_p);
    
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "viewport.setViewPosition(_p);"; }});
  }
  
  Pt pointFromEvent(MouseEvent e) {
    return pointFromComponentCoordinates(new Pt(e.getX(), e.getY()));
  }
  
  Pt pointFromComponentCoordinates(Pt p) {
    return new Pt((int) (p.x/zoomX), (int) (p.y/zoomY));
  }
  
  void uploadTheImage() {
    call(hotwire("#1007313"), "go", getImage(), titleForUpload);
  }
  
  void showFullScreen() {
    showFullScreenImageSurface(getImage());
  }
  
  void zoomIn(double f) { setZoom(getZoomX()*f, getZoomY()*f); }
  void zoomOut(double f) { setZoom(getZoomX()/f, getZoomY()/f); }
}

static class ImageSurfaceSelector extends MouseAdapter {
  ImageSurface is;
  Point startingPoint;
  boolean enabled = true;
  static boolean verbose = false;

  ImageSurfaceSelector(ImageSurface is) {
  this.is = is;
    if (containsInstance(is.tools, ImageSurfaceSelector.class)) return;
    is.tools.add(this);
    is.addMouseListener(this);
    is.addMouseMotionListener(this);
  }

  public void mousePressed(MouseEvent evt) {
    if (verbose) print("mousePressed");
    if (evt.getButton() != MouseEvent.BUTTON1) return;
    if (enabled)
      startingPoint = getPoint(evt);
  }

  public void mouseDragged(MouseEvent e) {
    if (verbose) print("mouseDragged");
    if (startingPoint != null) {
      Point endPoint = getPoint(e);
      Rectangle r = new Rectangle(startingPoint, new Dimension(endPoint.x-startingPoint.x+1, endPoint.y-startingPoint.y+1));
      normalize(r);
      r.width = min(r.width, is.getImage().getWidth()-r.x);
      r.height = min(r.height, is.getImage().getHeight()-r.y);
      is.setSelection(r);
    }
    if (verbose) print("mouseDragged done");
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
    if (verbose) print("mouseReleased");
    mouseDragged(e);
    if (getPoint(e).equals(startingPoint))
      is.setSelection(null);
    startingPoint = null;
  }
  
  Point getPoint(MouseEvent e) {
    return new Point((int) (e.getX()/is.getZoomX()), (int) (e.getY()/is.getZoomY()));
  }
}abstract static class Surface extends JPanel {
  public boolean clearSurface = true;
  private boolean clearOnce;

  Surface() {
    setDoubleBuffered(false);
  }

  Graphics2D createGraphics2D(int width, int height, Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setBackground(getBackground());
    if (clearSurface || clearOnce) {
      g2.clearRect(0, 0, width, height);
      clearOnce = false;
    }
    return g2;
  }

  public abstract void render(int w, int h, Graphics2D g);

  public void paintImmediately(int x,int y,int w, int h) {
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



// A concept should be an object, not just a string.

// Functions that should always be there for child processes:
static int concepts_internStringsLongerThan = 10;

static ThreadLocal<Boolean> concepts_unlisted = new ThreadLocal();

static interface Derefable {
  Concept get();
}

static interface IConceptIndex {
  void update(Concept c); // also for adding
  void remove(Concept c);
}

static interface IFieldIndex<A extends Concept, Val> {
  List<A> getAll(Val val);
}

static class Concepts {
  Map<Long, Concept> concepts = synchroTreeMap();
  HashMap<Class,Object> perClassData = new HashMap();
  String programID; // set to "-" for non-persistent (possibly not implemented)
  long idCounter;
  volatile long changes, changesWritten;
  volatile java.util.Timer autoSaver;
  volatile boolean savingConcepts, dontSave;
  boolean initialSave = true; // set to false to avoid initial useless saving
  int autoSaveInterval = -1000; // 1 second + wait logic
  boolean useGZIP = true, quietSave;
  ReentrantLock lock = new ReentrantLock(true);
  ReentrantLock saverLock = new ReentrantLock(true);
  long lastSaveTook, lastSaveWas;
  float maxAutoSavePercentage = 10;
  List<IConceptIndex> conceptIndices;
  Map<Class<? extends Concept>, Map<String, IFieldIndex>> fieldIndices;
  List saveActions = synchroList();
  
  Concepts() {}
  Concepts(String programID) {
  this.programID = programID;}
  
  synchronized long internalID() {
    do {
      ++idCounter;
    } while (hasConcept(idCounter));
    return idCounter;
  }
  
  void initProgramID() {
    if (programID == null)
      programID = getDBProgramID();
  }
  
  // Now tries to load from bot first, then go to disk.
  Concepts load() {
    return load(false);
  }
  
  Concepts safeLoad() {
    return load(true);
  }
  
  Concepts load(boolean allDynamic) {
    initProgramID();
    if (tryToGrab(allDynamic)) return this;
    return loadFromDisk(allDynamic);
  }
  
  Concepts loadFromDisk() { return loadFromDisk(false); }
  
  Concepts loadFromDisk(boolean allDynamic) {
    if (nempty(concepts)) clearConcepts();
    //DynamicObject_loading.set(true); // now done in unstructure()
    //try {
      long time = now();
      Map<Long, Concept> _concepts = concepts; // empty map
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
    /*} finally {
      DynamicObject_loading.set(null);
    }*/
    if (initialSave) allChanged();
    return this;
  }
  
  Concepts loadConcepts() { return load(); }
  
  boolean tryToGrab(boolean allDynamic) {
    if (sameSnippetID(programID, getDBProgramID())) return false;
    RemoteDB db = connectToDBOpt(programID);
    try {
      if (db != null) {
        loadGrab(db.fullgrab(), allDynamic);
        return true;
      }
    } finally {
      if (db != null) db.close();
    }
    return false;
  }
  
  Concepts load(String grab) {
    return loadGrab(grab, false);
  }
  
  Concepts safeLoad(String grab) {
    return loadGrab(grab, true);
  }
  
  Concepts loadGrab(String grab, boolean allDynamic) {
    clearConcepts();
    DynamicObject_loading.set(true);
    try {
      Map<Long, Concept> map = (Map)
        (allDynamic ? safeUnstructure(grab) : unstructure(grab));
      concepts.putAll(map);
      assignConceptsToUs();
      for (long l : map.keySet())
        idCounter = max(idCounter, l);
    } finally {
      DynamicObject_loading.set(null);
    }
    allChanged();
    return this;
  }
  
  void assignConceptsToUs() {
    for (Concept c : values(concepts)) {
      c._concepts = this;
      callOpt_noArgs(c, "_doneLoading2");
    }
  }

  String progID() {
    return programID == null ? getDBProgramID() : programID;
  }
  
  Concept getConcept(String id) {
    return empty(id) ? null : getConcept(parseLong(id));
  }
  
  Concept getConcept(long id) {
    return (Concept) concepts.get((long) id);
  }
  
  Concept getConcept(RC ref) {
    return ref == null ? null : getConcept(ref.longID());
  }
  
  boolean hasConcept(long id) {
    return concepts.containsKey((long) id);
  }
  
  void deleteConcept(long id) {
    Concept c = getConcept(id);
    if (c == null)
      print("Concept " + id + " not found");
    else
      c.delete();
  }
  
  void calcIdCounter() {
    long id_ = 0;
    for (long id : keys(concepts))
      id_ = max(id_, id);
    idCounter = id_+1;
    saveLocally2(this, programID, "idCounter");
  }
  
  void saveConceptsIfDirty() { saveConcepts(); }
  void save() { saveConcepts(); }

  void saveConcepts() {
    if (dontSave) return;
    initProgramID();
    saverLock.lock();
    savingConcepts = true;
    long start = now(), time;
    try {
      String s = null;
      //synchronized(main.class) {
      long _changes = changes;
      if (_changes == changesWritten) return;
      
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
      
      while (nempty(saveActions))
        pcallF(popFirst(saveActions));

      changesWritten = _changes; // only update when structure didn't fail
      
      if (!useGZIP) {
        time = now()-start;
        if (!quietSave)
          print("Saving " + toM(l(s)) + "M chars (" /*+ changesWritten + ", "*/ + time + " ms)");
        start = now();
        saveTextFile(f, javaTokWordWrap(s));
        getProgramFile(programID, "concepts.structure.gz").delete();
      }
      
      copyFile(f, getProgramFile(programID, "backups/concepts.structure" + (useGZIP ? ".gz" : "") + ".backup" + ymd() + "-" + formatInt(hours(), 2)));
      time = now()-start;
      if (!quietSave)
        print(programID + ": Saved " + toK(f.length()) + " K, " + n(concepts, "concepts") + " (" + time + " ms)");
      lastSaveWas = fullTime;
      lastSaveTook = now()-fullTime;
    } finally {
      savingConcepts = false;
      saverLock.unlock();
    }
  }
  
  void _autoSaveConcepts() {
    if (autoSaveInterval < 0 && maxAutoSavePercentage != 0) {
      long pivotTime = Math.round(lastSaveWas+lastSaveTook*100.0/maxAutoSavePercentage);
      if (now() < pivotTime) {
        //print("Skipping auto-save (last save took " + lastSaveTook + ")");
        return;
      }
    }
    try {
      saveConcepts();
    } catch (Throwable e) {
      print("Concept save failed, will try again: " + e);
    }
  }
  
  void clearConcepts() {
    concepts.clear();
    allChanged();
  }
  
  synchronized void allChanged() {
    ++changes;
  }
  
  // auto-save every second if dirty
  synchronized void autoSaveConcepts() {
    if (autoSaver == null) {
      if (isTransient()) throw fail("Can't persist transient database");
      autoSaver = doEvery_daemon(abs(autoSaveInterval), new Runnable() { public void run() { try {  _autoSaveConcepts() ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "_autoSaveConcepts()"; }});
      // print("Installed auto-saver (" + autoSaveInterval + " ms, " + progID() + ")");
    }
  }
  
  void cleanMeUp() {
    boolean shouldSave = autoSaver != null;
    if (autoSaver != null) {
      autoSaver.cancel();
      autoSaver = null;
    }
    while (savingConcepts) sleepInCleanUp(10);
    if (shouldSave)
      saveConceptsIfDirty();
  }
  
  Map<Long, String> getIDsAndNames() {
    Map<Long,String> map = new HashMap();
    Map<Long, Concept> cloned = cloneMap(concepts);
    for (long id : keys(cloned)) 
      map.put(id, cloned.get(id).className);
    return map;
  }
  
  void deleteConcepts(List l) {
    for (Object o : l)
      if (o instanceof Long)
        concepts.remove((Long) o);
      else if (o instanceof Concept)
        ((Concept) o).delete();
      else
        warn("Can't delete " + getClassName(o));
  }
  
  <A extends Concept> A conceptOfType(Class<A> type) {
    return firstOfType(allConcepts(), type);
  }
  
  <A extends Concept> List<A> conceptsOfType(Class<A> type) {
    return filterByType(allConcepts(), type);
  }
  
  <A extends Concept> List<A> listConcepts(Class<A> type) {
    return conceptsOfType(type);
  }
  
  <A extends Concept> List<A> list(Class<A> type) {
    return conceptsOfType(type);
  }
  
  List<Concept> list(String type) {
    return conceptsOfType(type);
  }
  
  List<Concept> conceptsOfType(String type) {
    return filterByDynamicType(allConcepts(), "main$" + type);
  }
  
  boolean hasConceptOfType(Class<? extends Concept> type) {
    return hasType(allConcepts(), type);
  }
  
  void persistConcepts() {
    loadConcepts();
    autoSaveConcepts();
  }
  
  // We love synonyms
  void conceptPersistence() {
    persistConcepts();
  }
  
  Concepts persist() { persistConcepts(); return this; }
  void persist(int interval) { autoSaveInterval = interval; persist(); }
    
  // Runs r if there is no concept of that type
  <A extends Concept> A ensureHas(Class<A> c, Runnable r) {
    A a = conceptOfType(c);
    if (a == null) {
      r.run();
      a = conceptOfType(c);
      if (a == null)
        throw fail("Concept not made by " + r + ": " + shortClassName(c));
    }
    return a;
  }
  
  // Ensures that every concept of type c1 is ref'd by a concept of
  // type c2.
  // Type of func: voidfunc(concept)
  void ensureHas(Class<? extends Concept> c1, Class<? extends Concept> c2, Object func) {
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
  
  // Type of func: voidfunc(concept)
  void forEvery(Class<? extends Concept> type, Object func) {
    for (Concept c : conceptsOfType(type))
      callF(func, c);
  }
  
  int deleteAll(Class<? extends Concept> type) {
    List<Concept> l = (List) conceptsOfType(type);
    for (Concept c : l) c.delete();
    return l(l);
  }
  
  Collection<Concept> allConcepts() {
    synchronized(concepts) {
      return new ArrayList(values(concepts));
    }
  }
  
  <A extends Concept> int countConcepts(Class<A> c, Object... params) {
    int n = 0;
    for (A x : list(c)) if (checkConceptFields(x, params)) ++n;
    return n;
  }

  int countConcepts(String c, Object... params) {
    int n = 0;
    for (Concept x : list(c)) if (checkConceptFields(x, params)) ++n;
    return n;
  }

  int countConcepts() {
    return l(concepts);
  }
  
  synchronized void addConceptIndex(IConceptIndex index) {
    if (conceptIndices == null)
      conceptIndices = new ArrayList();
    conceptIndices.add(index);
  }
  
  synchronized void addFieldIndex(Class<? extends Concept> c, String field, IFieldIndex index) {
    if (fieldIndices == null)
      fieldIndices = new HashMap();
    Map<String, IFieldIndex> map = fieldIndices.get(c);
    if (map == null)
      fieldIndices.put(c, map = new HashMap());
    map.put(field, index);
  }
  
  synchronized IFieldIndex getFieldIndex(Class<? extends Concept> c, String field) {
    if (fieldIndices == null) return null;
    Map<String, IFieldIndex> map = fieldIndices.get(c);
    return map == null ? null : map.get(field);
  }
  
  // inter-process methods
  
  RC xnew(String name, Object... values) {
    return new RC(cnew(name, values));
  }
  
  void xset(long id, String field, Object value) {
    xset(new RC(id), field, value);
  }
  
  void xset(RC c, String field, Object value) {
    if (value instanceof RC)
      value = getConcept((RC) value);
    cset(getConcept(c), field, value);
  }
  
  Object xget(long id, String field) {
    return xget(new RC(id), field);
  }
  
  Object xget(RC c, String field) {
    return xgetPost(cget(getConcept(c), field));
  }
  
  Object xgetPost(Object o) {
    o = deref(o);
    if (o instanceof Concept)
      return new RC((Concept) o);
    return o;
  }
  
  void xdelete(long id) {
    xdelete(new RC(id));
  }
  
  void xdelete(RC c) {
    getConcept(c).delete();
  }
  
  void xdelete(List<RC> l) {
    for (RC c : l)
      xdelete(c);
  }
  
  List<RC> xlist() {
    return map("toPassRef", allConcepts());
  }
  
  List<RC> xlist(String className) {
    return map("toPassRef", conceptsOfType(className));
  }
  
  boolean isTransient() { return eq(programID, "-"); }
  
  String xfullgrab() {
    lock.lock();
    try {
      if (changes == changesWritten && !isTransient())
        return loadConceptsStructure(programID);
      return structure(cloneMap(concepts));
    } finally {
      lock.unlock();
    }
  }
  
  void xshutdown() {
    // Killing whole VM if someone wants this DB to shut down
    cleanKillVM();
  }
  
  long xchangeCount() { return changes; }
  int xcount() { return countConcepts(); }
  
  void register(Concept c) {
    if (c._concepts == this) return;
    if (c._concepts != null) throw fail("Can't re-register");
    c._concepts = this;
    c.id = internalID();
    c.created = now();
    concepts.put((long) c.id, c);
    c.change();
  }
  
} // class Concepts

static volatile Concepts mainConcepts = new Concepts(); // Where we create new concepts

static class Concept extends DynamicObject {
  transient Concepts _concepts; // Where we belong
  long id;
  //O madeBy;
  //double energy;
  //bool defunct;
  long created;
  
  // used only internally (cnew)
  Concept(String className) {
    super(className);
    _created();
  }
  
  Concept() {
    if (!_loading()) {
      //className = shortClassName(this); // XXX - necessary?
      //print("New concept of type " + className);
      _created();
    }
  }
  
  Concept(boolean unlisted) {
    if (!unlisted) _created();
  }
  
  
  List<Ref> refs;
  List<Ref> backRefs;
  
  static boolean loading() { return _loading(); }
  static boolean _loading() { return isTrue(DynamicObject_loading.get()); }

  void _created() {
    if (!isTrue(concepts_unlisted.get()))
      mainConcepts.register(this);
  }
  
  /*void put(S field, O value) {
    fieldValues.put(field, value);
    change();
  }
  
  O get(S field) {
    ret fieldValues.get(field);
  }*/
  
  class Ref<A extends Concept> {
    A value;
    
    Ref() {
      if (!isTrue(DynamicObject_loading.get())) refs = addDyn(refs, this);
    }
    
    Ref(A value) {
  this.value = value;
      refs = addDyn(refs, this);
      index();
    }
    
    // get owning concept (source)
    Concept concept() {
      return Concept.this;
    }
    
    // get target
    A get() { return value; }
    boolean has() { return value != null; }
    
    void set(A a) {
      if (a == value) return;
      unindex();
      value = a;
      index();
    }
    
    void set(Ref<A> ref) { set(ref.get()); }
    void clear() { set((A) null); }
    
    void index() { 
      if (value != null)
        value.backRefs = addDyn(value.backRefs, this);
      change();
    }
    
    void unindex() {
      if (value != null)
        value.backRefs = removeDyn(value.backRefs, this);
    }
    
    void change() {
      Concept.this.change();
    }
  }
  
  class RefL<A extends Concept> extends AbstractList<A> {
    List < Ref < A > > l = new ArrayList();
    
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
        for (Ref<A> r : l) if (eq(r.get(), o)) return true;
      return super.contains(o);
    }
  }
  
  void delete() {
    //name = "[defunct " + name + "]";
    //defunct = true;
    //energy = 0;
    for (Ref r : unnull(refs))
      r.unindex();
    refs = null;
    for (Ref r : cloneList(backRefs))
      r.set((Concept) null);
    backRefs = null;
    
    if (_concepts != null) {
      _concepts.concepts.remove((long) id);
      _concepts.allChanged();
      if (_concepts.conceptIndices != null)
        for (IConceptIndex index : _concepts.conceptIndices)
          index.remove(this);
      _concepts = null;
    }
    id = 0;
  }
  
  BaseXRef export() {
    return new BaseXRef(_concepts.progID(), id);
  }
  
  // notice system of a change in this object
  void change() {
    if (_concepts != null) {
      _concepts.allChanged();
      if (_concepts.conceptIndices != null)
        for (IConceptIndex index : _concepts.conceptIndices)
          index.update(this);
    }
  }
  
  void _change() { change(); }
  
  String _programID() {
    return _concepts == null ? getDBProgramID() : _concepts.progID();
  }
} // class Concept

// remote reference (for inter-process communication or
// external databases). Formerly "PassRef".
// prepared for string ids if we do them later
static class RC {
  transient Object owner;
  String id;
  
  RC() {} // make serialisation happy
  RC(long id) { this.id = str(id); }
  RC(Object owner, long id) { this.id = str(id); this.owner = owner; }
  RC(Concept c) { this(c.id); }
  long longID() { return parseLong(id); }
  
  public String toString() {
    return id;
  }

  transient RemoteDB db;
  
  String getString(String field) { return db.xS(this, field); }
  Object get(String field) { return db.xget(this, field); }
  void set(String field, Object value) { db.xset(this, field, value); }

}

// Reference to a concept in another program
static class BaseXRef {
  String programID;
  long id;
    
  BaseXRef() {}
  BaseXRef(String programID, long id) {
  this.id = id;
  this.programID = programID;}
  
  public boolean equals(Object o) {
    if (!(o instanceof BaseXRef)) return false;
    BaseXRef r = (BaseXRef) ( o);
    return eq(programID, r.programID) && eq(id, r.id);
  }
  
  public int hashCode() {
    return programID.hashCode() + (int) id;
  }
}

// BaseXRef as a concept
static class XRef extends Concept {
  BaseXRef ref;
  
  XRef() {}
  XRef(BaseXRef ref) {
  this.ref = ref; _doneLoading2(); }
  
  // after we have been added to concepts
  void _doneLoading2() {
    getIndex().put(ref, this);
  }
    
  HashMap<BaseXRef, XRef> getIndex() {
    return getXRefIndex(_concepts);
  }
}

static synchronized HashMap<BaseXRef, XRef> getXRefIndex(Concepts concepts) {
  HashMap cache = (HashMap) concepts.perClassData.get(XRef.class);
  if (cache == null)
    concepts.perClassData.put(XRef.class, cache = new HashMap());
  return cache;
}

// uses mainConcepts
static XRef lookupOrCreateXRef(BaseXRef ref) {
  XRef xref = getXRefIndex(mainConcepts).get(ref);
  if (xref == null)
    xref = new XRef(ref);
  return xref;
}

// define standard concept functions to use main concepts

static <A extends Concept> List<A> list(Class<A> type) {
  return mainConcepts.list(type);
}

static <A extends Concept> List<A> list(Concepts concepts, Class<A> type) {
  return concepts.list(type);
}

static List<Concept> list(String type) {
  return mainConcepts.list(type);
}

static List<Concept> list(Concepts concepts, String type) {
  return concepts.list(type);
}

static int csetAll(Concept c, Object... values) {
  return cset(c, values);
}

static void cleanMeUp_concepts() {
  mainConcepts.cleanMeUp();
}

static void loadAndAutoSaveConcepts() {
  mainConcepts.persist();
}

static void loadAndAutoSaveConcepts(int interval) {
  mainConcepts.persist(interval);
}

static void loadConceptsFrom(String progID) {
  mainConcepts.programID = progID;
  mainConcepts.load();
}

static List<Concept> conceptsOfType(String type) {
  return mainConcepts.conceptsOfType(type);
}

static long changeCount() {
  return mainConcepts.changes;
}

static List<String> exposedDBMethods = ll("xlist", "xnew", "xset", "xdelete", "xget", "xclass", "xfullgrab", "xshutdown", "xchangeCount", "xcount");

static RC toPassRef(Concept c) {
  return new RC(c);
}

// so we can instantiate the program to run as a bare DB bot
static UnsupportedOperationException unsupportedOperation() {
  throw new UnsupportedOperationException();
}
static String quickVisualize_progID = "#1007145";
static Lock quickVisualize_lock = lock();

static boolean quickVisualize_hasCached(String query) {
  return quickVisualize_imageFile(query).length() != 0;
}

static BufferedImage quickVisualize_fromCache(String query) {
  File f = quickVisualize_imageFile(query);
  if (f.length() != 0) try { return loadPNG(f); } catch (Throwable __e) { printStackTrace2(__e); }
  return null;
}

static String quickVisualize_preprocess(String query) {
  return toUpper(shorten(trim(query), 200));
}

static BufferedImage quickVisualize(String query) {
  query = quickVisualize_preprocess(query);
  if (empty(query)) return null;
  BufferedImage img = quickVisualize_fromCache(query);
  if (img != null) return img;
  File f = quickVisualize_imageFile(query);
  /*L<S> urls = googleImageSearch_multi(query);
  saveTextFile(quickVisualize_urlsFile(query), joinLines(urls));
  if (empty(urls)) null;
  img = loadBufferedImage(first(urls));*/
  Lock _lock_1465 = quickVisualize_lock; lock(_lock_1465); try {
  img = googleImageSearchFirst(query);
  if (img == null) return null;
  savePNG(f, img);
  return img;
} finally { unlock(_lock_1465); } }

static String quickVisualize_imagePath(String query) {
  query = quickVisualize_preprocess(query);
  return fsI(quickVisualize_progID) + "/" + urlencode(query) + ".png";
}

static File quickVisualize_imageFile(String query) {
  query = quickVisualize_preprocess(query);
  return prepareProgramFile(quickVisualize_progID, urlencode(query) + ".png");
}

static File quickVisualize_urlsFile(String query) {
  query = quickVisualize_preprocess(query);
  return prepareProgramFile(quickVisualize_progID, "urls-" + urlencode(query) + ".txt");
}
static int thoughtCircleSize(BufferedImage img) {
  return min(img.getWidth(), img.getHeight()) + 20;
}
static JFrame showFullScreen(final JComponent c) {
  return (JFrame) swingAndWait(new F0<Object>() { Object get() { try { 
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
      .getDefaultScreenDevice();
    if (!gd.isFullScreenSupported())
      throw fail("No full-screen mode supported!");
    boolean dec = JFrame.isDefaultLookAndFeelDecorated();
    if (dec) JFrame.setDefaultLookAndFeelDecorated(false);
    final JFrame window = new JFrame();
    window.setUndecorated(true);
    if (dec) JFrame.setDefaultLookAndFeelDecorated(true);
    registerEscape(window, new Runnable() { public void run() { try {  disposeWindow(window) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "disposeWindow(window)"; }});
    window.add(wrap(c));
    gd.setFullScreenWindow(window);
    
    // Only this hides the task bar in Peppermint Linux w/Substance
    for (int i = 100; i <= 1000; i += 100)
      awtLater(i, new Runnable() { public void run() { try {  window.toFront() ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "window.toFront()"; }});
    
    return window;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()\r\n      ..."; }});
}
// make a lisp form
static Lisp lisp(String head, Object... args) {
  Lisp l = new Lisp(head);
  for (Object o : args)
    l.add(o);
  return l;
}

static Lisp lisp(String head, Collection args) {
  return new Lisp(head, args);
}

static String lines(Collection lines) { return fromLines(lines); }
static List<String> lines(String s) { return toLines(s); }
static int hashCodeFor(Object a) {
  return a == null ? 0 : a.hashCode();
}
static Pt scalePt(Pt p, double f) {
  return new Pt(iround(p.x*f), iround(p.y*f));
}
static boolean stdEq2(Object a, Object b) {
  if (a == null) return b == null;
  if (b == null) return false;
  if (a.getClass() != b.getClass()) return false;
  for (String field : allFields(a))
    if (neq(getOpt(a, field), getOpt(b, field)))
      return false;
  return true;
}
static BufferedImage drawThoughtCirclePlus_img;
static int drawThoughtCirclePlus_size = 24;

static void drawThoughtCirclePlus(BufferedImage bg, BufferedImage img, double x, double y) {
  if (drawThoughtCirclePlus_img == null)
    drawThoughtCirclePlus_img = resizeImage(loadImage2("#1009864"), drawThoughtCirclePlus_size);
  x -= drawThoughtCirclePlus_img.getWidth()/2;
  y -= drawThoughtCirclePlus_img.getHeight()/2;
  drawImageOnImage(drawThoughtCirclePlus_img, bg, iround(x), iround(y));
}
static double pointDistance(Pt a, Pt b) {
  return sqrt(sqr(a.x-b.x) + sqr(a.y-b.y));
}

static double pointDistance(double x1, double y1, double x2, double y2) {
  return sqrt(sqr(x1-x2) + sqr(y1-y2));
}
static int stdHash2(Object a) {
  if (a == null) return 0;
  return stdHash(a, toStringArray(allFields(a)));
}
static boolean inRange(int x, int n) {
  return x >= 0 && x < n;
}
static DoublePt translateDoublePt(Pt a, DoublePt b) {
  return a == null ? b : b == null ? new DoublePt(a.x, a.y) : new DoublePt(a.x+b.x, a.y+b.y);
}
static int drawThoughtCircleText_margin = 5;
static Color drawThoughtCircleText_color = Color.yellow;

static void drawThoughtCircleText(BufferedImage bg, BufferedImage img, DoublePt p, String text) {
  Graphics2D g = imageGraphics(bg);
  g.setFont(sansSerifBold(20));
  FontMetrics fm = g.getFontMetrics();
  int h = fm.getHeight();
  double y = p.y+thoughtCircleSize(img)/2+drawThoughtCircleText_margin;
  for (String s : lines(text)) {
    drawTextWithOutline(g, s, (float) (p.x-fm.stringWidth(s)/2), (float) (y+fm.getLeading()+fm.getMaxAscent()), drawThoughtCircleText_color, Color.black);
    y += h;
  }
  g.dispose();
}
static boolean containsInstance(Iterable i, Class c) {
  if (i != null) for (Object o : i)
    if (isInstanceX(c, o))
      return true;
  return false;
}
static void markWebsPosted() {
  markWebsPosted_createMarker();
  triggerWebsChanged();
}
static boolean loadBufferedImage_useImageCache = true;

static BufferedImage loadBufferedImage(String snippetIDOrURL) { try {
  if (snippetIDOrURL == null) return null;
  if (isURL(snippetIDOrURL))
    return ImageIO.read(new URL(snippetIDOrURL));

  if (!isSnippetID(snippetIDOrURL)) throw fail("Not a URL or snippet ID: " + snippetIDOrURL);
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
        // fall back to loading from sourceforge
      }
  }

  String imageURL = snippetImageURL(snippetID);
  System.err.println("Loading image: " + imageURL);
  BufferedImage image = ImageIO.read(new URL(imageURL));

  if (loadBufferedImage_useImageCache) {
    File tempFile = new File(dir, snippetID + ".tmp." + System.currentTimeMillis());
    ImageIO.write(image, "png", tempFile);
    tempFile.renameTo(new File(dir, snippetID + ".png"));
    //Log.info("Cached image.");
  }

  //Log.info("Loaded image.");
  return image;
} catch (Exception __e) { throw rethrow(__e); } }

static BufferedImage loadBufferedImage(File file) { try {
  return file.isFile() ? ImageIO.read(file) : null;
} catch (Exception __e) { throw rethrow(__e); } }
static void moveAllMenuItems(JPopupMenu src, JMenu dest) {
  Component[] l = src.getComponents();
  src.removeAll();
  for (Component c : l)
    dest.add(c);
}
static int ptY(Pt p) {
  return p == null ? 0 : p.y;
}
static int ptX(Pt p) {
  return p == null ? 0 : p.x;
}
static <A> boolean setAdd(Collection<A> c, A a) {
  if (c == null || c.contains(a)) return false;
  c.add(a);
  return true;
}
static BufferedImage clipBufferedImage(BufferedImage src, Rectangle clip) {
  return src.getSubimage(clip.x, clip.y, clip.width, clip.height);
}

static BufferedImage clipBufferedImage(BufferedImage src, Rect clip) {
  return clipBufferedImage(src, clip.getRectangle());
}

static BufferedImage clipBufferedImage(BufferedImage src, int x, int y, int w, int h) {
  return src.getSubimage(x, y, w, h);
}

static void clearAll(Object... l) {
  for (Object o : l) callOpt(o, "clear");
}
static JScrollPane enclosingScrollPane(Component c) {
  while (c.getParent() != null && !(c.getParent() instanceof JViewport) && c.getParent().getComponentCount() == 1) c = c.getParent(); // for jscroll_center
  if (!(c.getParent() instanceof JViewport)) return null;
  c = c.getParent().getParent();
  return c instanceof JScrollPane ? (JScrollPane) c : null;
}
static <A> A findWhere(Collection<A> c, Object... data) {
  for (A x : c)
    if (checkFields(x, data))
      return x;
  return null;
}
// independent timer
static void awtLater(int delay, final Object r) {
  swingLater(delay, r);
}

static void awtLater(Object r) {
  swingLater(r);
}

// dependent timer (runs only when component is visible)
static void awtLater(JComponent component, int delay, Object r) {
  installTimer(component, r, delay, delay, false);
}

static void awtLater(JFrame frame, int delay, Object r) {
  awtLater(frame.getRootPane(), delay, r);
}
static class Canvas extends ImageSurface {
  Object makeImg;
  boolean updating;
  
  Canvas() { zoomable = false; }
  Canvas(Object makeImg) { this(); this.makeImg = makeImg; }
  
  void update() { updateCanvas(this, makeImg); }
}

static Canvas jcanvas() { return jcanvas(null, 0); }

// f: (int w, int h) -> BufferedImage
static Canvas jcanvas(Object f) {
  return jcanvas(f, 0); // 100
}

static Canvas jcanvas(final Object f, final int updateDelay) {
  return (Canvas) swing(new F0<Object>() { Object get() { try { 
    final Canvas is = new Canvas(f);
    is.specialPurposed = true;
    final Runnable update = new Runnable() {
      boolean first = true;
      public void run() {
        BufferedImage img = is.getImage();
        int w = is.getWidth(), h = is.getHeight();
        if (first || img.getWidth() != w || img.getHeight() != h) {
          updateCanvas(is, f);
          first = false;
        }
      }
    };
    onResize(is, new Runnable() { public void run() { try {  awtLater(is, updateDelay, update) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "awtLater(is, updateDelay, update)"; }});
    bindToComponent(is, update); // first update
    return is;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "final Canvas is = new Canvas(f);\r\n    is.specialPurposed = true;\r\n    final R..."; }});
}
static <A> void setAddAll(List<A> a, Collection<A> b) {
  for (A x : b)
    setAdd(a, x);
}
static boolean containsIC(Collection<String> l, String s) {
  return containsIgnoreCase(l, s);
}

static boolean containsIC(String[] l, String s) {
  return containsIgnoreCase(l, s);
}

static boolean containsIC(String s, char c) {
  return containsIgnoreCase(s, c);
}

static boolean containsIC(String a, String b) {
  return containsIgnoreCase(a, b);
}
static int asInt(Object o) {
  return toInt(o);
}

static int drawThoughtLineText_shift = 10;
static void drawThoughtLineText_multiLine(BufferedImage bg,
  BufferedImage img1, int x1, int y1,
  BufferedImage img2, int x2, int y2,
  String text, Color color) {
  
  Graphics2D g = imageGraphics(bg);
  g.setColor(color);
  g.setFont(sansSerif(20));
  
  int w1 = img_minOfWidthAndHeight(img1);
  int w2 = img_minOfWidthAndHeight(img2);
  int sideShift = (w1-w2)/4;
  int len = vectorLength(x2-x1, y2-y1);
  int dx = iround(sideShift*(x2-x1)/len), dy = iround(sideShift*(y2-y1)/len);
  x1 += dx; x2 += dx;
  y1 += dy; y2 += dy;
  
  FontMetrics fm = g.getFontMetrics();
  int lineHeight = fm.getHeight();
  
  float yshift = 0;
  for (String line : reversed(lines(text))) {
    drawOutlineTextAlongLine(g, line, x1, y1, x2, y2, drawThoughtLine_width/2+drawThoughtLineText_shift, yshift, color, Color.black);
    yshift -= lineHeight; 
  }
  
  g.dispose();
}
static String firstToUpper(String s) {
  if (empty(s)) return s;
  return Character.toUpperCase(s.charAt(0)) + s.substring(1);
}
static int updateCanvas_retryInterval = 50;

// if makeImg returns null, it is recalled after a delay
static void updateCanvas(final Canvas canvas, final Object makeImg) {
  swingNowOrLater(new Runnable() { public void run() { try { 
    if (canvas.updating || canvas.getWidth() == 0) return;
    canvas.updating = true;
    try {
      BufferedImage img = asBufferedImage(callF(makeImg, canvas.getWidth(), canvas.getHeight()));
      if (img != null) {
        canvas.setImage(img);
        canvas.updating = false;
      } else
        awtLater(updateCanvas_retryInterval, new Runnable() { public void run() { try { 
          canvas.updating = false;
          updateCanvas(canvas, makeImg);
        
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "canvas.updating = false;\r\n          updateCanvas(canvas, makeImg);"; }});
    } catch (Throwable e) {
      canvas.updating = false;
      throw rethrow(e);
    }
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "if (canvas.updating || canvas.getWidth() == 0) return;\r\n    canvas.updating =..."; }});
}

static void updateCanvas(final Canvas canvas) {
  if (canvas != null) canvas.update();
}
static boolean hasTransparency(BufferedImage img) {
  return img.getColorModel().hasAlpha();
}
static double distancePointToLineSegment(Pt a, Pt b, Pt p) {
  int x1 = a.x, y1 = a.y, x2 = b.x, y2 = b.y, x3 = p.x, y3 = p.y;
  
  float px=x2-x1;
  float py=y2-y1;
  float temp=(px*px)+(py*py);
  float u=((x3 - x1) * px + (y3 - y1) * py) / (temp);
  if (u>1) u=1; else if(u<0) u=0;
  float x = x1 + u * px;
  float y = y1 + u * py;
  float dx = x - x3;
  float dy = y - y3;
  return sqrt(dx*dx + dy*dy);
}
static <A extends JComponent> A setToolTipText(final A c, final Object toolTip) {
  if (c == null) return null;
  { swing(new Runnable() { public void run() { try { 
    String s = nullIfEmpty(str(toolTip));
    if (neq(s, c.getToolTipText()))
      c.setToolTipText(s);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "String s = nullIfEmpty(str(toolTip));\r\n    if (neq(s, c.getToolTipText()))\r\n ..."; }}); }
  return c;
}
static void copyCAL(CirclesAndLines cal1, CirclesAndLines cal2) {
  if (cal1 == cal2) return;
  copyList(cal1.circles, cal2.circles);
  copyList(cal1.lines, cal2.lines);
  copyFields(cal1, cal2, "defaultImageID",  "arrowClass", "circleClass", "imgZoom", "imageForUserMadeNodes", "title", "globalID");
  copyListeners(cal1, cal2);
}
static AutoComboBox autoComboBox() {
  return autoComboBox(new ArrayList());
}

static AutoComboBox autoComboBox(final Collection<String> items) {
  return swing(new F0<AutoComboBox>() { AutoComboBox get() { try { 
    AutoComboBox cb = new AutoComboBox();
    cb.setKeyWord(items);
    return cb;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "new AutoComboBox cb;\r\n    cb.setKeyWord(items);\r\n    ret cb;"; }});
}

static AutoComboBox autoComboBox(String value, Collection<String> items) {
  return setText(autoComboBox(items), value);
}
static int drawThoughtArrow_size = 15;

static void drawThoughtArrow(BufferedImage bg,
  BufferedImage img1, double x1, double y1,
  BufferedImage img2, double x2, double y2, Color color) {
  double cs = thoughtCircleSize(img2)/2-1;
  double dist = pointDistance(x1, y1, x2, y2);
  double arrowLen = drawThoughtArrow_size*drawArrowHead_length-1;
  DoublePt v = blendDoublePts(new DoublePt(x2, y2), new DoublePt(x1, y1), (cs+arrowLen)/dist);
  DoublePt p = blendDoublePts(new DoublePt(x2, y2), new DoublePt(x1, y1), cs/dist);
  Graphics2D g = imageGraphics(bg);
  g.setColor(color);
  g.setStroke(new BasicStroke(drawThoughtLine_width));
  g.draw(new Line2D.Double(x1, y1, v.x, v.y));
  drawArrowHead(g, x1, y1, p.x, p.y, drawThoughtArrow_size);
  g.dispose();
}
static JFrame showCenterFrame(String title, int w, int h) {
  return showCenterFrame(title, w, h, null);
}

static JFrame showCenterFrame(int w, int h, Component content) {
  return showCenterFrame(defaultFrameTitle(), w, h, content);
}

static JFrame showCenterFrame(String title, int w, int h, Component content) {
  JFrame frame = makeFrame(title, content);
  frame.setSize(w, h);
  return centerFrame(frame);
}

static JFrame showCenterFrame(String title, Component content) {
  return centerFrame(makeFrame(title, content));
}


static JFrame showCenterFrame(Component content) {
  return centerFrame(makeFrame(content));
}
static Pt untranslatePt(Pt a, Pt b) {
  if (a == null) return b;
  return new Pt(b.x-a.x, b.y-a.y);
}
static String fsi(String id) {
  return formatSnippetID(id);
}



static String getTextFromClipboard() { try {
  Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
  if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
    return (String) transferable.getTransferData(DataFlavor.stringFlavor);
  return null;
} catch (Exception __e) { throw rethrow(__e); } }
static BufferedImage quickVisualizeOr(String query, String defaultImageID) {
  BufferedImage img = quickVisualize(query);
  return img != null ? img : loadImage2(defaultImageID);
}
static <A> A nu(Class<A> c, Object... values) {
  A a = nuObject(c);
  setAll(a, values);
  return a;
}
static String web_defaultRelationName = "";

static String web_defaultRelationName() {
  return web_defaultRelationName;
}
static JFrame frameInnerSize(final Component c, final double w, final double h) {
  final JFrame frame = getFrame(c);
  if (frame != null) { swing(new Runnable() { public void run() { try { 
    Container cp = frame.getContentPane();
    Dimension oldSize = cp.getPreferredSize();
    cp.setPreferredSize(new Dimension(iround(w), iround(h)));
    frame.pack();
    cp.setPreferredSize(oldSize);
  
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "Container cp = frame.getContentPane();\r\n    Dimension oldSize = cp.getPreferr..."; }}); }
  return frame;
}

static void frameInnerSize(JFrame frame, Dimension d) {
  frameInnerSize(frame, d.width, d.height);
}

static JFrame frameInnerSize(Pt p, JFrame frame) {
  frameInnerSize(frame, p.x, p.y);
  return frame;
}
static Pt pt(int x, int y) {
  return new Pt(x, y);
}
static boolean neqic(String a, String b) {
  return !eqic(a, b);
}
static <A> Iterator<A> iterator(Iterable<A> c) {
  return c == null ? emptyIterator() : c.iterator();
}
static void showFullScreenImageSurface(BufferedImage img) {
  showFullScreen(jscroll_centered(disposeFrameOnClick(new ImageSurface(img))));
}
static <B, A extends B> A addAndReturn(Collection<B> c, A a) {
  if (c != null) c.add(a);
  return a;
}
static <A> List<A> getWhere(Collection<A> c, Object... data) {
  List l = new ArrayList();
  for (A x : c)
    if (checkFields(x, data))
      l.add(x);
  return l;
}
static Color drawThoughtCircle_defaultColor = Color.white;

static void drawThoughtCircle(BufferedImage bg, BufferedImage img, double x, double y) {
  // TODO: crop to certain size
  BufferedImage circle = cutImageToCircle(img_addBorder(cutImageToCircle(img), drawThoughtCircle_defaultColor, 10));
  x -= circle.getWidth()/2;
  y -= circle.getHeight()/2;
  drawImageOnImage(circle, bg, iround(x), iround(y));
}
static long stopTiming_defaultMin = 10;

static long startTiming_startTime;
static void startTiming() {
  startTiming_startTime = now();
}

static void stopTiming() {
  stopTiming(null);
}

static void stopTiming(String text) {
  stopTiming(text, stopTiming_defaultMin);
}

static void stopTiming(String text, long minToPrint) {
  long time = now()-startTiming_startTime;
  if (time >= minToPrint) {
    text = or2(text, "Time: ");
    print(text + time + " ms");
  }
}
static void disableImageSurfaceSelector(ImageSurface is) {
  ImageSurfaceSelector s = firstInstance(is.tools, ImageSurfaceSelector.class);
  if (s == null) return;
  is.removeMouseListener(s);
  is.removeMouseMotionListener(s);
  is.tools.add(s);
}

static JMenu jmenu(final String title, final Object... items) {
  return swing(new F0<JMenu>() { JMenu get() { try { 
    JMenu menu = new JMenu(title);
    fillJMenu(menu, items);
    return menu;
   } catch (Exception __e) { throw rethrow(__e); } }
  public String toString() { return "JMenu menu = new(title);\r\n    fillJMenu(menu, items);\r\n    ret menu;"; }});
}
static boolean possibleGlobalID(String s) {
  return l(s) == 16 && allLowerCaseCharacters(s);
}
static void popup(final Throwable throwable) {
  popupError(throwable);
}

static void popup(final String msg) {
  print(msg);
  SwingUtilities.invokeLater(new Runnable() {
    public void run() {
      JOptionPane.showMessageDialog(null, msg);
    }
  });
}
static BufferedImage renderTiledBackground(String tileImageID, int w, int h) {
  return renderTiledBackground(tileImageID, w, h, 0, 0);
}

static BufferedImage renderTiledBackground(String tileImageID, int w, int h, int shiftX, int shiftY) {
  BufferedImage tileImage = loadImage2(tileImageID);
  BufferedImage img = newBufferedImage(w, h, Color.black);
  Graphics2D g = img.createGraphics();
  int tw = tileImage.getWidth(), th = tileImage.getHeight();
  for (int x = mod(shiftX-1, tw)-tw+1; x < w; x += tw)
    for (int y = mod(shiftY-1, th)-th+1; y < h; y += th)
      g.drawImage(tileImage, x, y, null);  
  g.dispose();
  return img;
} 
static int drawThoughtLine_width = 10;

static void drawThoughtLine(BufferedImage bg,
  BufferedImage img1, int x1, int y1,
  BufferedImage img2, int x2, int y2, Color color) {
  Graphics2D g = imageGraphics(bg);
  g.setColor(color);
  g.setStroke(new BasicStroke(drawThoughtLine_width));
  g.drawLine(x1, y1, x2, y2);
  g.dispose();
}
static BufferedImage loadImage2(String snippetIDOrURL) {
  return loadBufferedImage(snippetIDOrURL);
}

static BufferedImage loadImage2(File file) {
  return loadBufferedImage(file);
}
static Map<BufferedImage, Object> createGraphics_modulators = synchroIdentityHashMap();

static Graphics2D createGraphics(BufferedImage img) {
  Graphics2D g = img.createGraphics();
  Object mod = createGraphics_modulators.get(img);
  if (mod != null)
    callF(mod, g);
  return g;
}

// mod: voidfunc(Graphics2D)
static void createGraphics_modulate(BufferedImage img, Object mod) {
  mapPut2(createGraphics_modulators, img, mod);
}


// uses bilinear interpolation
static BufferedImage scaleImage(BufferedImage before, double scale) {
  if (scale == 1) return before;
  int w = before.getWidth();
  int h = before.getHeight();
  int neww = max(1, iround(w*scale)), newh = max(1, iround(h*scale));
  BufferedImage after = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_ARGB);
  AffineTransform at = new AffineTransform();
  at.scale(scale, scale);
  AffineTransformOp scaleOp = 
     new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
  return scaleOp.filter(before, after);
}



// onDrop: voidfunc(File)
static void jHandleFileDrop(JComponent c, final Object onDrop) {
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
      } catch (Throwable __e) { printStackTrace2(__e); }
      e.rejectDrop();
    }
  });
}
static long nowUnlessLoading() {
  return /*dynamicObjectIsLoading() ? 0 :*/ now();
}



static BufferedImage getImageFromClipboard() { try {
  Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
  if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
    return (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
  return imageFromDataURL(getTextFromClipboard());
} catch (Exception __e) { throw rethrow(__e); } }
static void requestFocus(final JComponent c) { focus(c); }


  static void popupError(final Throwable throwable) {
    throwable.printStackTrace(); // print stack trace to console for the experts
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        String text = throwable.toString();
        //text = cutPrefix(text, "java.lang.RuntimeException: ");
        JOptionPane.showMessageDialog(null, text);
      }
    });
  }
static int vectorLength(int x, int y) {
  return isqrt(sqr((long) x)+sqr((long) y));
}
static <A> void copyList(Collection<? extends A> a, Collection<A> b) {
  if (a == null || b == null) return;
  b.clear();
  b.addAll(a);
}
static File prepareProgramFile(String name) {
  return mkdirsForFile(getProgramFile(name));
}

static File prepareProgramFile(String progID, String name) {
  return mkdirsForFile(getProgramFile(progID, name));
}
static <A extends JComponent> A disposeFrameOnClick(final A c) {
  onClick(c, new Runnable() { public void run() { try {  disposeFrame(c) ;
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "disposeFrame(c)"; }});
  return c;
}
static Set<String> allFields(Object o) {
  TreeSet<String> fields = new TreeSet();
  Class _c = _getClass(o);
  do {
    for (Field f : _c.getDeclaredFields())
      fields.add(f.getName());
    _c = _c.getSuperclass();
  } while (_c != null);
  return fields;
}
static void savePNG(BufferedImage img, File file) { try {
  File tempFile = new File(file.getPath() + "_temp");
  CriticalAction ca = beginCriticalAction("Save " + f2s(file));
  try {
    ImageIO.write(img, "png", mkdirsFor(tempFile));
    file.delete();
    tempFile.renameTo(file);
  } finally {
    ca.done();
  }
} catch (Exception __e) { throw rethrow(__e); } }

// gotta love convenience & program-smartness
static void savePNG(File file, BufferedImage img) {
  savePNG(img, file);
}


static void savePNG(File file, RGBImage img) {
  savePNG(file, img.getBufferedImage());
}

static <A extends JComponent> A focus(final A a) {
  if (a != null) swingLater(new Runnable() { public void run() { try {  a.requestFocus(); 
} catch (Exception __e) { throw rethrow(__e); } }  public String toString() { return "a.requestFocus();"; }});
  return a;
}
static DoublePt blendDoublePts(DoublePt x, DoublePt y, double yish) {
  double xish = 1-yish;
  return new DoublePt(x.x*xish+y.x*yish, x.y*xish+y.y*yish);
}

static String nullIfEmpty(String s) {
  return isEmpty(s) ? null : s;
}
static void copyListeners(Object a, Object b) {
  for (String f : fieldNames(a))
    if (l(f) > 2 && startsWith(f, "on") && isUpperCase(f.charAt(2)))
      setOpt(b, f, getOpt(a, f));
}


static ThreadLocal<Boolean> drawOutlineTextAlongLine_flip = new ThreadLocal();

static void drawOutlineTextAlongLine(Graphics2D g, String text, int x1, int y1, int x2, int y2, int shift, Color fillColor, Color outlineColor) {
  drawOutlineTextAlongLine(g, text, x1, y1, x2, y2, shift, 0, fillColor, outlineColor);
}

static void drawOutlineTextAlongLine(Graphics2D g, String text, int x1, int y1, int x2, int y2, int shift, float yshift, Color fillColor, Color outlineColor) {
  Boolean flip = optPar(drawOutlineTextAlongLine_flip);
  if (y2 == y1 && x2 == x1) ++x2;
  
  AffineTransform tx = new AffineTransform();
  double angle = Math.atan2(y2-y1, x2-x1);
  if (flip == null)
    flip = abs(angle) > pi()/2;
  if (flip) angle -= pi();
  tx.translate((x1+x2)/2.0, (y1+y2)/2.0);
  tx.rotate(angle);

  FontMetrics fm = g.getFontMetrics();
  // int y = shift+fm.getLeading()+fm.getMaxAscent(); // below
  int y = -shift-fm.getMaxDescent(); // above
  //print("drawText y=" + y + ", shift=" + shift);
  
  AffineTransform old = g.getTransform();
  g.transform(tx);

  drawTextWithOutline(g, text, -fm.stringWidth(text)/2.0f, y+yshift, fillColor, outlineColor);
  g.setTransform(old);
}
static ThreadLocal<Boolean> imageGraphics_antiAlias = new ThreadLocal();

static Graphics2D imageGraphics(BufferedImage img) {
  return !isFalse(imageGraphics_antiAlias.get()) ? antiAliasGraphics(img) : createGraphics(img);
}
// usually L<S>
static String fromLines(Collection lines) {
  StringBuilder buf = new StringBuilder();
  if (lines != null)
    for (Object line : lines)
      buf.append(str(line)).append('\n');
  return buf.toString();
}

static String fromLines(String... lines) {
  return fromLines(asList(lines));
}
static BufferedImage img_addBorder(BufferedImage img, Color color, int border) {
  int top = border, bottom = border, left = border, right = border;
  int w = img.getWidth(), h = img.getHeight();
  BufferedImage img2 = createBufferedImage(left+w+right, top+h+bottom, color);
  Graphics2D g = img2.createGraphics();
  g.drawImage(img, left, top, null);
  g.dispose();
  return img2;
}


static BufferedImage cutImageToCircle(String imageID) {
  return cutImageToCircle(loadImage2(imageID));
}

static BufferedImage cutImageToCircle(BufferedImage image) {
  int w = min(image.getWidth(), image.getHeight());
  return cutImageToCircle(image, w);
}

static BufferedImage cutImageToCircle(BufferedImage image, int w) {
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
static boolean allLowerCaseCharacters(String s) {
  for (int i = 0; i < l(s); i++)
    if (Character.getType(s.charAt(i)) != Character.LOWERCASE_LETTER) return false;
  return true;
}
static int img_minOfWidthAndHeight(BufferedImage image) {
  return image == null ? 0 : min(image.getWidth(), image.getHeight());
}
static BufferedImage loadPNG(File file) {
  return loadBufferedImage(file);
}
static void setAll(Object o, Map<String, Object> fields) {
  if (fields == null) return;
  for (String field : keys(fields))
    set(o, field, fields.get(field));
}

static void setAll(Object o, Object... values) {
  //values = expandParams(c.getClass(), values);
  failIfOddCount(values);
  for (int i = 0; i+1 < l(values); i += 2) {
    String field = (String) values[i];
    Object value = values[i+1];
    set(o, field, value);
  }
}
static String defaultFrameTitle() {
  return autoFrameTitle();
}


static float drawArrowHead_length = 2f;

static void drawArrowHead(Graphics2D g, double x1, double y1, double x2, double y2, double size) {
  if (y2 == y1 && x2 == x1) return;
  
  Path2D.Double arrowHead = new Path2D.Double();
  arrowHead.moveTo(0, 0);
  double l = drawArrowHead_length*size;
  arrowHead.lineTo(-size, -l);
  arrowHead.lineTo(size, -l);
  arrowHead.closePath();

  AffineTransform tx = new AffineTransform();
  double angle = Math.atan2(y2-y1, x2-x1);
  tx.translate(x2, y2);
  tx.rotate(angle-Math.PI/2d);

  AffineTransform old = g.getTransform();
  g.transform(tx);
  g.fill(arrowHead);
  g.setTransform(old);
}
static JScrollPane jscroll_centered(Component c) {
  return new JScrollPane(jFullCenter(c));
}
static void onResize(Component c, final Object r) {
  c.addComponentListener(new ComponentAdapter() {
    public void componentResized(ComponentEvent e) {
      pcallF(r);
    }
  });
}
static void copyFields(Object x, Object y, String... fields) {
  if (empty(fields)) { // assume we should copy all fields
    Map<String, Object> map = objectToMap(x);
    for (String field : map.keySet())
      setOpt(y, field, map.get(field));
  } else 
    for (String field : fields) {
      Object o = getOpt(x, field);
      if (o != null)
        setOpt(y, field, o);
    }
}
static <A, B> Map<A, B> synchroIdentityHashMap() {
  return synchroMap(new IdentityHashMap());
}
static String toUpper(String s) {
  return s == null ? null : s.toUpperCase();
}

static List<String> toUpper(Collection<String> s) {
  return allToUpper(s);
}
static BufferedImage asBufferedImage(Object o) {
  BufferedImage bi = toBufferedImageOpt(o);
  if (bi == null) throw fail(getClassName(o));
  return bi;
}
static String googleImageSearch(String q) {
  return (String) call(hotwireOnce("#1010230"), "googleImageSearch", q);
}

static List<String> googleImageSearch_multi(String q) {
  return (List<String>) call(hotwireOnce("#1010230"), "googleImageSearch_multi", q);
}

static BufferedImage googleImageSearchFirst(String q) {
  return (BufferedImage) call(hotwireOnce("#1010230"), "googleImageSearchFirst", q);
}
static double sqrt(double x) {
  return Math.sqrt(x);
}
static void markWebsPosted_createMarker() {
  markWebsPosted_createMarker(programID());
}

static void markWebsPosted_createMarker(String progID) {
  createMarkerFile("#1007609", "webs.posted.at." + psI(progID));
}
// changes & returns canvas
static BufferedImage drawImageOnImage(BufferedImage img, BufferedImage canvas, int x, int y) {
  Graphics2D g = createGraphics(canvas);
  g.drawImage(img, x, y, null);
  g.dispose();
  return canvas;
}
static Font sansSerifBold(int fontSize) {
  return new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
}
static <A> A firstInstance(Collection c, Class<A> type) {
  return firstOfType(c, type);
}
static long sqr(long l) {
  return l*l;
}

static double sqr(double d) {
  return d*d;
}
static List onWebsChanged_listeners = synchroList();

static void triggerWebsChanged() {
  pcallF_all(onWebsChanged_listeners);
}

static void onWebsChanged(Object f) {
  setAdd(onWebsChanged_listeners, f);
}
static boolean isURL(String s) {
  return s.startsWith("http://") || s.startsWith("https://");
}
static BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
  return resizeImage(img, newW, newH, Image.SCALE_SMOOTH);
}

static BufferedImage resizeImage(BufferedImage img, int newW, int newH, int scaleType) {
  Image tmp = img.getScaledInstance(newW, newH, scaleType);
  BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
  Graphics2D g2d = dimg.createGraphics();
  g2d.drawImage(tmp, 0, 0, null);
  g2d.dispose();
  return dimg;
}

static BufferedImage resizeImage(BufferedImage img, int newW) {
  int newH = iround(img.getHeight()*(double) newW/img.getWidth());
  return resizeImage(img, newW, newH);
}
static Font sansSerif(int fontSize) {
  return new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
}
static void registerEscape(JFrame frame, final Runnable r) {
  String name = "Escape";
  Action action = abstractAction(name, r);
  JComponent pnl = frame.getRootPane();
  KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
  pnl.getActionMap().put(name, action);
  pnl.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, name);
}
static String snippetImageURL(String snippetID) {
  return snippetImageURL(snippetID, "png");
}

static String snippetImageURL(String snippetID, String contentType) {
  long id = parseSnippetID(snippetID);
  String url;
  if (id == 1000010 || id == 1000012)
    url = "http://tinybrain.de:8080/tb/show-blobimage.php?id=" + id;
  else if (isImageServerSnippet(id))
    url = imageServerLink(id);
  else
    //url = "http://eyeocr.sourceforge.net/filestore/filestore.php?cmd=serve&file=blob_" + id + "&contentType=image/" + contentType;
    url = "https://www.botcompany.de:8443/img/" + id;
  return url;
}
static <A extends JComponent> A bindToComponent(A component, final Runnable onShow, final Runnable onUnShow) {
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

static <A extends JComponent> A bindToComponent(A component, Runnable onShow) {
  return bindToComponent(component, onShow, null);
}
static int stdHash(Object a, String... fields) {
  if (a == null) return 0;
  int hash = getClassName(a).hashCode();
  for (String field : fields)
    hash = hash*2+hashCode(getOpt(a, field));
  return hash;
}
static boolean checkFields(Object x, Object... data) {
  for (int i = 0; i < l(data); i += 2)
    if (neq(getOpt(x, (String) data[i]), data[i+1]))
      return false;
  return true;
}
// better modulo that gives positive numbers always
static int mod(int n, int m) {
  return (n % m + m) % m;
}

static long mod(long n, long m) {
  return (n % m + m) % m;
}
static BigInteger mod(BigInteger n, int m) {
  return n.mod(bigint(m));
}
static BufferedImage imageFromDataURL(String url) {
  String pref = "base64,";
  int i = indexOf(url, pref);
  if (i < 0) return null;
  return decodeImage(base64decode(substring(url, i+l(pref))));
}
static File getCacheProgramDir() {
  return getCacheProgramDir(getProgramID());
}

static File getCacheProgramDir(String snippetID) {
  return new File(userHome(), "JavaX-Caches/" + formatSnippetIDOpt(snippetID));
}


static void drawTextWithOutline(Graphics2D g2, String text, float x, float y, Color fillColor, Color outlineColor) {
  BasicStroke outlineStroke = new BasicStroke(2.0f);

  g2.translate(x, y);

  // remember original settings
  Stroke originalStroke = g2.getStroke();
  RenderingHints originalHints = g2.getRenderingHints();

  // create a glyph vector from your text
  GlyphVector glyphVector = g2.getFont().createGlyphVector(g2.getFontRenderContext(), text);
  // get the shape object
  Shape textShape = glyphVector.getOutline();

  g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

  g2.setColor(outlineColor);
  g2.setStroke(outlineStroke);
  g2.draw(textShape); // draw outline

  g2.setColor(fillColor);
  g2.fill(textShape); // fill the shape

  // reset to original settings after painting
  g2.setStroke(originalStroke);
  g2.setRenderingHints(originalHints);
  
  g2.translate(-x, -y);
}


static float abs(float f) { return Math.abs(f); }
static int abs(int i) { return Math.abs(i); }
static double abs(double d) { return Math.abs(d); }
static BufferedImage decodeImage(byte[] data) { try {
  return ImageIO.read(new ByteArrayInputStream(data));
} catch (Exception __e) { throw rethrow(__e); } }
static <A> A firstOfType(Collection c, Class<A> type) {
  for (Object x : c)
    if (isInstanceX(type, x))
      return (A) x;
  return null;
}
static void failIfOddCount(Object... list) {
  if (odd(l(list)))
    throw fail("Odd list size: " + list);
}
static Set<String> fieldNames(Object o) {
  return allFields(o);
}
static boolean isUpperCase(char c) {
  return Character.isUpperCase(c);
}
static BufferedImage toBufferedImageOpt(Object o) {
  if (o instanceof BufferedImage) return (BufferedImage) o;
  String c = getClassName(o);
  if (eqOneOf(c, "main$BWImage", "main$RGBImage"))
    return (BufferedImage) call(o, "getBufferedImage");
  if (eq(c, "main$PNGFile"))
    return (BufferedImage) call(o, "getImage");
  return null;
}
static List<String> allToUpper(Collection<String> l) {
  List<String> x = new ArrayList(l(l));
  if (l != null) for (String s : l) x.add(upper(s));
  return x;
}
static void pcallF_all(Collection l, Object... args) {
  if (l != null) for (Object f : cloneList(l)) pcallF(f, args);
}
static Graphics2D antiAliasGraphics(BufferedImage img) {
  Graphics2D g = createGraphics(img);
  g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  return g;
}
static int hashCode(Object a) {
  return a == null ? 0 : a.hashCode();
}
static int isqrt(double x) {
  return iround(Math.sqrt(x));
}
static void createMarkerFile(String progID, String name) {
  File f = getProgramFile(progID, name);
  if (fileLength(f) == 0)
    saveTextFile(f, "1");
}
static double pi() {
  return Math.PI;
}
static BufferedImage createBufferedImage(int w, int h, Color color) {
  return newBufferedImage(w, h, color);
}
  static byte[] base64decode(String s) {
    byte[] alphaToInt = base64decode_base64toint;
    int sLen = s.length();
    int numGroups = sLen/4;
    if (4*numGroups != sLen)
      throw new IllegalArgumentException(
        "String length must be a multiple of four.");
    int missingBytesInLastGroup = 0;
    int numFullGroups = numGroups;
    if (sLen != 0) {
      if (s.charAt(sLen-1) == '=') {
        missingBytesInLastGroup++;
        numFullGroups--;
      }
      if (s.charAt(sLen-2) == '=')
        missingBytesInLastGroup++;
    }
    byte[] result = new byte[3*numGroups - missingBytesInLastGroup];

    // Translate all full groups from base64 to byte array elements
    int inCursor = 0, outCursor = 0;
    for (int i=0; i<numFullGroups; i++) {
      int ch0 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      int ch1 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      int ch2 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      int ch3 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));
      result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
      result[outCursor++] = (byte) ((ch2 << 6) | ch3);
    }

    // Translate partial group, if present
    if (missingBytesInLastGroup != 0) {
      int ch0 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      int ch1 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
      result[outCursor++] = (byte) ((ch0 << 2) | (ch1 >> 4));

      if (missingBytesInLastGroup == 1) {
        int ch2 = base64decode_base64toint(s.charAt(inCursor++), alphaToInt);
        result[outCursor++] = (byte) ((ch1 << 4) | (ch2 >> 2));
      }
    }
    // assert inCursor == s.length()-missingBytesInLastGroup;
    // assert outCursor == result.length;
    return result;
  }

  static int base64decode_base64toint(char c, byte[] alphaToInt) {
    int result = alphaToInt[c];
    if (result < 0)
      throw new IllegalArgumentException("Illegal character " + c);
    return result;
  }

  static final byte base64decode_base64toint[] = {
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54,
    55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
    5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
    24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34,
    35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51
  };


static String imageServerLink(String md5OrID) {
  if (possibleMD5(md5OrID))
    return "http://images.botcompany.de/images/md5/" + md5OrID;
  return imageServerLink(parseSnippetID(md5OrID));
}

static String imageServerLink(long id) {
  return "http://images.botcompany.de/images/" + id;
}
static BufferedImage img_getCenterPortion(BufferedImage img, int w, int h) {
  int iw = img.getWidth(), ih = img.getHeight();
  if (iw < w || ih < h) throw fail("Too small");
  int x = (iw-w)/2, y = (ih-h)/2;
  return clipBufferedImage(img, x, y, w, h);
}
// o is either a map already (string->object) or an arbitrary object,
// in which case its fields are converted into a map.
static Map<String, Object> objectToMap(Object o) { try {
  if (o instanceof Map) return (Map) o;
  
  TreeMap<String,Object> map = new TreeMap();
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
  return map;
} catch (Exception __e) { throw rethrow(__e); } }

// same for a collection (convert each element)
static List<Map<String, Object>> objectToMap(Collection l) {
  List x = new ArrayList();
  for (Object o : l)
    x.add(objectToMap(o));
  return x;
}
static BigInteger bigint(String s) {
  return new BigInteger(s);
}

static BigInteger bigint(long l) {
  return BigInteger.valueOf(l);
}


static long fileLength(String path) { return getFileSize(path); }
static long fileLength(File f) { return getFileSize(f); }

static boolean possibleMD5(String s) { return isMD5(s); }
static String upper(String s) {
  return s == null ? null : s.toUpperCase();
}


static class AutoComboBox extends JComboBox<Object> {
  String keyWord[] = {"item1", "item2", "item3"};
  Vector myVector = new Vector();
  
  AutoComboBox() {
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
  
  /**
   * set the item list of the AutoComboBox
   * @param keyWord an String array
   */
  void setKeyWord(String[] keyWord) {
    this.keyWord = keyWord;
    setMyVector();
  }
  
  void setKeyWord(Collection<String> keyWord) {
    setKeyWord(toStringArray(keyWord));
  }
  
  private void setMyVector() {
    copyArrayToVector(keyWord, myVector);
  }
}

static class ComboListener extends KeyAdapter {
  JComboBox cb;
  Vector vector;
  
  ComboListener(JComboBox cb, Vector vector) {
  this.vector = vector;
  this.cb = cb;}
  
  public void /*keyReleased*/keyPressed(KeyEvent key) {
    if (key.getKeyCode() == KeyEvent.VK_ENTER) return;
    
    if (key.getKeyCode() == KeyEvent.VK_ESCAPE) { cb.hidePopup(); return; }
    
    if (key.getKeyCode() == KeyEvent.VK_TAB /*&& key.getModifiers() == 0*/) {
      print("Have tab event (modifiers=" + key.getModifiers() + ")");
      if ((key.getModifiers() & ActionEvent.SHIFT_MASK) == 0 && cb.isPopupVisible()) {
        cb.setSelectedIndex(0); // accept item
        cb.hidePopup();
      } else // standard tab behavior
        swing_standardTabBehavior(key);
        
      return;
    }
    
    JTextField tf = (JTextField) ( cb.getEditor().getEditorComponent());
    
    if (tf.getCaretPosition() != l(tf.getText())) return;
    
    String text = ((JTextField) key.getSource()).getText();

    Vector list = getFilteredList(text);
    if (nempty(list)) {
      cb.setModel(new DefaultComboBoxModel(list));
      cb.setSelectedIndex(-1);
      tf.setText(text); // necessary?
      cb.showPopup();
    } else
      cb.hidePopup();
  }

  public Vector getFilteredList(String text) {
    return new Vector(scoredSearch(text, vector));
  }
}static class RemoteDB {
  DialogIO db;
  String name;
  
  // s = bot name or snippet ID
  RemoteDB(String s) {
    this(s, false);
  }
  
  RemoteDB(String s, boolean autoStart) {
    name = s;
    if (isSnippetID(s)) name = dbBotName(s);
    db = findBot(name);
    if (db == null)
      if (autoStart) {
        nohupJavax(fsI(s));
        waitForBotStartUp(name);
        assertNotNull("Weird problem", db = findBot(s));
      } else
        throw fail("DB " + s + " not running");
  }

  boolean functional() { return db != null; } // now always true
  
  List<RC> list() { return adopt((List<RC>) rpc(db, "xlist")); }
  List<RC> list(String className) { return adopt((List<RC>) rpc(db, "xlist", className)); }
  List<RC> xlist() { return list(); }
  List<RC> xlist(String className) { return list(className); }
  
  // adopt is an internal method
  List<RC> adopt(List<RC> l) {
    if (l != null) for (RC rc : l) adopt(rc);
    return l;
  }
  
  RC adopt(RC rc) { if (rc != null) rc.db = this; return rc; }
  
  Object adopt(Object o) {
    if (o instanceof RC) return adopt((RC) o);
    return o;
  }
  
  String xclass(RC o) {
    return (String) rpc(db, "xclass", o);
  }
  
  Object xget(RC o, String field) {
    return adopt(rpc(db, "xget", o, field));
  }
  
  String xS(RC o, String field) {
    return (String) xget(o, field);
  }
  
  RC xgetref(RC o, String field) {
    return adopt((RC) xget(o, field));
  }
  
  void xset(RC o, String field, Object value) {
    rpc(db, "xset", o, field, value);
  }
  
  RC uniq(String className) {
    RC ref = first(list(className));
    if (ref == null)
      ref = xnew(className);
    return ref;
  }
  RC xuniq(String className) { return uniq(className); }
  
  RC xnew(String className, Object... values) {
    return adopt((RC) rpc(db, "xnew", className, values));
  }
  
  void xdelete(RC o) {
    rpc(db, "xdelete", o);
  }
  
  void xdelete(List<RC> l) {
    rpc(db, "xdelete", l);
  }

  void close() {
    if (db != null)
      db.close();
  }
  
  String fullgrab() { return (String) rpc(db, "xfullgrab"); }
  String xfullgrab() { return fullgrab(); }
  
  void xshutdown() { rpc(db, "xshutdown"); }
  
  long xchangeCount() { return (long) rpc(db, "xchangeCount"); }
  int xcount() { return (int) rpc(db, "xcount"); }

  void reconnect() {
    close();
    db = findBot(name);
  }
  
  RC rc(long id) { return new RC(this, id); }
}// a Lisp-like form
static class Lisp implements Iterable<Lisp> {
  String head;
  List<Lisp> args;
  
  // O more; // additional info, user-defined
  
  Lisp() {}
  Lisp(String head) { this.head = _compactString(head); }
  Lisp(String head, Lisp... args) {
  this.head = head;
    argsForEdit().addAll(asList(args));
  }
  Lisp(String head, Collection args) {
    this.head = _compactString(head);
    for (Object arg : args) add(arg);
  }
  
  List<Lisp> argsForEdit() {
    return args == null ? (args = new ArrayList()) : args;
  }
  
  // INEFFICIENT
  public String toString() {
    if (empty())
      return quoteIfNotIdentifierOrInteger(head);
    List<String> bla = new ArrayList();
    for (Lisp a : args)
      bla.add(a.toString());
    String inner = join(", ", bla);
    if (head.equals(""))
      return "{" + inner + "}"; // list
    else
      return quoteIfNotIdentifier(head) + "(" + inner + ")";
  }

  String raw() {
    if (!isEmpty ()) throw fail("not raw: " + this);
    return head;
  }
  
  Lisp add(Lisp l) {
    argsForEdit().add(l);
    return this;
  }
  
  Lisp add(String s) {
    argsForEdit().add(new Lisp(s));
    return this;
  }
  
  Lisp add(Object o) {
    if (o instanceof Lisp) add((Lisp) o);
    else if (o instanceof String) add((String) o);
    else throw fail("Bad argument type: " + structure(o));
    return this;
  }
  
  int size() {
    return l(args);
  }
  
  boolean empty() { return main.empty(args); }
  boolean isEmpty() { return main.empty(args); }
  boolean isLeaf() { return main.empty(args); }
  
  Lisp get(int i) {
    return main.get(args, i);
  }
  
  String getString(int i) { Lisp a = get(i); return a == null ? null : a.head; }
  
  String s(int i) { return getString(i); }
  String rawOrNull(int i) {
    Lisp a = get(i); return a != null && a.isLeaf() ? a.head : null;
  }
  String raw(int i) { return assertNotNull(rawOrNull(i)); }
  boolean isLeaf(int i) { return rawOrNull(i) != null; }
  
  boolean isA(String head) {
    return eq(head, this.head);
  }

  boolean is(String head, int size) {
    return isA(head) && size() == size;
  }
  
  boolean is(String head) { return isA(head); }
  boolean headIs(String head) { return isA(head); }
  
  boolean is(String... heads) {
    return asList(heads).contains(head);
  }
  
  // check head for one of these (ignore case)
  boolean isic(String... heads) {
    return containsIgnoreCase(heads, head);
  }
  
  public Iterator<Lisp> iterator() {
    return main.iterator(args);
  }
  
  Lisp subList(int fromIndex, int toIndex) {
    Lisp l = new Lisp(head);
    l.argsForEdit().addAll(args.subList(fromIndex, toIndex)); // better to copy here I guess - safe
    return l;
  }

  public boolean equals(Object o) {
    if (o == null || o.getClass() != Lisp.class) return false;
    Lisp l = (Lisp) ( o);
    return eq(head, l.head) && (isLeaf() ? l.isLeaf()
      : l.args != null && eq(args, l.args));
  }
  
  public int hashCode() {
    return head.hashCode() + main.hashCode(args);
  }
  
  Lisp addAll(List args) {
    for (Object arg : args) add(arg);
    return this;
  }
  
  String unquoted() { return unquote(raw()); }
  String unq() { return unquoted(); }
  String unq(int i) { return get(i).unq(); }
  
  // heads of arguments
  List<String> heads() { return collect(args, "head"); }
}


static Object cget(Object c, String field) {
  Object o = getOpt(c, field);
  if (o instanceof Concept.Ref) return ((Concept.Ref) o).get();
  return o;
}
static Collection<Concept> allConcepts() {
  return mainConcepts.allConcepts();
}

static Collection<Concept> allConcepts(Concepts concepts) {
  return concepts.allConcepts();
}

static void close(AutoCloseable c) { try {
  if (c != null) c.close();
} catch (Exception __e) { throw rethrow(__e); } }
// firstDelay = delay
static FixedRateTimer doEvery_daemon(long delay, final Object r) {
  return doEvery_daemon(delay, delay, r);
}

static FixedRateTimer doEvery_daemon(long delay, long firstDelay, final Object r) {
  FixedRateTimer timer = new FixedRateTimer(true);
  timer.scheduleAtFixedRate(smartTimerTask(r, timer, delay), firstDelay, delay);
  return timer;
}

static FixedRateTimer doEvery_daemon(double delaySeconds, final Object r) {
  return doEvery_daemon(toMS(delaySeconds), r);
}
static String loadConceptsStructure(String progID) {
  return loadTextFilePossiblyGZipped(getProgramFile(progID, "concepts.structure"));
}

static String loadConceptsStructure() {
  return loadConceptsStructure(dbProgramID());
}
static long toK(long l) {
  return (l+1023)/1024;
}
static long toM(long l) {
  return (l+1024*1024-1)/(1024*1024);
}

static String toM(long l, int digits) {
  return formatDouble(toM_double(l), digits);
}
static void clearConcepts() {
  mainConcepts.clearConcepts();
}

static void clearConcepts(Concepts concepts) {
  concepts.clearConcepts();
}
static SortedMap synchroTreeMap() {
  return Collections.synchronizedSortedMap(new TreeMap());
}

static boolean hasType(Collection c, Class type) {
  for (Object x : c)
    if (isInstanceX(type, x))
      return true;
  return false;
}
static <A extends Concept> A uniq(Class<A> c, Object... params) {
  return uniqueConcept(c, params);
}

static <A extends Concept> A uniq(Concepts cc, Class<A> c, Object... params) {
  return uniqueConcept(cc, c, params);
}
static String javaTokWordWrap(String s) {
  int cols = 120, col = 0;
  List<String> tok = javaTok(s);
  for (int i = 0; i < l(tok); i++) {
    String t = tok.get(i);
    if (odd(i) && col >= cols && !containsNewLine(t))
      tok.set(i, t += "\n");
    int idx = t.lastIndexOf('\n');
    if (idx >= 0) col = l(t)-(idx+1);
    else col += l(t);
  }
  return join(tok);
}
static Object rpc(String botName, String method, Object... args) {
  return unstructure_debug(matchOK2OrFail(
    sendToLocalBot(botName, rpc_makeCall(method, args))));
}

static Object rpc(DialogIO bot, String method, Object... args) {
  return unstructure_debug(matchOK2OrFail(
    bot.ask(rpc_makeCall(method, args))));
}

static String rpc_makeCall(String method, Object... args) {
  if (empty(args))
    return "call " + method;
  return format("call *", concatLists((List) ll(method), asList(args)));
}
static void copyArrayToVector(Object[] array, Vector v) {
  v.clear();
  v.addAll(toList(array));
}
static String quoteIfNotIdentifier(String s) {
  if (s == null) return null;
  return isJavaIdentifier(s) ? s : quote(s);
}
static <A> void addAll(Collection<A> c, Iterable<A> b) {
  if (c != null && b != null) for (A a : b) c.add(a);
}

static <A> void addAll(Collection<A> c, Collection<A> b) {
  if (c != null && b != null) c.addAll(b);
}

static <A> void addAll(Collection<A> c, A... b) {
  if (c != null) c.addAll(Arrays.asList(b));
}
static boolean bareDBMode_on;

static void bareDBMode() {
  bareDBMode(null); // default autoSaveInterval
}

static void bareDBMode(Integer autoSaveInterval) {
  bareDBMode_on = true;
  conceptsAndBot(autoSaveInterval);
}
static Str concept(String name) {
  for (Str s : list(Str.class))
    if (eqic(s.name, name) || containsIgnoreCase(s.otherNames, name))
      return s;
  return new Str(name);
}
static int hours() {
  return hours(Calendar.getInstance());
}

static int hours(Calendar c) {
  return c.get(Calendar.HOUR_OF_DAY);
}
static String _compactString(String s) {
  return s;
}
static <A extends Concept> A findBackRef(Concept c, Class<A> type) {
  for (Concept.Ref r : c.backRefs)
    if (instanceOf(r.concept(), type))
      return (A) r.concept();
  return null;
}

static <A extends Concept> A findBackRef(Class<A> type, Concept c) {
  return findBackRef(c, type);
}
static <A> List<A> removeDyn(List<A> l, A a) {
  if (l == null) return null;
  l.remove(a);
  return empty(l) ? null : l;
}
static String formatInt(int i, int digits) {
  return padLeft(str(i), '0', digits);
}

static String formatInt(long l, int digits) {
  return padLeft(str(l), '0', digits);
}
static <A> List<A> filterByType(Collection c, Class<A> type) {
  List<A> l = new ArrayList();
  for (Object x : c)
    if (isInstanceX(type, x))
      l.add((A) x);
  return l;
}

static <A> List<A> filterByType(Object[] c, Class<A> type) {
  return filterByType(asList(c), type);
}

static int done_minPrint = 10;

static long done(long startTime, String desc) {
  long time = now()-startTime;
  if (time >= done_minPrint)
    print(desc + " [" + time + " ms]");
  return time;
}

static long done(String desc, long startTime) {
  return done(startTime, desc);
}

static long done(long startTime) {
  return done(startTime, "");
}
static void readLocally(String progID, String varNames) {
  readLocally2(mc(), progID, varNames);
}

static void readLocally(String varNames) {
  readLocally2(mc(), programID(), varNames);
}

static void readLocally2(Object obj, String varNames) {
  readLocally2(obj, programID(), varNames);
}

static int readLocally_stringLength;

static ThreadLocal<Boolean> readLocally2_allDynamic = new ThreadLocal();

// read a string variable from standard storage
// does not overwrite variable contents if there is no file
static void readLocally2(Object obj, String progID, String varNames) { try {
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
        if (!structureGZFile.isFile()) return;
        //value = loadGZTextFile(structureGZFile);
        InputStream fis = new FileInputStream(structureGZFile);
        try {
          GZIPInputStream gis = new GZIPInputStream(fis);
          InputStreamReader reader = new InputStreamReader(gis, "UTF-8");
          BufferedReader bufferedReader = new BufferedReader(reader);
          //O o = unstructure_reader(bufferedReader);
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
} catch (Exception __e) { throw rethrow(__e); } }

static void readLocally_set(Object c, String varName, Object value) {
  Object oldValue = get(c, varName);
  if (oldValue instanceof List && !(oldValue instanceof ArrayList) && value != null) {
    // Assume it's a synchroList.
    value = synchroList((List) value);
  }
  set(c, varName, value);
}

static String ymd() {
  return year() + formatInt(month(), 2) + formatInt(dayOfMonth(), 2);
}
static void saveGZStructureToFile(String file, Object o) {
  saveGZStructureToFile(getProgramFile(file), o);
}
  
static void saveGZStructureToFile(File file, Object o) { try {
  File parentFile = file.getParentFile();
  if (parentFile != null)
    parentFile.mkdirs();
  File tempFile = tempFileFor(file);
  if (tempFile.exists()) try {
    String saveName = tempFile.getPath() + ".saved." + now();
    copyFile(tempFile, new File(saveName));
  } catch (Throwable e) { printStackTrace(e); }
  
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
} catch (Exception __e) { throw rethrow(__e); } }
static boolean checkConceptFields(Concept x, Object... data) {
  for (int i = 0; i < l(data); i += 2)
    if (neq(cget(x, (String) data[i]), deref(data[i+1])))
      return false;
  return true;
}
static void saveLocally(String variableName) {
  saveLocally(programID(), variableName);
}

static void saveLocally(String progID, String variableName) {
  saveLocally2(mc(), progID, variableName);
}

static void saveLocally2(Object obj, String variableName) {
  saveLocally2(obj, programID(), variableName);
}

static void saveLocally2(Object obj, String progID, String variableName) {
  Lock _lock_1804 = saveLock(); lock(_lock_1804); try {
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
} finally { unlock(_lock_1804); } }
static List<String> scoredSearch(String query, Iterable<String> data) {
  Map<String,Integer> scores = new HashMap();
  List<String> prepared = scoredSearch_prepare(query);
  for (String s : data) {
    int score = scoredSearch_score(s, prepared);
    if (score != 0)
      scores.put(s, score);
  }
  return keysSortedByValuesDesc(scores);
}
static RemoteDB connectToDBOpt(String dbNameOrID) { try {
  return new RemoteDB(dbNameOrID);
} catch (Throwable __e) { return null; } }
static <A> List<A> addDyn(List<A> l, A a) {
  if (l == null) l = new ArrayList();
  l.add(a);
  return l;
}
static List map(Iterable l, Object f) {
  return map(f, l);
}

static List map(Object f, Iterable l) {
  List x = emptyList(l);
  if (l != null) for (Object o : l)
    x.add(callF(f, o));
  return x;
}


  static List map(F1 f, Iterable l) {
    List x = emptyList(l);
    if (l != null) for (Object o : l)
      x.add(callF(f, o));
    return x;
  }


static List map(Object f, Object[] l) { return map(f, asList(l)); }
static List map(Object[] l, Object f) { return map(f, l); }

static List map(Object f, Map map) {
  return map(map, f);
}

// map: func(key, value) -> list element
static List map(Map map, Object f) {
  List x = new ArrayList();
  if (map != null) for (Object _e : map.entrySet()) {
    Map.Entry e = (Map.Entry) _e;
    x.add(callF(f, e.getKey(), e.getValue()));
  }
  return x;
}
static void change() {
  //mainConcepts.allChanged();
  // safe version for now cause function is sometimes included unnecessarily (e.g. by EGDiff)
  callOpt(getOptMC("mainConcepts"), "allChanged");
}
static <A> List<A> filterByDynamicType(Collection<A> c, String type) {
  List<A> l = new ArrayList();
  for (A x : c)
    if (eq(dynamicClassName(x), type))
      l.add(x);
  return l;
}
static long waitForBotStartUp_timeoutSeconds = 60;

// returns address or fails
static String waitForBotStartUp(String botName) {
  for (int i = 0; i < waitForBotStartUp_timeoutSeconds; i++) {
    sleepSeconds(i == 0 ? 0 : 1);
    String addr = getBotAddress(botName);
    if (addr != null)
      return addr;
  }
  throw fail("Bot not found: " + quote(botName));
}
static <A, B> Map<A, B> cloneMap(Map<A, B> map) {
  if (map == null) return litmap();
  // assume mutex is equal to collection
  synchronized(map) {
    return map instanceof TreeMap ? new TreeMap((TreeMap) map) // copies comparator
      : map instanceof LinkedHashMap ? new LinkedHashMap(map)
      : new HashMap(map);
  }
}
static Lock dbLock() {
  return mainConcepts.lock;
}
static void swing_standardTabBehavior(KeyEvent key) {
  if ((key.getModifiers() & ActionEvent.SHIFT_MASK) != 0)
    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
  else
    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
}

static boolean hasConcept(Class<? extends Concept> c, Object... params) {
  return findConceptWhere(c, params) != null;
}
static <A extends Concept> int countConcepts(Class<A> c, Object... params) {
  return mainConcepts.countConcepts(c, params);
}

static int countConcepts() {
  return mainConcepts.countConcepts();
}

static int countConcepts(String className) {
  return mainConcepts.countConcepts(className);
}

static <A extends Concept> int countConcepts(Concepts concepts, String className) {
  return concepts.countConcepts(className);
}
static <A, B> List<B> getAll(Map<A, B> map, Collection<A> l) {
  return lookupAllOpt(map, l);
}

static <A, B> List<B> getAll(Collection<A> l, Map<A, B> map) {
  return lookupAllOpt(map, l);
}


static int year() {
  return Calendar.getInstance().get(Calendar.YEAR);
}
static <A extends Concept> A uniqueConcept(Class<A> c, Object... params) {
  return uniqueConcept(mainConcepts, c, params);
}

static <A extends Concept> A uniqueConcept(Concepts cc, Class<A> c, Object... params) {
  params = expandParams(c, params);
  A x = findConceptWhere(cc, c, params);
  if (x == null) {
    x = unlisted(c);
    csetAll(x, params);
    cc.register(x);
  }
  return x;
}
static Lock saveLock_lock = fairLock();

static Lock saveLock() {
  return saveLock_lock;
}
static int scoredSearch_score(String s, List<String> words) {
  int score = 0;
  for (String word : words)
    score += scoredSearch_score_single(s, word);
  return score;
}

static int scoredSearch_score(String s, String query) {
  return scoredSearch_score(s, scoredSearch_prepare(query));
}

static List<String> scoredSearch_prepare(String query) {
  return splitAtSpace(query);
}
static <A extends Concept> A findConceptWhere(Class<A> c, Object... params) {
  return findConceptWhere(mainConcepts, c, params);
}

static <A extends Concept> A findConceptWhere(Concepts concepts, Class<A> c, Object... params) {
  params = expandParams(c, params);
  
  // indexed
  if (concepts.fieldIndices != null)
    for (int i = 0; i < l(params); i += 2) {
      IFieldIndex<A, Object> index = concepts.getFieldIndex(c, (String) params[i]);
      if (index != null) {
        for (A x : index.getAll(params[i+1]))
          if (checkConceptFields(x, params)) return x;
        return null;
      }
    }
    
  // table scan
  for (A x : concepts.list(c)) if (checkConceptFields(x, params)) return x;
  return null;
}

static Concept findConceptWhere(Concepts concepts, String c, Object... params) {
  for (Concept x : concepts.list(c)) if (checkConceptFields(x, params)) return x;
  return null;
}

static String dynamicClassName(Object o) {
  if (o instanceof DynamicObject && ((DynamicObject) o).className != null)
    return "main$" + ((DynamicObject) o).className;
  return className(o);
}
static <A, B> List<B> lookupAllOpt(Map<A, B> map, Collection<A> l) {
  List<B> out = new ArrayList();
  if (l != null) for (A a : l)
    addIfNotNull(out, map.get(a));
  return out; 
}

static <A, B> List<B> lookupAllOpt(Collection<A> l, Map<A, B> map) {
  return lookupAllOpt(map, l);
}
static Object getOptMC(String field) {
  return getOpt(mc(), field);
}
static String formatDouble(double d, int digits) {
  String format = "0." + rep(digits, '#');
  return new java.text.DecimalFormat(format, new java.text.DecimalFormatSymbols(Locale.ENGLISH)).format(d);
}

// r may return false to cancel timer
static TimerTask smartTimerTask(Object r, java.util.Timer timer, long delay) {
  return new SmartTimerTask(r, timer, delay);
}

static  class SmartTimerTask extends TimerTask {
  Object r;
  java.util.Timer timer;
  long delay;
  SmartTimerTask() {}
  SmartTimerTask(Object r, java.util.Timer timer, long delay) {
  this.delay = delay;
  this.timer = timer;
  this.r = r;}
  public String toString() { return "SmartTimerTask(" + r + ", " + timer + ", " + delay + ")"; }
  long lastRun;
  
  public void run() {
    if (!licensed())
      timer.cancel();
    else {
      lastRun = fixTimestamp(lastRun);
      long now = now();
      if (now >= lastRun + delay*0.9) {
        lastRun = now;
        if (eq(false, pcallF(r)))
          timer.cancel();
      }
    }
  }
}
static <A, B> List<A> keysSortedByValuesDesc(final Map<A, B> map) {
  List<A> l = new ArrayList(map.keySet());
  sort(l, mapComparatorDesc(map));
  return l;
}
static boolean containsNewLine(String s) {
  return contains(s, '\n'); // screw \r, nobody needs it
}
static Object unstructure_debug(String s) {
  try {
    return unstructure(s);
  } catch (Throwable _e) {
    print("Was unstructuring: " + s);
  
throw rethrow(_e); }
}
static int month() {
  return Calendar.getInstance().get(Calendar.MONTH)+1;
}
static String getBotAddress(String bot) {
  List<ScannedBot> l = fullBotScan(bot);
  return empty(l) ? null : first(l).address;
}
static String padLeft(String s, char c, int n) {
  return rep(c, n-l(s)) + s;
}

// default to space
static String padLeft(String s, int n) {
  return padLeft(s, ' ', n);
}
static String dbProgramID() {
  return getDBProgramID();
}
static String matchOK2OrFail(String s) {
  Matches m = new Matches();
  if (swic(s, "ok "))
    return substring(s, 3);
  else if (eqic(s, "ok"))
    return "ok";
  else
    throw fail(s);
}
static String sendToLocalBot(String bot, String text, Object... args) {
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

static String sendToLocalBot(int port, String text, Object... args) {
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
static int dayOfMonth() {
  return days();
}
static double toM_double(long l) {
  return l/(1024*1024.0);
}
static File tempFileFor(File f) {
  return new File(f.getPath() + "_temp");
}


static int days() {
  return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
}
static String rep(int n, char c) {
  return repeat(c, n);
}

static String rep(char c, int n) {
  return repeat(c, n);
}

static <A> List<A> rep(A a, int n) {
  return repeat(a, n);
}

static <A> List<A> rep(int n, A a) {
  return repeat(n, a);
}

static <A> void addIfNotNull(Collection<A> l, A a) {
  if (a != null) l.add(a);
}


static List<String> splitAtSpace(String s) {
  return asList(s.split("\\s+"));
}
static long fixTimestamp(long timestamp) {
  return timestamp > now() ? 0 : timestamp;
}
static class ScannedBot {
  String helloString;
  String address;
  
  ScannedBot(String helloString, String address) {
  this.address = address;
  this.helloString = helloString;}
  ScannedBot() {}
}

static List<ScannedBot> fullBotScan() {
  return fullBotScan("");
}

static List<ScannedBot> fullBotScan(String searchPattern) {
  List<ScannedBot> bots = new ArrayList();
  for (ProgramScan.Program p : quickBotScan()) {
    String botName = firstPartOfHelloString(p.helloString);
    boolean isVM = startsWithIgnoreCase(p.helloString, "This is a JavaX VM.");
    boolean shouldRecurse = swic(botName, "Multi-Port") || isVM;
    
    if (swic(botName, searchPattern)) bots.add(new ScannedBot(botName, "" + p.port));

    if (shouldRecurse) try {
      Map<Number, String> subBots = (Map) unstructure(sendToLocalBotQuietly(p.port, "list bots"));
      for (Number vport : subBots.keySet()) {
        botName = subBots.get(vport);
        if (swic(botName, searchPattern)) 
          bots.add(new ScannedBot(botName, p.port + "/" + vport));
      }
    } catch (Exception e) { e.printStackTrace(); }
  }
  return bots;
}
static <A, B> Comparator<A> mapComparatorDesc(final Map<A, B> map) {
  return new Comparator<A>() {
    public int compare(A a, A b) {
      return cmp(map.get(b), map.get(a));
    }
  };
}
static <T> void sort(T[] a, Comparator<? super T> c) {
  Arrays.sort(a, c);
}

static <T> void sort(T[] a) {
  Arrays.sort(a);
}

static <T> void sort(List<T> a, Comparator<? super T> c) {
  Collections.sort(a, c);
}

static void sort(List a) {
  Collections.sort(a);
}
static int scoredSearch_score_single(String s, String query) {
  int i = indexOfIgnoreCase(s, query);
  if (i < 0) return 0;
  if (i > 0) return 1;
  return l(s) == l(query) ? 3 : 2;
}
// make concept instance that is not connected to DB
static <A extends Concept> A unlisted(Class<A> c, Object... args) {
  concepts_unlisted.set(true);
  try {
    return nuObject(c, args);
  } finally {
    concepts_unlisted.set(null);
  }
}


static class Str extends Concept {
  String name;
  List<String> otherNames = new ArrayList();
  
  Str() {}
  Str(String name) {
  this.name = name;}
  
  public String toString() { return name; }
}static class FixedRateTimer extends java.util.Timer {
  FixedRateTimer() { this(false); }
  FixedRateTimer(boolean daemon) {
    super(daemon);
    _registerTimer(this);
  }
  
  List<Entry> entries = synchroList();
  
  static  class Entry {
  TimerTask task;
  long firstTime;
  long period;
  Entry() {}
  Entry(TimerTask task, long firstTime, long period) {
  this.period = period;
  this.firstTime = firstTime;
  this.task = task;}
  public String toString() { return "Entry(" + task + ", " + firstTime + ", " + period + ")"; }}
  
  // Note: not all methods overridden; only use these once
  
  public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
    entries.add(new Entry(task, now()+delay, period));
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

static Set<java.util.Timer> _registerTimer_list = newWeakHashSet();

static void _registerTimer(java.util.Timer timer) {
  _registerTimer_list.add(timer);
}


static <A> Set<A> newWeakHashSet() {
  return synchroWeakHashSet();
}


static <A> Set<A> synchroWeakHashSet() {
  return new WeakHashSet();
}


static class WeakHashSet<A> extends AbstractSet<A> {
  Map<A, Boolean> map = newWeakHashMap();
  
  public int size() { return map.size(); }
  public Iterator<A> iterator() { return keys(map).iterator(); }
  public boolean contains(Object o) { return map.containsKey(o); }
  
  public boolean add(A a) {
    return map.put(a, Boolean.TRUE) != null;
  }
  public boolean remove(Object o) { return map.remove(o) != null; }
  
  Object mutex() { return collectionMutex(map); }
}
}
