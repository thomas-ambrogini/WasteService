from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
with Diagram('wasteserviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_transporttrolley', graph_attr=nodeattr):
          transporttrolley=Custom('transporttrolley(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_wasteservice', graph_attr=nodeattr):
          storage_manager=Custom('storage_manager','./qakicons/symActorSmall.png')
          wasteservice=Custom('wasteservice','./qakicons/symActorSmall.png')
     wasteservice >> Edge(color='magenta', style='solid', xlabel='moveToDestination', fontcolor='magenta') >> transporttrolley
     wasteservice >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transporttrolley
     wasteservice >> Edge(color='magenta', style='solid', xlabel='deposit', fontcolor='magenta') >> transporttrolley
     wasteservice >> Edge(color='blue', style='solid', xlabel='updateWeights', fontcolor='blue') >> storage_manager
     wasteservice >> Edge(color='magenta', style='solid', xlabel='trolleyRotate', fontcolor='magenta') >> transporttrolley
diag
