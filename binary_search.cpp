#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <chrono>
#include <cstdlib>
#include <ctime>

using namespace std;
using namespace std::chrono;

struct Row {
    int number;
    string text;
    Row(int n, const string &t) : number(n), text(t) {}
};

vector<Row> readDataset(const string &filename) {
    vector<Row> rows;
    ifstream file(filename);
    if (!file.is_open()) {
        cout << "Error reading file: " << filename << endl;
        return rows;
    }

    string line;
    while (getline(file, line)) {
        stringstream ss(line);
        string numPart, textPart;
        if (getline(ss, numPart, ',') && getline(ss, textPart)) {
            try {
                int number = stoi(numPart);
                rows.emplace_back(number, textPart);
            } catch (...) {
                // Skip invalid rows
            }
        }
    }
    return rows;
}

int binarySearch(const vector<Row> &data, int target) {
    int left = 0;
    int right = data.size() - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (data[mid].number == target) {
            return mid;
        } else if (data[mid].number < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1;
}

int extractNFromFilename(const string &filename) {
    size_t pos1 = filename.find("_sort_");
    size_t pos2 = filename.find(".csv");
    if (pos1 == string::npos || pos2 == string::npos) {
        return -1;
    }
    string nStr = filename.substr(pos1 + 6, pos2 - (pos1 + 6));
    return stoi(nStr);
}

void runBinarySearchTests(const vector<Row> &data, const string &outputFilename) {
    ofstream writer(outputFilename);
    if (!writer.is_open()) {
        cout << "Error writing to file: " << outputFilename << endl;
        return;
    }

    int n = data.size();
    srand(time(0));

    // Best case: middle element (O(1))
    int bestTarget = data[n / 2].number;
    auto start = high_resolution_clock::now();
    for (int i = 0; i < n; i++) {
        binarySearch(data, bestTarget);
    }
    auto duration_best = duration_cast<milliseconds>(high_resolution_clock::now() - start);
    writer << "Best case time (ms): " << duration_best.count() << endl;

    // Average case: random elements (O(log n))
    start = high_resolution_clock::now();
    for (int i = 0; i < n; i++) {
        int randomIndex = rand() % n;
        binarySearch(data, data[randomIndex].number);
    }
    auto duration_avg = duration_cast<milliseconds>(high_resolution_clock::now() - start);
    writer << "Average case time (ms): " << duration_avg.count() << endl;

    // Worst case: non-existent element (O(log n))
    int worstTarget = -1;  // Guaranteed not to exist
    start = high_resolution_clock::now();
    for (int i = 0; i < n; i++) {
        binarySearch(data, worstTarget);
    }
    auto duration_worst = duration_cast<milliseconds>(high_resolution_clock::now() - start);
    writer << "Worst case time (ms): " << duration_worst.count() << endl;

    writer.close();
}

int main() {
    string filename;
    cout << "Enter dataset filename (e.g., merge_sort_10000.csv): ";
    cin >> filename;

    vector<Row> rows = readDataset(filename);
    if (rows.empty()) {
        cout << "No data loaded. Exiting." << endl;
        return 1;
    }

    int n = extractNFromFilename(filename);
    if (n == -1) {
        cout << "Invalid filename format. Exiting." << endl;
        return 1;
    }

    string outputFile = "binary_search_" + to_string(n) + ".txt";
    runBinarySearchTests(rows, outputFile);

    cout << "Binary search performance test completed.\nResults saved to " << outputFile << endl;
    return 0;
}