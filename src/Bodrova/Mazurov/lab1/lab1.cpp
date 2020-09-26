#include <iostream>
#include<vector>
#include<Windows.h>
#include<thread>

#define threadCount 3
using namespace std;

struct Ldata {
	int idThread;
	const vector<int> * matrix;
	static vector<pair<int, int>> maxAndCount;
	
};

vector<pair<int, int>> Ldata::maxAndCount = vector<pair<int, int>>(threadCount);

void findCountMax(const vector<int> * matrix,int start, int end, pair<int, int> &maxAndCount)
{
	int count = 1, max = matrix->at(start);
	for (int i = start + 1;i < end;i++) {
		if (matrix->at(i) > max) {
			max = matrix->at(i);
			count = 1;
		}
		else if (matrix->at(i) == max)
			count++;
	}
	maxAndCount = make_pair(max, count);
}

DWORD WINAPI findCountMatrixParallel(void *args) {
	Ldata Data = *(Ldata*)args;
	int start = Data.idThread*(Data.matrix->size() / threadCount);
	int end = (Data.idThread + 1)*(Data.matrix->size() / threadCount);
	if (Data.idThread == threadCount - 1) {
		
		for (int i = start;i < end;i++) {
			this_thread::sleep_for(chrono::milliseconds(1000));
			pair<int, int> maxAndCount;
			findCountMax(Data.matrix, start, end, maxAndCount);
			Data.maxAndCount[Data.idThread] = maxAndCount;
		}
		return 0;
	}
	for (int i = start;i < end;i++){
		this_thread::sleep_for(chrono::milliseconds(1000));
		pair<int, int> maxAndCount;
		findCountMax(Data.matrix, start, end, maxAndCount);
		Data.maxAndCount[Data.idThread] = maxAndCount;
	}
	 
	return 0;
	
}

int main() {

	vector<int> matrix = { 1,1,3,
						4,5,5,
						9,9,9 };

	HANDLE threads[threadCount];
	DWORD threadsID[threadCount];
	Ldata ldata[threadCount];

	for (int i = 0;i < threadCount;i++) {
		ldata[i].matrix = &matrix;
		ldata[i].idThread = i;
		threads[i] = CreateThread(NULL, 0, &findCountMatrixParallel, &ldata[i], 0, &threadsID[i]);
	}

	WaitForMultipleObjects(threadCount, threads, TRUE, INFINITE);
	for (int i = 0;i < threadCount;i++) {
		cout << ldata[i].maxAndCount[i].first << " " << ldata[i].maxAndCount[i].second << endl;
	}
	int COUNT = Ldata::maxAndCount[0].second;
	int temp= Ldata::maxAndCount[0].first;
	for (int i = 1;i < Ldata::maxAndCount.size();i++) {
		if (Ldata::maxAndCount[i].first>temp) {
			temp = Ldata::maxAndCount[i].first;
			COUNT = Ldata::maxAndCount[i].second;
		}
		else if(Ldata::maxAndCount[i].first==temp) 
			COUNT+=Ldata::maxAndCount[i].second;
	}
	cout << COUNT << endl;
	system("pause");
	return 0;
}