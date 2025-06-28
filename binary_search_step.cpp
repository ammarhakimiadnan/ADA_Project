/*
 * File: binary_search_step.cpp
 * Implements binary search with step logging for CCP6214 Assignment
 * Requirements:
 *  - Log each comparison step during binary search
 *  - Output to binary_search_step_[target].txt
 *  - Verify dataset is sorted
 */

#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

struct Row {
    int number;
    string text;
    Row(int n, const string &t) : number(n), text(t) {}
    string toString() const {
        return to_string(number) + "/" + text;
    }
};

vector<Row> readDataset(const string &filename) {
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
    return rows;
}

bool isDatasetSorted(const vector<Row> &data) {
    for (size_t i = 1; i < data.size(); i++) {
        if (data[i].number < data[i-1].number) {
            return false;
        }
    }
    return true;
}

void binarySearchStep(const vector<Row> &data, int target, ofstream &writer) {
    int left = 0;
    int right = data.size() - 1;
    bool found = false;

    while (left <= right) {
        int mid = left + (right - left) / 2;
        writer << mid << ": " << data[mid].toString();

        if (data[mid].number == target) {
            found = true;
            break;
        } else if (data[mid].number < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
        writer << endl;
    }

    if (!found) {
        writer << -1;
    }
}

int main() {
    cout << "Enter dataset filename (e.g., merge_sort_10000.csv): ";
    string filename;
    cin >> filename;

    vector<Row> rows = readDataset(filename);
    if (rows.empty()) {
        cerr << "Error: No valid data loaded." << endl;
        return 1;
    }

    if (!isDatasetSorted(rows)) {
        cerr << "Error: Dataset is not sorted." << endl;
        return 1;
    }

    cout << "Enter target integer: ";
    int target;
    if (!(cin >> target)) {
        cerr << "Error: Invalid target input" << endl;
        return 1;
    }

    string outputFile = "binary_search_step_" + to_string(target) + ".txt";
    ofstream writer(outputFile);
    if (!writer.is_open()) {
        cerr << "Error: Failed to create output file." << endl;
        return 1;
    }

    binarySearchStep(rows, target, writer);
    cout << "Results saved to " << outputFile << endl;

    return 0;
}