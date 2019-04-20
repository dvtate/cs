#include <iostream>
#include <vector>
#include <stack>

class Node {
public:

	// to neighbors
	std::vector<Node*> edges;

	// to make it unique and identifiable
	std::string label;

	// to see if it is discovered or not
	enum Color {
		WHITE,
		GREY,
		BLACK,
	} color;


	Node (std::string label):
		label(label), color(WHITE) 
	{}

};

// traverses graph, coloring all vertices black
void dfsDiscover(const std::vector<Node*>& graph, Node* v) {
	if (v->color == Node::BLACK)
		return;
	std::cout <<"Discovered: " <<v->label <<std::endl;
	v->color = Node::BLACK;

	for (Node* n : v->edges)
			dfsDiscover(graph, n);
}

// use DFS to discover the graph, if there are any undiscovered elems remaining then it's not connected
const Node* disconnected(const std::vector<Node*>& graph) {
	dfsDiscover(graph, graph[0]);
	for (const Node* n : graph)
		if (n->color == Node::WHITE)
			return n;
	return nullptr;
}


std::vector<Node*> eulerianCycle(const std::vector<Node*>& graph) {
	std::stack<Node*> s;
	std::vector<Node*> ret;
	Node* v = graph[0];

	while (v->edges.size() || s.size()) 
		// runs O(V+E) times
		if (v->edges.empty()) { // runs O(V) times
			// this edge is a part of the cycle
			ret.emplace_back(v);
			v = s.top();
			s.pop();

		} else { // runs O(E) times
			// follow edge
			Node* new_v = v->edges[0];

			// remove both parts of edge (undirectional)
			unsigned short i = 0;
			for (; i < new_v->edges.size(); i++)
				if (new_v->edges[i] == v) {
					new_v->edges.erase(new_v->edges.begin() + i);
					break;
				}
			v->edges.erase(v->edges.begin() + 0);
			
			// push v onto stack
			s.push(v);
			// follow edge
			v = new_v;
		}
	return ret;
}

int main() {
	Node a("a"), b("b"), c("c"), d("d"), e("e"), f("f"), g("g"), h("h");
	a.edges = { &b, &c };
	b.edges = { &a, &d };
	c.edges = { &a, &d, &f, &e };
	d.edges = { &b, &c };
	e.edges = { &c, &g };
	f.edges = { &c, &g };
	g.edges = { &e, &f };
	std::vector<Node*> graph = { &a, &b, &c, &d, &e, &f, &g };

	const Node* dc = disconnected(graph);
	if (dc) {
		std::cout <<"No Eulerian Cycle: Graph isn't connected to point " <<dc->label <<std::endl;
		return 0;
	}
	std::cout <<"Graph is connected\n";

	for (const Node* n : graph)
		if (n->edges.size() % 2 != 0) {
			std::cout <<"No Eulerian Cycle: Vertex " <<n->label <<" has odd degree.\n";
			return 0;
		}

	std::cout <<"All edges have even degrees\n";


	std::cout <<"cycle: ";
	for (Node* n : eulerianCycle(graph))
		std::cout <<n->label <<", ";
	std::cout <<"... repeat\n";

}
