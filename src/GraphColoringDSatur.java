import java.util.HashSet;
import java.util.Set;

public class GraphColoringDSatur {
    private final Graph graph;
    private int[] color;
    private int[] coloringOrder;
    private int colorCount;
    private boolean colored;

    private static final String[] LABELS = {
        "AC", "AL", "AM", "AP", "BA", "CE", "DF",
        "ES", "GO", "MA", "MG", "MS", "MT", "PA",
        "PB", "PE", "PI", "PR", "RJ", "RN", "RO",
        "RR", "RS", "SC", "SE", "SP", "TO"
    };

    public GraphColoringDSatur(Graph graph) {
        if (graph == null) throw new IllegalArgumentException("graph nao pode ser nulo");
        this.graph = graph;
        this.color = new int[graph.V()];
        this.coloringOrder = new int[graph.V()];
        this.colorCount = 0;
        this.colored = false;
        for (int i = 0; i < graph.V(); i++) color[i] = -1;
    }

    public Graph getGraph() {
        return graph;
    }

    @SuppressWarnings("unchecked")
    public void color() {
        int V = graph.V();
        int[] saturation = new int[V];
        Set<Integer>[] neighborColors = new HashSet[V];
        boolean[] done = new boolean[V];

        for (int i = 0; i < V; i++) neighborColors[i] = new HashSet<>();

        // Passo 1: colorir o vértice de maior grau com cor 1
        int start = 0;
        for (int v = 1; v < V; v++)
            if (graph.degree(v) > graph.degree(start)) start = v;

        color[start] = 1;
        done[start] = true;
        coloringOrder[0] = start;
        colorCount = 1;

        for (int w : graph.adj(start)) {
            neighborColors[w].add(1);
            saturation[w] = neighborColors[w].size();
        }

        // Passo 4: colorir os demais vértices
        for (int step = 1; step < V; step++) {
            // Escolher vértice não colorido com maior saturação; empate → maior grau
            int best = -1;
            for (int v = 0; v < V; v++) {
                if (done[v]) continue;
                if (best == -1
                        || saturation[v] > saturation[best]
                        || (saturation[v] == saturation[best] && graph.degree(v) > graph.degree(best))) {
                    best = v;
                }
            }

            // Menor cor viável para best
            int k = 1;
            while (neighborColors[best].contains(k)) k++;

            color[best] = k;
            done[best] = true;
            coloringOrder[step] = best;
            if (k > colorCount) colorCount = k;

            // Atualizar saturação dos vizinhos não coloridos
            for (int w : graph.adj(best)) {
                if (!done[w]) {
                    neighborColors[w].add(k);
                    saturation[w] = neighborColors[w].size();
                }
            }
        }

        colored = true;
    }

    public int getColor(int vertex) {
        if (!colored) throw new IllegalStateException("Execute color() antes de consultar cores");
        return color[vertex];
    }

    public int getColorCount() {
        if (!colored) throw new IllegalStateException("Execute color() antes de consultar o total");
        return colorCount;
    }

    public int[] getColoringOrder() {
        if (!colored) throw new IllegalStateException("Execute color() antes de consultar a ordem");
        return coloringOrder.clone();
    }

    public boolean isValidColoring() {
        if (!colored) throw new IllegalStateException("Execute color() antes de validar");
        for (int v = 0; v < graph.V(); v++)
            for (int w : graph.adj(v))
                if (color[v] == color[w]) return false;
        return true;
    }

    public String getLabel(int vertex) {
        if (vertex >= 0 && vertex < LABELS.length) return LABELS[vertex];
        return String.valueOf(vertex);
    }
}
