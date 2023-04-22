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
          transporttrolley_mover=Custom('transporttrolley_mover','./qakicons/symActorSmall.png')
          alarmemitter=Custom('alarmemitter','./qakicons/symActorSmall.png')
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='findPath', fontcolor='magenta') >> transporttrolley_mover
     transporttrolley_mover >> Edge(color='magenta', style='solid', xlabel='dopath', fontcolor='magenta') >> pathexec
     sys >> Edge(color='red', style='dashed', xlabel='sonardata', fontcolor='red') >> alarmemitter
     alarmemitter >> Edge(color='blue', style='solid', xlabel='stop', fontcolor='blue') >> transporttrolley_mover
     alarmemitter >> Edge(color='blue', style='solid', xlabel='resume', fontcolor='blue') >> transporttrolley_mover
diag
