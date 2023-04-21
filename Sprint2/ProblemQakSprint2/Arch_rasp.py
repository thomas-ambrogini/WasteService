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
with Diagram('raspArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctx_rasp', graph_attr=nodeattr):
          led=Custom('led','./qakicons/symActorSmall.png')
          sonarqak22=Custom('sonarqak22','./qakicons/symActorSmall.png')
          ledcontroller=Custom('ledcontroller','./qakicons/symActorSmall.png')
          sonarmastermock=Custom('sonarmastermock','./qakicons/symActorSmall.png')
          sonarsimulator=Custom('sonarsimulator(coded)','./qakicons/codedQActor.png')
          sonardatasource=Custom('sonardatasource(coded)','./qakicons/codedQActor.png')
          datacleaner=Custom('datacleaner(coded)','./qakicons/codedQActor.png')
          ledsimulator=Custom('ledsimulator(coded)','./qakicons/codedQActor.png')
          ledconcrete=Custom('ledconcrete(coded)','./qakicons/codedQActor.png')
     led >> Edge(color='blue', style='solid', xlabel='turnon', fontcolor='blue') >> ledsimulator
     led >> Edge(color='blue', style='solid', xlabel='turnon', fontcolor='blue') >> ledconcrete
     led >> Edge(color='blue', style='solid', xlabel='turnoff', fontcolor='blue') >> ledsimulator
     led >> Edge(color='blue', style='solid', xlabel='turnoff', fontcolor='blue') >> ledconcrete
     sonarqak22 >> Edge(color='blue', style='solid', xlabel='sonaractivate', fontcolor='blue') >> sonarsimulator
     sonarqak22 >> Edge(color='blue', style='solid', xlabel='sonaractivate', fontcolor='blue') >> sonardatasource
     sys >> Edge(color='red', style='dashed', xlabel='sonar', fontcolor='red') >> sonarqak22
     sonarqak22 >> Edge( xlabel='sonardata', **eventedgeattr, fontcolor='red') >> sys
     ledcontroller >> Edge(color='blue', style='solid', xlabel='turnon', fontcolor='blue') >> led
     ledcontroller >> Edge(color='blue', style='solid', xlabel='turnoff', fontcolor='blue') >> led
     ledcontroller >> Edge(color='blue', style='solid', xlabel='blink', fontcolor='blue') >> led
     sonarmastermock >> Edge(color='blue', style='solid', xlabel='sonaractivate', fontcolor='blue') >> sonarqak22
     sonarmastermock >> Edge(color='blue', style='solid', xlabel='sonardeactivate', fontcolor='blue') >> sonarqak22
diag
