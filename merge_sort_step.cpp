#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <list>
#include <string>

using namespace std;

struct Row
{
    int number;
    string text;
    Row(int n, const string &t) : number(n), text(t) {}
    string toString() const
    {
        return to_string(number) + "/" + text;
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
                // Skip malformed lines
            }
        }
    }
    return rows;
}

// Prints the list in [number/string, ...] format to both terminal and file
void printRowList(const vector<Row> &list, ofstream &writer, const string &prefix = "")
{
    ostringstream oss;
    oss << prefix << "[";
    for (size_t i = 0; i < list.size(); ++i)
    {
        oss << list[i].toString();
        if (i < list.size() - 1)
            oss << ", ";
    }
    oss << "]";
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
        // Print the list after each merge (conquering) step
        printRowList(list, writer);
    }
}

int main()
{
    vector<Row> rows = readDataset("dataset_sample_1000.csv");
    int start, end;
    cout << "Select start row: ";
    cin >> start;
    cout << "Select end row: ";
    cin >> end;

    if (start < 1 || end > (int)rows.size() || start >= end + 1)
    {
        cout << "Invalid range." << endl;
        return 1;
    }

    // Copy the selected range into a new vector (start and end are 1-based, inclusive)
    vector<Row> selectedRows(rows.begin() + (start - 1), rows.begin() + end);

    // Output file name based on the selected range
    ostringstream oss;
    oss << "merge_sort_step_" << start << "_" << end << ".txt";
    string outputFile = oss.str();
    ofstream writer(outputFile);

    if (!writer.is_open())
    {
        cout << "Error writing to file: " << outputFile << endl;
        return 1;
    }

    printRowList(selectedRows, writer);
    mergeSort(selectedRows, 0, selectedRows.size() - 1, writer);
    printRowList(selectedRows, writer);

    cout << "Merge sort completed.\nCheck " << outputFile << " for results." << endl;
    return 0;
}