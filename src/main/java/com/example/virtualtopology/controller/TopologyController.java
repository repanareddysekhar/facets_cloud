package com.example.virtualtopology.controller;

import com.example.virtualtopology.dto.ConnectionGroupDto;
import com.example.virtualtopology.entity.EdgesEntity;
import com.example.virtualtopology.service.TopologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class TopologyController {

    private static final Logger log = LoggerFactory.getLogger(TopologyController.class);

    @Autowired
    private TopologyService topologyService;

    @PostMapping("/connectionGroups")
    public ResponseEntity<ConnectionGroupDto> addConnectionGroup(@RequestParam String connectionGroupName) {
        log.info("addConnectionGroup called");
        ConnectionGroupDto connectionGroupDto = topologyService.addConnectionGroup(connectionGroupName);
        if (!Objects.isNull(connectionGroupDto)) {
            return new ResponseEntity<>(connectionGroupDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/connectionGroups/{connectionGroupName}")
    public ResponseEntity<ConnectionGroupDto> getConnectionGroupDetails(@PathVariable String connectionGroupName) {
        log.info("getConnectionGroupDetails called");
        ConnectionGroupDto connectionGroupDto = topologyService.getConnectionGroupDetails(connectionGroupName);
        if (!Objects.isNull(connectionGroupDto)) {
            return new ResponseEntity<>(connectionGroupDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/connectionGroups")
    public ResponseEntity<List<String>> getAllConnectionGroupDetails() {
        log.info("getAllConnectionGroupDetails called");
        return new ResponseEntity<>(topologyService.getAllConnectionGroupDetails(), HttpStatus.OK);
    }

    @PostMapping("/addNode")
    public ResponseEntity<String> addNode(
            @RequestParam String nodeName,
            @RequestParam String connectionGroupName) throws Exception {
        log.info("addNode called");
        if (topologyService.addNode(nodeName, connectionGroupName)) {
            return new ResponseEntity<>(nodeName + " added to group " + connectionGroupName, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Trying to add existing node to another connection group", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addEdge")
    public ResponseEntity<String> addEdge(
            @RequestParam String toNode,
            @RequestParam String fromNode,
            @RequestParam String connectionGroupName) throws Exception {
        log.info("addEdge called");
        EdgesEntity edgesEntity = topologyService.addEdge(toNode, fromNode, connectionGroupName);
        return new ResponseEntity<>("Successfully added edge", HttpStatus.OK);
    }

    @GetMapping("/findConnectionGroup")
    public ResponseEntity<String> findConnectionGroup(@RequestParam String nodeName) {
        log.info("findConnectionGroup called");
        return new ResponseEntity<>(topologyService.findConnectionGroup(nodeName), HttpStatus.OK);
    }

    @GetMapping("/getAllNodesInConnectionGroup")
    public ResponseEntity<List<String>> getAllNodesInConnectionGroup(@RequestParam String connectionGroupName) {
        log.info("getAllNodesInConnectionGroup called");
        List<String> nodesInConnectionGroup = topologyService.getAllNodesInConnectionGroup(connectionGroupName);
        if(Objects.isNull(nodesInConnectionGroup)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(nodesInConnectionGroup, HttpStatus.OK);
    }

    @GetMapping("/getAllEdgesInConnectionGroup")
    public ResponseEntity<List<String>> getAllEdgesInConnectionGroup(@RequestParam String connectionGroupName) throws Exception {
        log.info("getAllEdgesInConnectionGroup called");
        List<String> nodesInConnectionGroup = topologyService.getAllEdgesInConnectionGroup(connectionGroupName);
        return new ResponseEntity<>(nodesInConnectionGroup, HttpStatus.OK);
    }

}
