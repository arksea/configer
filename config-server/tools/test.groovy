import akka.actor.*;
import java.util.*;
import groovy.json.JsonOutput;
import akka.routing.*;
import net.arksea.acache.*;
import net.arksea.config.*;
import static net.arksea.base.FutureUtils.completer;
import java.time.*;
ActorRef cacheActor
ActorSelection cache
ConfigService svc 
try{
    svc = new ConfigService('weather-api', 'qa', ['172.17.150.87:8805'])
    println '-------------------------------------------------------------------'
    println 'appBootConfig -> ' + svc.getMap('appBootConfig')
    println 'config2 -> ' + svc.getInteger('config2')
    println 'config3 -> ' + svc.getString('config3')
    println 'config4 -> ' + svc.getFloat('config4')
    println '-------------------------------------------------------------------'
    Thread.sleep(35000)
    println 'appBootConfig -> ' + svc.getMap('appBootConfig')
} catch (Exception ex) {
    print ex
}
Thread.sleep(3500)
svc?.system?.terminate()
Thread.sleep(3000)