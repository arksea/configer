import akka.actor.*;
import java.util.*;
import groovy.json.JsonOutput;
import akka.routing.*;
import net.arksea.acache.*;
import net.arksea.config.*;
import static net.arksea.base.FutureUtils.completer;
import java.time.*;
ActorSystem sys
ActorRef cacheActor
ActorSelection cache

try{
    List<String> paths = Arrays.asList(
            'akka.tcp://system@172.17.150.87:8805/user/configCacheServer'
        );
    sys = akka.actor.ActorSystem.create('system')
    ConfigService svc = new ConfigService(sys, paths, 'weather-api', 'qa', 8000)
    Thread.sleep(3000)
    println '------------------------------------'
    println svc.getMap('appBootConfig')
    println svc.getInteger('config2')
    println svc.getString('config3')
    println svc.getFloat('config4')
    println '------------------------------------'
} catch (Exception ex) {
    print ex
}
Thread.sleep(3000)
sys?.terminate()
Thread.sleep(3000)