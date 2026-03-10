#!/bin/bash

# TGLOBE Load-Links API Test Script
# Tests the heavy load categorization logic (>5000kg = HEAVY_DUTY)

BASE_URL="http://localhost:8080/api/v1/bookings"
MINIKUBE_URL="http://$(minikube ip):30001/api/v1/bookings"

echo "========================================"
echo "TGLOBE Load-Links API Test Suite"
echo "========================================"
echo ""

# Function to test with local endpoint
test_local() {
    echo "Testing with LOCAL endpoint: $BASE_URL"
    echo ""
    
    # Test 1: Light Load (should remain as provided or default)
    echo "[Test 1] Creating Light Load (2000kg)..."
    curl -X POST "$BASE_URL/request" \
        -H "Content-Type: application/json" \
        -d '{
            "customerName": "Lagos Logistics Ltd",
            "loadType": "Light Goods",
            "weight": 2000.0,
            "pickup": "Onne Port",
            "dropoff": "Trans Amadi",
            "status": "PENDING"
        }' \
        -w "\nHTTP Status: %{http_code}\n\n"
    
    # Test 2: Heavy Load (should be auto-categorized as HEAVY_DUTY)
    echo "[Test 2] Creating Heavy Load (8000kg)..."
    curl -X POST "$BASE_URL/request" \
        -H "Content-Type: application/json" \
        -d '{
            "customerName": "Port Machinery Corp",
            "loadType": "Heavy Machinery",
            "weight": 8000.0,
            "pickup": "Onne Port",
            "dropoff": "Warri Industrial",
            "status": "PENDING"
        }' \
        -w "\nHTTP Status: %{http_code}\n\n"
    
    # Test 3: Threshold Load (exact 5000kg boundary)
    echo "[Test 3] Creating Boundary Load (5000kg)..."
    curl -X POST "$BASE_URL/request" \
        -H "Content-Type: application/json" \
        -d '{
            "customerName": "Boundary Test Co",
            "loadType": "Standard",
            "weight": 5000.0,
            "pickup": "Onne Port",
            "dropoff": "Port Harcourt",
            "status": "PENDING"
        }' \
        -w "\nHTTP Status: %{http_code}\n\n"
    
    # Test 4: Retrieve all bookings
    echo "[Test 4] Fetching all bookings..."
    curl -X GET "$BASE_URL/all" \
        -H "Content-Type: application/json" \
        -w "\nHTTP Status: %{http_code}\n\n"
}

# Function to test with Minikube endpoint
test_minikube() {
    echo "Testing with MINIKUBE endpoint: $MINIKUBE_URL"
    echo ""
    
    echo "[Minikube] Creating Heavy Load (10000kg)..."
    curl -X POST "$MINIKUBE_URL/request" \
        -H "Content-Type: application/json" \
        -d '{
            "customerName": "Minikube Test Carrier",
            "loadType": "Heavy Equipment",
            "weight": 10000.0,
            "pickup": "Onne Port",
            "dropoff": "Port Terminal",
            "status": "PENDING"
        }' \
        -w "\nHTTP Status: %{http_code}\n\n"
    
    echo "[Minikube] Fetching all bookings..."
    curl -X GET "$MINIKUBE_URL/all" \
        -H "Content-Type: application/json" \
        -w "\nHTTP Status: %{http_code}\n\n"
}

# Check arguments
if [ "$1" == "minikube" ]; then
    test_minikube
else
    test_local
fi

echo "========================================"
echo "Tests Complete!"
echo "========================================"
echo ""
echo "Load Categorization Logic:"
echo "  - Weight > 5000kg  → loadType set to 'HEAVY_DUTY'"
echo "  - Weight ≤ 5000kg → loadType remains as provided"
echo ""
