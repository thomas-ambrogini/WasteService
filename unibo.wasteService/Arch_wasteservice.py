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
     with Cluster('ctx_basicrobot', graph_attr=nodeattr):
          pathexec=Custom('pathexec(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_rasp', graph_attr=nodeattr):
          led=Custom('led(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_wasteservice', graph_attr=nodeattr):
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          alarmemitter=Custom('alarmemitter','./qakicons/symActorSmall.png')
          transporttrolley_mover=Custom('transporttrolley_mover','./qakicons/symActorSmall.png')
          storage_manager=Custom('storage_manager','./qakicons/symActorSmall.png')
          wasteservice=Custom('wasteservice','./qakicons/symActorSmall.png')
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='findPath', fontcolor='magenta') >> transporttrolley_mover
     sys >> Edge(color='red', style='dashed', xlabel='sonardata', fontcolor='red') >> alarmemitter
     transporttrolley_mover >> Edge(color='magenta', style='solid', xlabel='dopath', fontcolor='magenta') >> pathexec
     wasteservice >> Edge(color='magenta', style='solid', xlabel='storeRequest', fontcolor='magenta') >> storage_manager
     wasteservice >> Edge(color='magenta', style='solid', xlabel='moveToDestination', fontcolor='magenta') >> transporttrolley
     wasteservice >> Edge(color='magenta', style='solid', xlabel='pickup', fontcolor='magenta') >> transporttrolley
     wasteservice >> Edge(color='magenta', style='solid', xlabel='deposit', fontcolor='magenta') >> transporttrolley
     wasteservice >> Edge(color='blue', style='solid', xlabel='updateWeights', fontcolor='blue') >> storage_manager
diag
