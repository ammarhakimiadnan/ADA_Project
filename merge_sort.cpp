#include <iostream> // For input/output operations
#include <fstream>  // For file operations
#include <sstream>  // For string stream operations
#include <vector>   // For using vector to store rows
#include <list>     // For using list to merge sorted sublists
#include <string>   // For string manipulation
#include <chrono>   // For timing the sort operation

using namespace std;

struct Row
{
    int number;
    string text;
    Row(int n, const string &t) : number(n), text(t) {}
    string toString() const
    {
        return to_string(number) + "," + text;
    }
};

// Reads the dataset from a CSV file and returns a vector of Row objects
vector<Row> readDataset(const string &filename)
{
    vector<Row> rows;
    ifstream file(filename);
    string line;
    while (getline(file, line))
    {
        stringstream ss(line);
        string numPart, textPart;
        if (getline(ss, numPart, ',') && getline(ss, textPart))
        {
            try
            {
                int number = stoi(numPart);
                rows.emplace_back(number, textPart);
            }
            catch (...)
            {
            }
        }
    }
    return rows;
}

// Prints the list in [number/string, ...] format to both terminal and file
void printRowList(const vector<Row> &list, ofstream &writer, const string &prefix = "")
{
    ostringstream oss;
    for (size_t i = 0; i < list.size(); ++i)
    {
        oss << list[i].toString();
        if (i < list.size() - 1)
            oss << endl;
    }
    writer << oss.str() << endl;
}

// Merge two sorted sublists by number using list
void merge(vector<Row> &list, int left, int mid, int right)
{
    std::list<Row> L(list.begin() + left, list.begin() + mid + 1);
    std::list<Row> R(list.begin() + mid + 1, list.begin() + right + 1);

    int k = left;
    while (!L.empty() && !R.empty())
    {
        if (L.front().number <= R.front().number)
        {
            list[k++] = L.front();
            L.pop_front();
        }
        else
        {
            list[k++] = R.front();
            R.pop_front();
        }
    }
    while (!L.empty())
    {
        list[k++] = L.front();
        L.pop_front();
    }
    while (!R.empty())
    {
        list[k++] = R.front();
        R.pop_front();
    }
}

// Merge sort for vector<Row>, sorting by number only
void mergeSort(vector<Row> &list, int left, int right, ofstream &writer)
{
    if (left < right)
    {
        // 1. Divide: Find the middle index to split the list into two halves
        int mid = (left + right) / 2;
        // 2. Recursion: Sort the left half
        mergeSort(list, left, mid, writer);
        // 2. Recursion: Sort the right half
        mergeSort(list, mid + 1, right, writer);
        // 3. Conquer: Merge the two sorted halves
        merge(list, left, mid, right);
    }
}

void mergeSort(vector<Row> &list, ofstream &writer)
{
    // Start the merge sort process
    mergeSort(list, 0, list.size() - 1, writer);
}

int main()
{
    // Adjust the filename as needed
    vector<Row> rows = readDataset("dataset_sample_1000.csv");
    // Get the size of the dataset
    int size = rows.size();
    // Create output file name based on dataset size
    ostringstream oss;
    oss << "merge_sort_" << size << ".csv";
    string outputFile = oss.str();

    // Open the output file for writing
    ofstream writer(outputFile);
    if (!writer.is_open())
    {
        cout << "Error writing to file: " << outputFile << endl;
        return 1;
    }

    cout << "Merge Sort with C++" << endl;
    cout << "Dataset size: " << size << endl;

    // Start timing the merge sort operation
    auto startTime = chrono::high_resolution_clock::now();
    // Start the merge sort algorithm
    mergeSort(rows, writer);
    auto endTime = chrono::high_resolution_clock::now();
    // Calculate the duration of the sort operation
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime).count();
    cout << "Time Taken: " << duration << " ms" << endl;
    printRowList(rows, writer);
    cout << "Merge sort with C++ completed.\nCheck " << outputFile << " for results." << endl;
    return 0;
}