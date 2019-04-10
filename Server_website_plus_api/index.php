<?php 

if (isset($_GET['url'])) {

echo('
<!DOCTYPE html>

<head>
    <title>'.$_GET['url'].'</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="utf-8" />
    
 
	




<!-- Bootstrap -->
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet" />
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet" />

    <link href="//fonts.googleapis.com/css?family=Abel|Open+Sans:400,600" rel="stylesheet">
    <!--<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>-->
    <script src="//cdnjs.cloudflare.com/ajax/libs/d3/3.4.6/d3.min.js"></script>
    <script src="//cdn.jsdelivr.net/parseuri/1.2.2/parseuri.min.js"></script>
    <style>

 html {
            background: url(bg/rotate.php) no-repeat center center fixed; 
            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            background-size: cover;
        }



        h1 {
            font-family: "Abel", Arial, sans-serif;
            font-weight: 400;
            font-size: 40px;
        }

      
        .panel {
            background-color: transparent;
	    background: transparent;
	    border-color: transparent;

        }

       body {
            
            font-size: 16px;
            font-family: "Open Sans",serif;
            background: transparent;
        }
        .margin-base-vertical {
            margin: 40px 0;
        }



.navbar {
   background-color: transparent;
   background: transparent;
   border-color: transparent;

}



.node circle {
  cursor: pointer;
  stroke: #3182bd;
  stroke-width: 1.5px;
}

.node text {
  font: 15px sans-serif;
  pointer-events: none;
  text-anchor: middle;
 
 
}

line.link {
  fill: none;
  stroke: #9ecae1;
  stroke-width: 1.5px;
}



    </style>


</head>




<nav class="navbar navbar-default" role="navigation">
  
          


<form class="navbar-form navbar-left" role="search" method="get" id="another">
  <div class="form-group">
    <input type="url" class="form-control" name="url" placeholder="http://google.com" id="input">
  </div>

  <button type="submit" class="btn btn-default">Another?</button>
</form>
<script type="text/javascript">
      var myform = document.getElementById(\'another\');
      myform.onsubmit = function(){ 
                    document.getElementById(\'input\').value = parseUri(document.getElementById(\'input\').value).host;
                    myform.submit();
                  };
</script>


</nav>

<!--<div class="panel panel-default">
  <div class="panel-body">-->

<script>
function getWidth() {
    if (self.innerWidth) {
       return self.innerWidth;
    }
    else 
if (document.documentElement && document.documentElement.clientHeight){
        return document.documentElement.clientWidth;
    }
    else if (document.body) {
        return document.body.clientWidth;
    }
    return 0;
}

function getHeight() {
    if (self.innerHeight) {
       return self.innerHeight;
    }
    else 
if (document.documentElement && document.documentElement.clientHeight){
        return document.documentElement.clientHeight;
    }
    else if (document.body) {
        return document.body.clientHeight;
    }
    return 0;
}


var width =  getWidth()-20,
    height = getHeight()-100,
    root;

var force = d3.layout.force()
    .linkDistance(100)
    .charge(-200)
    .gravity(.05)
    .size([width, height])
    .on("tick", tick);

var svg = d3.select("body").append("svg")
    .attr("width", width)
    .attr("height", height);

var link = svg.selectAll(".link"),
    node = svg.selectAll(".node");

d3.json("/api/graph.php?url='.$_GET['url'].'", function(error, json) {
  root = json;
  update();
  
function collapse(d) {
    if (d.children) {
      d._children = d.children;
      d._children.forEach(collapse);
      d.children = null;
    }
  }


  root.children.forEach(collapse);
  update();
});


//});

function update() {
  var nodes = flatten(root),
      links = d3.layout.tree().links(nodes);

  // Restart the force layout.
  force
      .nodes(nodes)
      .links(links)
      .start();

  // Update links.
  link = link.data(links, function(d) { return d.target.id; });

  link.exit().remove();

  link.enter().insert("line", ".node")
      .attr("class", "link");

  // Update nodes.
  node = node.data(nodes, function(d) { return d.id; });

  node.exit().remove();

  var nodeEnter = node.enter().append("g")
      .attr("class", "node")
      .on("click", click)
      .call(force.drag);

  nodeEnter.append("circle")
      .attr("r", function(d) { return Math.sqrt(d.size) / 10 || 4.5; });

  nodeEnter.append("text")
      .attr("dy", ".35em")
      .text(function(d) { return d.name; });

  node.select("circle")
      .style("fill", color);
}

function tick() {
  link.attr("x1", function(d) { return d.source.x; })
      .attr("y1", function(d) { return d.source.y; })
      .attr("x2", function(d) { return d.target.x; })
      .attr("y2", function(d) { return d.target.y; });

  node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
}

function color(d) {
  return d._children ? "#3182bd" // collapsed package
      : d.children ? "#c6dbef" // expanded package
      : "#fd8d3c"; // leaf node
}

// Toggle children on click.
function click(d) {
  if (d3.event.defaultPrevented) return; // ignore drag
  if (d.children) {
    d._children = d.children;
    d.children = null;
  } else if(d._children){
    d.children = d._children;
    d._children = null;
  }
    else
	{
window.location = "http://map.codinginc.com/?url="+d.name;

}
  update();
}

// Returns a list of all nodes under the root.
function flatten(root) {
  var nodes = [], i = 0;

  function recurse(node) {
   if (node.children) node.children.forEach(recurse);
   if (!node.id) node.id = ++i;
    nodes.push(node);
  }

  recurse(root);
  return nodes;
}








</script>




<!--</div>
</div>-->





</body>
</html>

');


}
else
{

echo('<!DOCTYPE html>
<html>
<head>
    <title>The Map.</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="utf-8"/>
    <!-- Bootstrap -->
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet" />
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet" />

    <link href="//fonts.googleapis.com/css?family=Abel|Open+Sans:400,600" rel="stylesheet">
	 <script src="//cdn.jsdelivr.net/parseuri/1.2.2/parseuri.min.js"></script>
    <style>

 html {
            background: url(bg/rotate.php) no-repeat center center fixed; 
            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            background-size: cover;
        }


        body {
            padding-top: 20px;
            font-size: 16px;
            font-family: "Open Sans",serif;
            background: transparent;
        }

        h1 {
            font-family: "Abel", Arial, sans-serif;
            font-weight: 400;
            font-size: 40px;
        }

      
        .panel {
            background-color: rgba(255, 255, 255, 0.9);
        }

        .margin-base-vertical {
            margin: 40px 0;
        }

    </style>


</head>
<body>

    <div class="container">
        <div class="row">
            <div class="col-md-6 col-md-offset-3" style="color:#767676;">

                <h1 class="margin-base-vertical">What website?</h1>

                <p>
                    We are connected. 
                </p>
                <p>
           	    Now see how websites are connected.
                </p>

                <p>
                    
                </p>

                <form class="margin-base-vertical"  method="get" id="first">
                    <p class="input-group">
                        <span class="input-group-addon">&rarr;</span>
                        <input type="url" class="form-control input-lg" name="url" placeholder="http://google.com" id="input"/>
                    </p>
                   
                    <p class="text-center">
                        <button type="submit" class="btn btn-success btn-lg">Take a look.</button>
                    </p>
                    
                </form>
<script type="text/javascript">
      var myform = document.getElementById(\'first\');
      myform.onsubmit = function(){ 
                    document.getElementById(\'input\').value = parseUri(document.getElementById(\'input\').value).host;
                    myform.submit();
                  };
</script>

            </div>
        </div>
    </div>

</body>
</html>');






}
?>
