var nodeFocus = false;

//Set margins and sizes
var margin = {
    top: 20,
    bottom: 50,
    right: 30,
    left: 50
};
var width = 1060 - margin.left - margin.right;
var height = 700 - margin.top - margin.bottom;
//Load Color Scale
var c10 = d3.scale.category10();

//Load External Data
d3.json("/appgraph", function(error, dataset) {
    if (error) throw error;

        //Start: Working dropdown box
        var data3 =[dataset.nodes];
        var nodesForCombo = dataset.nodes;
//        sortAndUpdate(nodesForCombo, true);
        console.log(data3);
        console.log(nodesForCombo);
        var select = d3.select("#appDropdown")
            .insert("select", "svg")
            .attr('class','select')
            .on('change',onchange)

        var options = select
            .selectAll("option")
            .data(nodesForCombo).enter()
            .append("option")
            .attr("value", function (d) { return  d.appName; })
            .text(function (d) { return d.appName; })
            .order();

        select.append("option")
            .attr("value", "All App")
            .text("All App");

        function onchange() {
            var selectValue = d3.select('select').property('value')
            if (selectValue == "All App"){
                d3.json("/appgraph", function(error, dataset) {
                    if (error) throw error;
                        makeVis(dataset);
                    });
            }
            else{
                d3.json("/context?appName="+selectValue, function(error, dataset) {
                    if (error) throw error;
                        makeVis(dataset);
                    });
            }
        };
        //End: Working dropdown box

    makeVis(dataset);
});

		var makeVis = function(dataset) {
            //Create an SVG element and append it to the DOM
            d3.select("svg").remove();
            var svgElement = d3.select("#graph")
                                .append("svg")
                                .style("width", width + margin.left + margin.right + "px")
                                .style("height", height + margin.top + margin.bottom + "px")
                                .attr({"width": width+margin.left+margin.right, "height": height+margin.top+margin.bottom})
                                .append("g")
                                .attr("transform","translate("+margin.left+","+margin.top+")");
            //Extract data from dataset
            			var nodes = dataset.nodes,
            				links = dataset.links;
            			//Create Force Layout
            			var force = d3.layout.force()
            							.size([width, height])
            							.nodes(nodes)
            							.links(links)
            							.gravity(0.05)
            							.charge(-200)
            							.linkDistance(200);
            			//Add links to SVG
            			var link = svgElement.selectAll(".link")
            						.data(links)
            						.enter()
            						.append("line")
            						.attr("stroke-width", function(d){ return 15/10; })
            						.attr("class", "link")
            						.on("click", linkClick);

            			//Add nodes to SVG
            			var node = svgElement.selectAll(".node")
            						.data(nodes)
            						.enter()
            						.append("g")
            						.attr("class", "node")
                                    .on("mouseover", nodeOver)
                                    .on("mouseout", nodeOut)
            						.on("click", nodeClick)
            						.call(force.drag);
            			//Add labels to each node
            			var label = node.append("text")
            							.attr("dx", 12)
            							.attr("dy", "0.35em")
            							.attr("font-size", function(d){ return d.influence*0.25>9? d.influence*0.25: 9; })
            							.text(function(d){ return d.appName; });
            			//Add circles to each node
            			var circle = node.append("circle")
            							.attr("r", function(d){ return d.influence/6>5 ? d.influence/6 : 5; })
            							.attr("fill", function(d){ return c10(d.zone*10); });

            			//This function will be executed for every tick of force layout
            			force.on("tick", function(){
            				//Set X and Y of node
            				node.attr("r", function(d){ return d.influence; })
            					.attr("cx", function(d){ return d.x = Math.max(d.influence, Math.min(width - d.influence, d.x));  })
            					.attr("cy", function(d){ return d.y = Math.max(d.influence, Math.min(height - d.influence, d.y)); });
            				//Set X, Y of link
            				link.attr("x1", function(d){ return d.source.x; })
            				link.attr("y1", function(d){ return d.source.y; })
            				link.attr("x2", function(d){ return d.target.x; })
            				link.attr("y2", function(d){ return d.target.y; });
            				//Shift node a little
            				node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
            			});
            			//Start the force layout calculation
            			force.start();
		};
        function nodeOver(d,i,e) {
        var el = this;
        if (!d3.event.fromElement) {
          el = e;
        }
        if (nodeFocus) {
          return;
        }

        highlightNeighbors(d,i);
      }

		function nodeClick(d,i) {
                nodeFocus = false;
                nodeOut();
                nodeOver(d,i,this);
                nodeFocus = true;
                var newContent = "<p><b>" + d.appName + "</b></p>";
                newContent += "<p>Attributes: </p><p><ul>";
                newContent += "<li>" + "Brand Name" + ": " + d.brand+ "</li>";
                newContent += "<li>" + "Zone" + ": " + d.zone+ "</li>";
                newContent += "<li>" + "Influence" + ": " + d.influence+ "</li>";

//                for (x in nodeAttributes) {
//                  newContent += "<li>" + nodeAttributes[x] + ": " + d.properties[nodeAttributes[x]]+ "</li>";
//                }
                newContent += "</ul></p><p>Dependency:</p><ul>";
                var neighbors = findNeighbors(d,i);
                for (var x in neighbors.nodes) {
                  if (neighbors.nodes[x] != d) {
                    newContent += "<li>" + neighbors.nodes[x].appName + "</li>";
                  }
                }
                newContent += "</ul></p>";

                d3.select("#modal").style("display", "block").select("#content").html(newContent);
              }

    function linkClick(d,i) {
            var newContent = "<p></p><p><b>" + "Dependency Details"  +"</b></p>";
            newContent += "<p><ul>";
            newContent += "<li>" + "Protocol" + ": " + d.dependency + "</li>";
            newContent += "</ul></p>";

            d3.select("#modal").style("display", "block").select("#content").html(newContent);
          }

        function highlightNeighbors(d,i) {
          var nodeNeighbors = findNeighbors(d,i);
          d3.selectAll("g.node").each(function(p) {
            var isNeighbor = nodeNeighbors.nodes.indexOf(p);
            d3.select(this).select("circle")
            .style("opacity", isNeighbor > -1 ? 1 : .25)
            .style("stroke-width", isNeighbor > -1 ? 3 : 0)
            .style("stroke", isNeighbor > -1 ? "black" : "gray");
            d3.select(this).select("text")
             .style("opacity", isNeighbor > -1 ? 1 : .25);
          })

          d3.selectAll("line.link")
          .style("stroke-width", function (d) {return nodeNeighbors.links.indexOf(d) > -1 ? 3 : 1.5})
          .style("opacity", function (d) {return nodeNeighbors.links.indexOf(d) > -1 ? 3 : .25})
        }
        function findNeighbors(d,i) {
          var neighborArray = [d];
          var linkArray = [];
          var linksArray = d3.selectAll("line.link").filter(function(p) {return p.source == d || p.target == d}).each(function(p) {
            neighborArray.indexOf(p.source) == -1 ? neighborArray.push(p.source) : null;
            neighborArray.indexOf(p.target) == -1 ? neighborArray.push(p.target) : null;
            linkArray.push(p);
          })
          return {nodes: neighborArray, links: linkArray};
        }
      function nodeOut() {
        if (nodeFocus) {
          return;
        }

        d3.selectAll(".hoverLabel").remove();
        d3.selectAll("circle").style("opacity", 1).style("stroke-width", 0);
        d3.selectAll("line").style("opacity", 1).style("stroke-width", function (d) {return 1.5});
        d3.selectAll("text").style("opacity", 1);
      }

      function sortAndUpdate(nodesForCombo, isAscending) {
        nodesForCombo.sort(function(a, b) {
        if (isAscending) {
               return d3.ascending(a.appName, b.appName);
        }
        else {
               return d3.descending(a.appName, b.appName);
        }
        });

      }

