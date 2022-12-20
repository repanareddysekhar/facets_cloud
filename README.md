# facets_cloud

**Brief Intro:**
Have written SpringBoot app for creating connection groups, nodes, adding nodes to connection groups, adding edges between nodes, finding connection group using nodes name etc.
Basic error handling is done due to time constraint.

All the requirements mentioned in the doc are implemented(based on my understanding).

**Methodology:**
1. Used h2 database itself for now.

2. Database structure is as follows:

TABLE **CONNECTION_GROUP_ENTITY** with COLUMNS (ID, CONNECTION_GROUP_NAME)

TABLE **NODE_ENTITY** with COLUMNS (ID, NODE_NAME, CONNECTION_GROUP_ID)

TABLE **EDGES_ENTITY** with COLUMNS (ID, TO_NODE_ID, FROM_NODE_ID, CONNECTION_GROUP_ID)

Using the above tables, storing the graph.

3. Used two lookup hashmaps
-> One for storing connectionGroupName and its nodes
-> One for storing nodeName and group name it belongs to.
Using this hashmaps, we avoid querying the database and helps in fast lookup.

4. User can create the connection groups, nodes and perform all above operations. Necessary error handles are implemented to throw errors if user tries to perform operations in wrong manner as described in the assignment document.


All the API names are self explanotory

**EndPoints to test: (via postman or curl etc.)**
1. Listing all connection groups:
GET http://localhost:8097/api/connectionGroups

2. getConnectionGroupDetails using connectionGroup Name
GET http://localhost:8097/api/connectionGroups/{connectionGroupName}

3. Add connectionGroup into db
POST http://localhost:8097/api/connectionGroups?connectionGroupName={groupName}

4. Adding node to connectionGroup
POST http://localhost:8097/api/addNode?nodeName={nodeName}&connectionGroupName={groupName}

5. Adding edge between nodes
POST http://localhost:8097/api/addEdge?toNode={nodeName}&fromNode={nodeName}&connectionGroupName={groupName}

6. Find connection group using node
GET http://localhost:8097/api/findConnectionGroup?nodeName={nodeName}

7. Get all nodes in the connection group
GET http://localhost:8097/api/getAllNodesInConnectionGroup?connectionGroupName={groupName}

8. Get all edges in the connection group
GET http://localhost:8097/api/getAllEdgesInConnectionGroup?connectionGroupName={groupName}
