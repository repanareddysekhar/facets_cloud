package com.example.virtualtopology.service;

import com.example.virtualtopology.dto.ConnectionGroupDto;
import com.example.virtualtopology.entity.ConnectionGroupEntity;
import com.example.virtualtopology.entity.EdgesEntity;
import com.example.virtualtopology.entity.NodeEntity;
import com.example.virtualtopology.repository.ConnectionGroupsRepo;
import com.example.virtualtopology.repository.EdgesRepo;
import com.example.virtualtopology.repository.NodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class TopologyService {

    public static HashMap<String, List<String>> connectionGroupNodes = new HashMap<>();
    public static HashMap<String, String> nodeToConnectionGroupLookUp = new HashMap<>();

    @Autowired
    private ConnectionGroupsRepo connectionGroupsRepo;

    @Autowired
    private NodeRepo nodeRepo;

    @Autowired
    private EdgesRepo edgesRepo;

    public ConnectionGroupDto addConnectionGroup(String connectionGroupName) {
        ConnectionGroupDto connectionGroupDto = new ConnectionGroupDto();
        if (!connectionGroupNodes.containsKey(connectionGroupName)) {
            ConnectionGroupEntity connectionGroupEntity = new ConnectionGroupEntity();
            connectionGroupEntity.setConnectionGroupName(connectionGroupName);
            connectionGroupEntity = connectionGroupsRepo.save(connectionGroupEntity);

            // add connection group in the loop up map
            connectionGroupNodes.put(connectionGroupName, new ArrayList<>());
            connectionGroupDto.setConnectionGroupId(connectionGroupEntity.getId());
            connectionGroupDto.setConnectionGroupName(connectionGroupEntity.getConnectionGroupName());
        } else {
            ConnectionGroupEntity connectionGroupEntity = connectionGroupsRepo.findByConnectionGroupName(connectionGroupName).orElse(null);
            if (Objects.isNull(connectionGroupEntity)) {
                return null;
            }
            connectionGroupDto.setConnectionGroupId(connectionGroupEntity.getId());
            connectionGroupDto.setConnectionGroupName(connectionGroupEntity.getConnectionGroupName());
        }
        return connectionGroupDto;
    }

    public ConnectionGroupDto getConnectionGroupDetails(String connectionGroupName) {
        ConnectionGroupDto connectionGroupDto = new ConnectionGroupDto();
        if (!connectionGroupNodes.containsKey(connectionGroupName)) {
            return null;
        } else {
            ConnectionGroupEntity connectionGroupEntity = connectionGroupsRepo.findByConnectionGroupName(connectionGroupName).orElse(null);
            if (Objects.isNull(connectionGroupEntity)) {
                return null;
            }
            connectionGroupDto.setConnectionGroupId(connectionGroupEntity.getId());
            connectionGroupDto.setConnectionGroupName(connectionGroupEntity.getConnectionGroupName());
        }
        return connectionGroupDto;
    }

    public List<String> getAllConnectionGroupDetails() {
        return new ArrayList<String>(connectionGroupNodes.keySet());
    }

    public boolean addNode(String nodeName, String connectionGroupName) throws Exception {
        if (!nodeToConnectionGroupLookUp.containsKey(nodeName)) {
            NodeEntity nodeEntity = new NodeEntity();
            nodeEntity.setNodeName(nodeName);
            ConnectionGroupEntity connectionGroupEntity = connectionGroupsRepo.findByConnectionGroupName(connectionGroupName).orElse(null);
            if (Objects.isNull(connectionGroupEntity)) {
                throw new Exception("Connection group does not exist");
            }
            nodeEntity.setConnectionGroup(connectionGroupEntity);
            nodeEntity = nodeRepo.save(nodeEntity);
            nodeToConnectionGroupLookUp.put(nodeName, connectionGroupName);
            List<String> allNodes = connectionGroupNodes.get(connectionGroupName);
            allNodes.add(nodeName);
            connectionGroupNodes.put(connectionGroupName, allNodes);
            return true;
        } else {
            if (!nodeToConnectionGroupLookUp.get(nodeName).equals(connectionGroupName)) {
                return false;
            }
            throw new Exception("Node already present in the connection group");
        }
    }

    public String findConnectionGroup(String nodeName) {
        if(nodeToConnectionGroupLookUp.containsKey(nodeName)) {
            return nodeToConnectionGroupLookUp.get(nodeName);
        } else {
            return "Does not belong to any connection group";
        }
    }

    public List<String> getAllNodesInConnectionGroup(String connectionGroupName) {
        if(connectionGroupNodes.containsKey(connectionGroupName)) {
            return new ArrayList<String>(connectionGroupNodes.get(connectionGroupName));
        }
        return null;
    }

    public EdgesEntity addEdge(String toNode, String fromNode, String connectionGroupName) throws Exception {
        if(!nodeToConnectionGroupLookUp.containsKey(toNode)) {
            throw new Exception(toNode + " doesn't exist");
        }
        if(!nodeToConnectionGroupLookUp.get(toNode).equals(connectionGroupName)) {
            throw new Exception(toNode + " doesn't belong to " + connectionGroupName);
        }
        if(!nodeToConnectionGroupLookUp.containsKey(fromNode)) {
            throw new Exception(fromNode + " doesn't exist");
        }
        if(!nodeToConnectionGroupLookUp.get(fromNode).equals(connectionGroupName)) {
            throw new Exception(fromNode + " doesn't belong to " + connectionGroupName);
        }
        if(!connectionGroupNodes.containsKey(connectionGroupName)) {
            throw new Exception(connectionGroupName + " doesn't exists");
        }

        EdgesEntity edgesEntity = new EdgesEntity();
        ConnectionGroupEntity connectionGroupEntity = connectionGroupsRepo.findByConnectionGroupName(connectionGroupName).orElse(null);
        if(Objects.isNull(connectionGroupEntity)) {
            throw new Exception(connectionGroupName + " doesn't exists");
        }
        edgesEntity.setConnectionGroup(connectionGroupEntity);
        NodeEntity toNodeEntity = nodeRepo.findByNodeName(toNode).orElse(null);
        if(Objects.isNull(toNodeEntity)) {
            throw new Exception(toNode + " doesn't exist");
        }
        edgesEntity.setToNode(toNodeEntity);
        NodeEntity fromNodeEntity = nodeRepo.findByNodeName(fromNode).orElse(null);
        if(Objects.isNull(fromNodeEntity)) {
            throw new Exception(fromNode + " doesn't exist");
        }
        edgesEntity.setFromNode(fromNodeEntity);
        edgesEntity = edgesRepo.save(edgesEntity);

        return edgesEntity;
    }

    public List<String> getAllEdgesInConnectionGroup(String connectionGroupName) throws Exception {
        if(!connectionGroupNodes.containsKey(connectionGroupName)){
            throw new Exception("Connection group does not ecists");
        }

        List<EdgesEntity> edgesEntityList = edgesRepo.findAllByConnectionGroup(connectionGroupName);
        List<String> allEdges = new ArrayList<>();
        for(EdgesEntity edgesEntity : edgesEntityList) {
            String edge = edgesEntity.getToNode().getNodeName() + "-->" + edgesEntity.getFromNode().getNodeName();
            allEdges.add(edge);
        }
        return allEdges;
    }
}
