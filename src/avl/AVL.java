package avl;
import java.util.ArrayList;
import java.util.Vector;

import sequence.NonexistantVINException;

public class AVL {
    private Node root;

    public AVL() {};

    public void clear() {
        root = null;
    }
    /**
     * Returns the previous Vehicle in lexicographical order given a VIN
     * @param VIN VIN of the the Vehicle as a reference to get previous Vehicle in lexicographical order
     * @return previous Vehicle in lexicographical order given a node
     */
    public String prevKey(String VIN) throws NonexistantVINException {
        VIN = VIN.toUpperCase();
        Node n = findNode(VIN);
        if(n == null) {
            throw new NonexistantVINException(VIN);
        }
        if (n == root) {
            if (n.hasLeft()) {
                Node travel = n.left;
                while (travel.hasRight())
                    travel = travel.right;
                return travel.vehicle.getVIN();
            }
            return null;
        } else if(n.isRightChild() && !n.hasRight()) {
            return n.parent.vehicle.getVIN();
        } else {
            if(n.hasLeft()) {
                Node travel = n.left;
                while (travel.hasRight())
                    travel = travel.right;
                return travel.vehicle.getVIN();
            } else {
                if (n.isLeftChild()) {
                    Node travel = n.parent;
                    while(!travel.isRightChild() && travel != root)
                        travel = travel.parent;
                    if (travel == root)
                        return null;
                    return travel.parent.vehicle.getVIN();
                } else {
                    return n.parent.vehicle.getVIN();
                }
            }
        }
    }

    /**
     * Returns the next Vehicle in lexicographical order given a VIN
     * @param VIN VIN of the the Vehicle as a reference to get the next Vehicle in lexicographical order
     * @return next Vehicle in lexicographical order given a node
     */
    public String nextKey(String VIN) throws NonexistantVINException {
        VIN = VIN.toUpperCase();
        Node n = findNode(VIN);
        if(n == null) {
            throw new NonexistantVINException(VIN);
        }
        if (n == root) {
            if (n.hasRight()) {
                Node travel = n.right;
                while (travel.hasLeft())
                    travel = travel.left;
                return travel.vehicle.getVIN();
            }
            return null;
        } else {
            if (n.hasRight()) {
                Node travel = n.right;
                while (travel.hasLeft()) {
                    travel = travel.left;
                }
                return travel.vehicle.getVIN();
            } else {
                if(n.isRightChild()) {
                    Node travel = n.parent;
                    while (!travel.isLeftChild() && travel != root)
                        travel = travel.parent;
                    if (travel == root)
                        return null;
                    return travel.parent.vehicle.getVIN();
                } else {
                    return n.parent.vehicle.getVIN();
                }
            }
        }
    }

    /**
     * Inserts a Vehicle into the AVL Tree sorted by VIN in lexicographical order
     * @param input Vehicle to be inserted
     * @throws DuplicateVINException Exception when inserting a Vehicle with an already existing key
     */
    public void insert(Vehicle input) throws DuplicateVINException {
        if(isEmpty()) {
            root = new Node(input);
            return;
        }

        // BST Insertion
        Node current = root;
        while(true) {
            if (input.compareTo(current.vehicle.getVIN()) < 0) {
                if(current.hasLeft()) {
                    current = current.left;
                } else {
                    current.left = new Node(input, current);
                    break;
                }
            } else if (input.compareTo(current.vehicle.getVIN()) > 0) {
                if(current.hasRight()) {
                    current = current.right;
                } else {
                    current.right = new Node(input, current);
                    break;
                }
            } else {
                throw new DuplicateVINException(input.getVIN());
            }
        }
        this.updateHeight(current);

        // Balancing the AVL Tree after insertion
        for(current = current.parent;current != null; current = current.parent) {
            if(current.isUnbalanced()) {
                balanceNode(current);
                break;
            }
        }
    }

    /**
     * Deletes an element from the AVL given the VIN
     * @param VIN VIN of the vehicle to be deleted
     * @return true if the vehicle has been found and deleted; false if the vehicle does not exist
     */
    public boolean delete(String VIN) {
        VIN = VIN.toUpperCase();
        Node current = root;
        Node balancer = null;
        while(true) {
            // Searching node
            if (current.vehicle.compareTo(VIN) > 0) {
                if(current.hasLeft()) {
                    current = current.left;
                } else {
                    return false;
                }
            } else if (current.vehicle.compareTo(VIN) < 0) {
                if(current.hasRight()) {
                    current = current.right;
                } else {
                    return false;
                }
            } else {
                break;
            }
        }
        // Node has been found and will be deleted below
        if(current.isRightChild()) {
            // Single child substitution
            if(current.hasLeft() && !current.hasRight()) {
                current.parent.right = current.left;
                current.left.parent = current.parent;
                balancer = current.left; // For balancing reasons
            } else if (current.hasRight() && !current.hasLeft()) {
                current.parent.right = current.right;
                current.right.parent = current.parent;
                balancer = current.right; // For balancing reasons
            }
            // Leaf node case
            else if (!current.hasLeft() && !current.hasRight()) {
                current.parent.right = null;
                balancer = current.parent; // For balancing reasons
            }
            // Has both left and right case
            else {
                Node substitute = current.left;
                boolean wentRight = substitute.hasRight();
                while(substitute.hasRight())
                    substitute = substitute.right;
                current.parent.right = substitute;
                substitute.parent = current.parent;
                substitute.right = current.right;
                current.right.parent = substitute;
                if (wentRight) {
                    substitute.left = current.left;
                    current.left.parent = substitute;
                    balancer = substitute.parent;
                } else {
                    balancer = substitute;
                }
                printPreOrder();
            }
        } else if(current.isLeftChild()) {
            // Single child substitution
            if(current.hasLeft() && !current.hasRight()) {
                current.parent.left = current.left;
                current.left.parent = current.parent;
                balancer = current.left; // For balancing reasons
            } else if (current.hasRight() && !current.hasLeft()) {
                current.parent.left = current.right;
                current.right.parent = current.parent;
                balancer = current.right; // For balancing reasons
            }
            // Leaf node case
            else if (!current.hasLeft() && !current.hasRight()) {
                current.parent.left = null;
                balancer = current.parent; // For balancing reasons
            }
            // Has both left and right case
            else {
                Node substitute = current.left;
                boolean wentRight = substitute.hasRight();
                while(substitute.hasRight())
                    substitute = substitute.right;
                substitute.parent = current.parent;
                current.parent.left = substitute;
                substitute.right = current.right;
                current.right.parent = substitute;
                if (wentRight) {
                    substitute.left = current.left;
                    current.left.parent = substitute;
                    balancer = substitute.parent;
                } else {
                    balancer = substitute;
                }
            }
        } else if(current == root) {
            // Single child substitution
            if(current.hasLeft() && !current.hasRight()) {
                root = current.left;
                current.left.parent = null;
                balancer = root;
            } else if (current.hasRight() && !current.hasLeft()) {
                root = current.right;
                current.right.parent = null;
                balancer = root;
            }
            // Leaf node case
            else if (!current.hasLeft() && !current.hasRight()) {
                root = null;
            }
            // Has both left and right case
            else {
                Node substitute = current.left;
                boolean wentRight = substitute.hasRight();
                while(substitute.hasRight())
                    substitute = substitute.right;
                substitute.parent = null;
                substitute.right = current.right;
                current.right.parent = substitute;
                if (wentRight) {
                    substitute.left = current.left;
                    current.left.parent = substitute;
                    balancer = substitute.parent;
                } else {
                    balancer = substitute;
                }
                root = substitute;
            }
        }
        updateHeight(balancer);
        // Balancing the AVL Tree after deletion
        for(;balancer != null; balancer = balancer.parent) {
            if(balancer.isUnbalanced()) {
                balanceNode(balancer);
                break;
            }
        }
        return true;
    }

    public ArrayList<String> allKeys() {
        ArrayList<String> allKeys = new ArrayList<>();
        inorderGetKeys(root, allKeys);
        return allKeys;
    }
    private void inorderGetKeys(Node n, ArrayList<String> allKeys) {
        if(n.hasLeft())
            inorderGetKeys(n.left, allKeys);
        allKeys.add(n.getVehicle().getVIN());
        if(n.hasRight())
            inorderGetKeys(n.right, allKeys);
    }

    public ArrayList<Vehicle> allVehicles() {
        ArrayList<Vehicle> allVehicles = new ArrayList<>();
        inorderGetVehicles(root, allVehicles);
        return allVehicles;
    }
    private void inorderGetVehicles(Node n, ArrayList<Vehicle> allVehicles) {
        if(n.hasLeft())
            inorderGetVehicles(n.left, allVehicles);
        allVehicles.add(n.getVehicle());
        if(n.hasRight())
            inorderGetVehicles(n.right, allVehicles);
    }

    private void balanceNode(Node node) {
        boolean unbalanced = false;
        boolean r1 = false;
        boolean r2 = false;
        if(node.isUnbalanced()) {
            Node travel = node;
            unbalanced = true;
            if (!travel.hasLeft())
                r1 = true;
            else if (!travel.hasRight())
                r1 = false;
            else {
                r1 = travel.right.height > travel.left.height;
            }

            travel = r1 ? travel.right : travel.left;
            if (!travel.hasLeft())
                r2 = true;
            else if (!travel.hasRight())
                r2 = false;
            else {
                r2 = travel.right.height > travel.left.height;
            }
        }

        if (unbalanced) {
            if(r1 && r2) {
                leftRotation(node);
            } else if (!r1 && !r2) {
                rightRotation(node);
            } else if (r1 && !r2) {
                rightRotation(node.right);
                leftRotation(node);
            } else {
                leftRotation(node.left);
                rightRotation(node);
            }
        }
    }

    private void leftRotation(Node node) {
        Node r = node.right;
        node.right = r.left;
        if(node.right != null)
            node.right.parent = node;

        if(node == root)
            root = r;
        r.parent = node.parent;
        if(node.parent != null)
            if(node.isRightChild())
                node.parent.right = r;
            else
                node.parent.left = r;

        r.left = node;
        node.parent = r;

        this.updateHeight(node);
    }

    private void rightRotation(Node node) {
        Node l = node.left;
        node.left = l.right;
        if(node.left != null)
            node.left.parent = node;

        if(node == root)
            root = l;
        l.parent = node.parent;
        if(node.parent != null)
            if(node.isRightChild())
                node.parent.right = l;
            else
                node.parent.left = l;

        l.right = node;
        node.parent = l;

        this.updateHeight(node);
    }

    public Vehicle find(String VIN) {
        return findNode(VIN).getVehicle();
    }

    public Node findNode(String VIN) {
        Node current = root;
        while(true) {
            if (current.vehicle.compareTo(VIN) > 0) {
                if(current.hasLeft()) {
                    current = current.left;
                } else {
                    return null;
                }
            } else if (current.vehicle.compareTo(VIN) < 0) {
                if(current.hasRight()) {
                    current = current.right;
                } else {
                    return null;
                }
            } else {
                return current;
            }
        }
    }

    private void updateHeight(Node updatedNodeParent) {
        for (Node current = updatedNodeParent; current != null; current = current.parent) {
            if(!current.hasRight() && !current.hasLeft()) {
                current.height = 0;
            } else if (!current.hasRight()) {
                current.height = current.left.height + 1;
            } else if (!current.hasLeft()){
                current.height = current.right.height + 1;
            } else {
                current.height = Math.max(current.left.height, current.right.height) + 1;
            }
        }
    }

    // Useful for debugging prints nodes with [VIN Path ParentVIN]
    public void printPreOrder() {
        printPreOrder(root, "");
        System.out.println();
    }
    private void printPreOrder(Node v, String s) {
        if(v.parent == null)
            System.out.printf("[%s Ø %d root]", v.vehicle.getVIN(), v.height);
        else
            System.out.printf("[%s %s %d %s]", v.vehicle.getVIN(), s, v.height, v.parent.vehicle.getVIN());

        if (v.hasLeft())
            printPreOrder(v.left, s+"L");
        if (v.hasRight())
            printPreOrder(v.right, s+"R");
    }

    public boolean isEmpty() {
        return root == null;
    }

    private static class Node {
        private Vehicle vehicle;
        private int height;
        private Node parent;
        private Node left;
        private Node right;

        private Node(Vehicle vehicle) {
            this.vehicle = vehicle;
            height = 0;
        }

        private Node(Vehicle vehicle, Node parent) {
            this.parent = parent;
            this.vehicle = vehicle;
            height = 0;
        }

        private boolean isRightChild() {
            return this.parent != null && this.parent.right == this;
        }

        private boolean isLeftChild() {
            return this.parent != null && this.parent.left == this;
        }

        private boolean hasLeft() {
            return left != null;
        }

        private boolean hasRight() {
            return right != null;
        }

        private Node getRight() {
            return right;
        }

        private Node getLeft() {
            return left;
        }

        private Vehicle getVehicle() {
            return vehicle;
        }

        private boolean isUnbalanced () {
            int rHeight = this.right == null ? -1 : this.right.height;
            int lHeight = this.left == null ? -1 : this.left.height;
            return Math.abs(rHeight - lHeight) > 1;
        }
    }
}
