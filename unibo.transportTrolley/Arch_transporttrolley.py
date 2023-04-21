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
with Diagram('transporttrolleyArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_basicrobot', graph_attr=nodeattr):
          pathexec=Custom('pathexec(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_transporttrolley', graph_attr=nodeattr):
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          ledcontroller=Custom('ledcontroller','./qakicons/symActorSmall.png')
          transporttrolley_mover=Custom('transporttrolley_mover','./qakicons/symActorSmall.png')
     with Cluster('ctx_rasp', graph_attr=nodeattr):
          led=Custom('led(ext)','./qakicons/externalQActor.png')
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='findPath', fontcolor='magenta') >> transporttrolley_mover
     transporttrolley_mover >> Edge(color='blue', style='solid', xlabel='coapUpdate', fontcolor='blue') >> ledcontroller
     ledcontroller >> Edge(color='blue', style='solid', xlabel='turnon', fontcolor='blue') >> led
     ledcontroller >> Edge(color='blue', style='solid', xlabel='turnoff', fontcolor='blue') >> led
     ledcontroller >> Edge(color='blue', style='solid', xlabel='blink', fontcolor='blue') >> led
     transporttrolley_mover >> Edge(color='magenta', style='solid', xlabel='dopath', fontcolor='magenta') >> pathexec
diag
