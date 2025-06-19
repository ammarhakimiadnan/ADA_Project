#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <chrono>

struct Row {
    int number;
    std::string text;

    Row(int n, const std::string& t) : number(n), text(t) {}

    std::string toString() const {
        return std::to_string(number) + "," + text;
    }
};

// Read the dataset from a CSV file and return a vector of Row objects
std::vector<Row> readDataset(const std::string& filename) {
    std::vector<Row> rows;
    std::ifstream file(filename);
    if (!file.is_open()) {
        std::cerr << "Error: Could not open file " << filename << std::endl;
        return rows;
    }
    std::string line;
    while (std::getline(file, line)) {
        std::stringstream ss(line);
        std::string numStr, text;
        if (std::getline(ss, numStr, ',') && std::getline(ss, text)) {
            try {
                int number = std::stoi(numStr);
                rows.emplace_back(number, text);
            } catch (...) {
                // Skip header or malformed lines
            }
        }
    }
    return rows;
}

// Print the list in number/string format
void printRowList(const std::vector<Row>& list, std::ofstream& writer) {
    for (size_t i = 0; i < list.size(); ++i) {
        writer << list[i].toString();
        if (i < list.size() - 1)
            writer << std::endl;
    }
}

// Partition logic for Quick Sort
int partition(std::vector<Row>& list, int low, int high) {
    Row pivot = list[high];
    int i = low - 1;
    for (int j = low; j < high; ++j) {
        if (list[j].number <= pivot.number) {
            ++i;
            std::swap(list[i], list[j]);
        }
    }
    std::swap(list[i + 1], list[high]);
    return i + 1;
}

// Recursive Quick Sort
void quickSort(std::vector<Row>& list, int low, int high) {
    if (low < high) {
        int pivotIndex = partition(list, low, high);
        quickSort(list, low, pivotIndex - 1);
        quickSort(list, pivotIndex + 1, high);
    }
}

int main() {
    std::string inputFile = "dataset_20000.csv";
    std::vector<Row> rows = readDataset(inputFile);
    int size = rows.size();

    if (rows.empty()) {
        std::cerr << "No data to sort or file not found. Exiting." << std::endl;
        return 1;
    }

    std::string outputFile = "quick_sort_" + std::to_string(size) + ".csv";
    std::ofstream writer(outputFile);
    if (!writer.is_open()) {
        std::cerr << "Error: Could not open output file " << outputFile << std::endl;
        return 1;
    }

    std::cout << "Quick Sort with C++" << std::endl;
    std::cout << "Dataset Size: " << size << std::endl;

    auto start = std::chrono::high_resolution_clock::now();
    quickSort(rows, 0, size - 1);
    auto end = std::chrono::high_resolution_clock::now();

    std::chrono::duration<double, std::milli> elapsed = end - start;
    std::cout << "Time Taken: " << elapsed.count() << " ms" << std::endl;

    printRowList(rows, writer);
    writer.close();

    std::cout << "Quick sort with C++ completed.\nCheck " << outputFile << " for results." << std::endl;
    return 0;
}