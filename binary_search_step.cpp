#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
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
    if (!file.is_open())
    {
        cout << "Error reading file: " << filename << endl;
        return rows;
    }

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
                // If stoi fails, ignore that row
            }
        }
    }
    return rows;
}

// Perform binary search and log every step
void binarySearchStep(const vector<Row> &data, int target, ofstream &writer)
{
    int left = 0;
    int right = data.size() - 1;
    bool found = false;

    while (left <= right)
    {
        int mid = left + (right - left) / 2;
        writer << mid << ": " << data[mid].toString() << endl;

        if (data[mid].number == target)
        {
            found = true;
            break;
        }
        else if (data[mid].number < target)
        {
            left = mid + 1;
        }
        else
        {
            right = mid - 1;
        }
    }

    if (!found)
    {
        writer << -1 << endl;
    }
}

int main()
{
    string filename;
    int target;

    cout << "Enter dataset filename (e.g. merge_sort_10000.csv): ";
    cin >> filename;

    vector<Row> rows = readDataset(filename);

    if (rows.empty())
    {
        cout << "No data loaded. Exiting." << endl;
        return 1;
    }

    cout << "Enter target integer: ";
    cin >> target;

    string outputFile = "binary_search_step_" + to_string(target) + ".txt";
    ofstream writer(outputFile);
    if (!writer.is_open())
    {
        cout << "Error writing to file: " << outputFile << endl;
        return 1;
    }

    binarySearchStep(rows, target, writer);

    cout << "Binary search with C++ completed.\nCheck " << outputFile << " for results." << endl;
    return 0;
}
