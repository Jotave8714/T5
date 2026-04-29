public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException(
                    "informe o arquivo de entrada. Ex.: java Main ../dados/brasil.txt"
            );
        }

        In in = new In(args[0]);
        Graph graph = new Graph(in);
        GraphColoringDSatur dsatur = new GraphColoringDSatur(graph);

        StdOut.println("=== Lista de Adjacência ===");
        StdOut.println(graph);

        dsatur.color();

        int[] order = dsatur.getColoringOrder();
        StdOut.println("=== Coloração DSatur ===");
        StdOut.print("Ordem de coloração: ");
        for (int i = 0; i < order.length; i++) {
            if (i > 0) StdOut.print(", ");
            StdOut.print(dsatur.getLabel(order[i]) + "(" + order[i] + ")");
        }
        StdOut.println();
        StdOut.println();

        StdOut.println("Cor de cada estado:");
        for (int v = 0; v < graph.V(); v++) {
            StdOut.printf("  %s (%2d): cor %d%n", dsatur.getLabel(v), v, dsatur.getColor(v));
        }
        StdOut.println();

        StdOut.println("Total de cores utilizadas: " + dsatur.getColorCount());
        StdOut.println("Coloração válida: " + dsatur.isValidColoring());
    }
}
