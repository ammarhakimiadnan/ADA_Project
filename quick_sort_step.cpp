#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <chrono>

struct Row
{
    int number;
    std::string text;

    Row(int n, const std::string &t) : number(n), text(t) {}

    std::string toString() const
    {
        return std::to_string(number) + "/" + text;
    }
};

// Read the dataset from a CSV file and return a vector of Row objects
std::vector<Row> readDataset(const std::string &filename)
{
    std::vector<Row> rows;
    std::ifstream file(filename);
    if (!file.is_open())
    {
        std::cerr << "Error: Could not open file " << filename << std::endl;
        return rows;
    }
    std::string line;
    while (std::getline(file, line))
    {
        std::stringstream ss(line);
        std::string numStr, text;
        if (std::getline(ss, numStr, ',') && std::getline(ss, text))
        {
            try
            {
                int number = std::stoi(numStr);
                rows.emplace_back(number, text);
            }
            catch (...)
            {
                // Skip header or malformed lines
            }
        }
    }
    return rows;
}

// Print the list in [number/string, ...] format
void printRowList(const std::vector<Row> &list, std::ofstream &writer, bool brackets = true)
{
    if (brackets) writer << "[";
    for (size_t i = 0; i < list.size(); ++i)
    {
        writer << list[i].toString();
        if (i < list.size() - 1)
            writer << ", ";
    }
    if (brackets) writer << "]";
    writer << std::endl;
}

// Partition logic for Quick Sort with step printing
int partition(std::vector<Row> &list, int low, int high, std::ofstream &writer)
{
    Row pivot = list[high];
    int i = low - 1;
    for (int j = low; j < high; ++j)
    {
        if (list[j].number <= pivot.number)
        {
            ++i;
            std::swap(list[i], list[j]);
        }
    }
    std::swap(list[i + 1], list[high]);
    
    // Print the pivot index and current state
    writer << "pi=" << (i + 1) << " [";
    for (size_t idx = 0; idx < list.size(); ++idx)
    {
        writer << list[idx].toString();
        if (idx < list.size() - 1)
            writer << ", ";
    }
    writer << "]" << std::endl;

    return i + 1;
}

// Recursive Quick Sort with step printing
void quickSort(std::vector<Row> &list, int low, int high, std::ofstream &writer)
{
    if (low < high)
    {
        int pivotIndex = partition(list, low, high, writer);
        quickSort(list, low, pivotIndex - 1, writer);
        quickSort(list, pivotIndex + 1, high, writer);
    }
}

int main()
{
    std::string inputFile = "dataset_sample_1000.csv";
    std::vector<Row> rows = readDataset(inputFile);

    if (rows.empty())
    {
        std::cerr << "No data to sort or file not found. Exiting." << std::endl;
        return 1;
    }

    int start, end;
    std::cout << "Select start row: ";
    std::cin >> start;
    std::cout << "Select end row: ";
    std::cin >> end;

    // Adjust for 1-based indexing and validate range
    start = std::max(1, start);
    end = std::min(static_cast<int>(rows.size()), end);
    if (start > end)
    {
        std::cerr << "Invalid range selected. Exiting." << std::endl;
        return 1;
    }

    // Create a subvector for the selected range
    std::vector<Row> selectedRows(rows.begin() + start - 1, rows.begin() + end);

    std::string outputFile = "quick_sort_step_" + std::to_string(start) + "_" + std::to_string(end) + ".txt";
    std::ofstream writer(outputFile);
    if (!writer.is_open())
    {
        std::cerr << "Error: Could not open output file " << outputFile << std::endl;
        return 1;
    }

    // Print initial state
    printRowList(selectedRows, writer);

    // Perform quick sort with step printing
    quickSort(selectedRows, 0, selectedRows.size() - 1, writer);

    writer.close();

    std::cout << "quick sort with C++ completed.\nCheck " << outputFile << " for results." << std::endl;
    return 0;
}