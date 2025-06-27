#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <chrono>
#include <cstdlib>
#include <ctime>
#include <algorithm>
#include <iomanip>

using namespace std;
using namespace std::chrono;

struct Row {
    int number;
    string text;
    Row(int n, const string &t) : number(n), text(t) {}
    bool operator<(const Row& other) const {
        return number < other.number;
    }
};

vector<Row> readAndSortDataset(const string &filename) {
    vector<Row> rows;
    ifstream file(filename);
    if (!file.is_open()) {
        cerr << "Error: Failed to open file " << filename << endl;
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
                cerr << "Warning: Skipped invalid row: " << line << endl;
            }
        }
    }
    
    sort(rows.begin(), rows.end());
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

void runBinarySearchTests(const vector<Row> &data) {
    int n = data.size();
    string outputFilename = "binary_search_" + to_string(n) + ".txt";
    ofstream writer(outputFilename);
    if (!writer.is_open()) {
        cerr << "Error: Failed to create output file " << outputFilename << endl;
        return;
    }

    // Prevent optimization
    volatile int dummy = 0;
    
    // Warm up
    for (int i = 0; i < 1000; i++) {
        dummy += binarySearch(data, data[0].number);
    }

    // Best case
    int bestTarget = data[n/2].number;
    auto bestStart = high_resolution_clock::now();
    for (int i = 0; i < n; i++) {
        dummy += binarySearch(data, bestTarget);
    }
    double bestTime = duration_cast<nanoseconds>(high_resolution_clock::now() - bestStart).count() / 1000000.0 / n;

    // Average case
    srand(time(0));
    auto avgStart = high_resolution_clock::now();
    for (int i = 0; i < n; i++) {
        int randomIndex = rand() % n;
        dummy += binarySearch(data, data[randomIndex].number);
    }
    double avgTime = duration_cast<nanoseconds>(high_resolution_clock::now() - avgStart).count() / 1000000.0 / n;

    // Worst case
    int worstTarget = data.back().number + 1;
    auto worstStart = high_resolution_clock::now();
    for (int i = 0; i < n; i++) {
        dummy += binarySearch(data, worstTarget);
    }
    double worstTime = duration_cast<nanoseconds>(high_resolution_clock::now() - worstStart).count() / 1000000.0 / n;

    writer << fixed << setprecision(6);
    writer << "Best case time: " << bestTime << " ms\n";
    writer << "Average case time: " << avgTime << " ms\n";
    writer << "Worst case time: " << worstTime << " ms\n";

    cout << "Results saved to " << outputFilename << endl;
}

int main(int argc, char* argv[]) {
    if (argc != 2) {
        cerr << "Usage: " << argv[0] << " <dataset_filename>" << endl;
        return 1;
    }

    string filename = argv[1];
    vector<Row> rows = readAndSortDataset(filename);
    if (rows.empty()) {
        cerr << "Error: No valid data loaded." << endl;
        return 1;
    }

    runBinarySearchTests(rows);
    return 0;
}